# Architectural Layers & Resource Identity Boundaries

## Problem: Application Layer ResourceId Usage

When designing application-layer handlers and repositories, a question arises: **Should ports and domain contracts use `ResourceId` value objects, or should we keep identity decomposed (project_id, resource_id_local) and only construct ResourceId at the domain boundary?**

This document explains the prudent architectural approach and the reasoning behind it.

## The Tension

**Surface question:** Where does ResourceId construction happen, and who knows about its composite structure (project_id + local_id + format)?

**Naive approaches and their problems:**

1. **Option A: Ports use decomposed fields**
   - Ports take `(project_id, resource_id_local, format)` as parameters
   - **Problem:** Ports become infrastructure-aware (know about three separate parameters instead of a domain concept)
   - **Problem:** Every adapter must deal with reconstruction logic
   - **Problem:** Breaks the abstraction—ports should hide infrastructure details, not encode them

2. **Option B: Ports use ResourceId everywhere, including DTOs**
   - HTTP/RPC DTOs construct ResourceId before the application layer even executes
   - **Problem:** DTOs become tightly coupled to domain validation logic
   - **Problem:** Validation errors leak into API contract (format resolution happens before auth, transactionality, etc.)
   - **Problem:** DTOs are just data containers; making them domain-aware violates their purpose

## The Prudent Approach: Layered Responsibility

ResourceId is a **domain value object**. Its construction and use must respect layer boundaries:

### Layer 1: API/HTTP/RPC (Presentation)
**Input DTOs keep fields decomposed:**
```rust
pub struct CreateComponentRequest {
    pub project_id: ProjectId,           // Raw input
    pub resource_id_local: String,       // Raw input
    pub name: String,
}
```

**Rationale:**
- DTOs are containers for external input, not domain concepts
- Serialization/deserialization is simpler with flat fields
- Decouples HTTP contract from domain validation rules
- Clear about what the caller is providing (raw project_id and local_id)

### Layer 2: Application Business Logic (Use Cases)
**Orchestrate raw input → domain value objects:**
```rust
pub async fn create_component(
    &self, 
    request: CreateComponentRequest, 
    tx: &mut Tx
) -> Result<ComponentCreated> {
    // 1. Resolve format by project_id
    let format = self.format_repo.load(tx, &request.project_id)
        .await?
        .ok_or(ApplicationError::ProjectNotInitialized)?;
    
    // 2. Construct the domain value object (HERE, internally)
    let resource_id = ResourceId::new(
        request.project_id.clone(),
        request.resource_id_local.clone(),
        format,
    )?;
    
    // 3. Pass to domain logic
    let component = Component::new(resource_id, request.name, ...)?;
    
    // ... publish events, update shadow, etc.
}
```

**Rationale:**
- The application layer glues raw input into domain concepts
- It orchestrates external dependencies (format resolution, transactions)
- ResourceId construction is **internal orchestration**, not a boundary violation
- Format resolution happens here because it's a transactional concern (within the UoW)

### Layer 3: Ports (Domain Contracts)
**Use ResourceId in all signatures:**
```rust
#[async_trait]
pub trait ComponentRepositoryPort: Send + Sync {
    async fn find(&self, tx: &mut Tx, resource_id: &ResourceId) -> Result<Option<Component>>;
    async fn upsert_component(&self, tx: &mut Tx, component: Component) -> Result<()>;
    async fn delete_component(&self, tx: &mut Tx, resource_id: &ResourceId) -> Result<()>;
}
```

**Rationale:**
- Ports represent **how domain concepts are persisted/communicated**
- They are **not** infrastructure contracts; they are **domain contracts implemented by infrastructure**
- Ports should speak domain language (ResourceId), never infrastructure language
- Ports hide how the infrastructure stores/retrieves ResourceId
- If you swap PostgreSQL for MongoDB, both adapters implement the same port by decomposing ResourceId differently

### Layer 4: Infrastructure Implementation
**Adapt domain concepts to storage/messaging:**
```rust
#[async_trait]
impl ComponentRepositoryPort for PgComponentRepository {
    async fn find(&self, tx: &mut Tx, resource_id: &ResourceId) -> Result<Option<Component>> {
        let row = sqlx::query(
            "SELECT * FROM components WHERE project_id = $1 AND resource_id_local = $2"
        )
        .bind(resource_id.project_id().to_string())
        .bind(resource_id.local_id())
        .fetch_optional(tx)
        .await?;
        
        if let Some(row) = row {
            Ok(Some(Component::from_row(row)?))
        } else {
            Ok(None)
        }
    }
}
```

**Rationale:**
- Infrastructure adapters decompose domain value objects as needed
- They know storage-specific details (SQL column names, message formats)
- They don't impose those details on the application or domain layers
- Multiple adapters can implement the same port by adapting differently

## Layer Responsibility Matrix

| Layer | Construct ResourceId? | Use Decomposed Fields? | Use Full ResourceId? |
|-------|----------------------|----------------------|----------------------|
| **Presentation (HTTP/RPC DTOs)** | ❌ No | ✅ Yes (project_id, resource_id_local) | ❌ No |
| **Application (Use Cases/UoW)** | ✅ Yes (after format resolution) | ✅ Yes (in request DTOs) | ✅ Yes (internally) |
| **Domain (Entities/Value Objects)** | ✅ Always | ❌ No | ✅ Always |
| **Ports (Interfaces)** | ❌ No | ❌ No | ✅ Yes (contracts) |
| **Infrastructure (Adapters)** | ❌ No | ✅ Yes (extract from ResourceId) | ✅ Yes (receive from ports) |

## Key Insights

### Insight 1: Ports Are Domain Contracts, Not Infrastructure Contracts

The critical realization is that **ports are where the application layer speaks to the domain's external dependencies**. They are not "database interfaces"—they are "how the domain concept is persisted." The port itself should be domain-aware (use ResourceId), even though the implementation is infrastructure-aware.

Think of it as: "Tell me how to find a component by its identity; I don't care if you use SQL, NoSQL, or a filing cabinet."

### Insight 2: Layer Boundaries Protect Against Coupling

By keeping ResourceId construction internal to the application layer:
- API/HTTP contract never depends on ResourceId validation rules
- If ResourceId validation changes, only application logic is affected, not HTTP clients
- Ports present a stable, domain-focused abstraction despite internal changes

### Insight 3: Application Layer Is Orchestration, Not Infra

The application layer is **allowed** to work with domain value objects. It's not a violation because application use cases are **part of the domain logic**, not infrastructure. The orchestration of "resolve format → construct ResourceId → create component → publish event" is a domain use case, not an infrastructure concern.

## Anti-Pattern: What NOT to Do

### ❌ Anti-Pattern 1: Ports with Decomposed Fields
```rust
// WRONG: Ports expose infrastructure details
#[async_trait]
pub trait ComponentRepositoryPort {
    async fn find(
        &self, 
        tx: &mut Tx, 
        project_id: &ProjectId,           // Exposed detail
        resource_id_local: &str,          // Exposed detail
        format: &IdFormat,                // Exposed detail
    ) -> Result<Option<Component>>;
}
```
**Problem:** This is not a domain contract; it's infrastructure-specific. Changing how you store resource identities would require changing all adapters.

### ❌ Anti-Pattern 2: ResourceId in HTTP DTOs
```rust
// WRONG: DTOs become domain-aware
#[derive(Deserialize)]
pub struct CreateComponentRequest {
    pub resource_id: ResourceId,  // Validation happens before transaction!
}
```
**Problem:** Validation errors leak into HTTP contract. Validation must happen within a transactional boundary (after format is resolved).

### ❌ Anti-Pattern 3: Domain Services Take Decomposed Fields
```rust
// WRONG: Domain service doesn't know its own value objects
impl ComponentService {
    pub async fn create(
        &self, 
        project_id: ProjectId,
        resource_id_local: String, 
        format: IdFormat,
    ) -> Result<Component> {
        // Must construct ResourceId here, but shouldn't
    }
}
```
**Problem:** The domain service is forced to know about three parameters instead of one domain concept.

## Summary: The Prudent Pattern

```
HTTP Input (project_id, resource_id_local)
    ↓
Application Layer: Resolve format, construct ResourceId, call domain logic
    ↓
Domain Layer: Use ResourceId as value object
    ↓
Ports: Accept/return ResourceId (domain contracts)
    ↓
Adapters: Decompose ResourceId for storage/messaging
```

This pattern:
- ✅ Keeps layer boundaries clean
- ✅ Ports are domain-focused, not infrastructure-focused
- ✅ Application is orchestration, not just a pass-through
- ✅ DTOs remain simple containers
- ✅ Easy to test (mock ports that speak ResourceId)
- ✅ Easy to swap infrastructure (different adapters implement same port)
