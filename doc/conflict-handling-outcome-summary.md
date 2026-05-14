# FSCL Create-Conflict Handling: Outcome Summary

## Scope
This summary captures the agreed design outcomes for concurrent CreateRequest handling across views and service instances, with emphasis on eventual consistency and reconciliation behavior.

Primary reference diagrams:
- [Case 1](data-management-concept/create-conflict-case-1-same-view-sequential.seq.puml): same view, sequential
- [Case 2](data-management-concept/create-conflict-case-2-same-view-concurrent.seq.puml): same view, concurrent
- [Case 3](data-management-concept/create-conflict-case-3-cross-view-sequential.seq.puml): cross view, sequential
- [Case 4](data-management-concept/create-conflict-case-4-cross-view-concurrent-reconcile.seq.puml): cross view, concurrent with reconciliation

## Final Architecture Decisions

1. Same-view conflicts (Case 1 and Case 2)
- Resolved by normal transactional processing and DB constraints.
- No in-memory cross-instance lock/cache is required for correctness.
- Expected result in race: one request succeeds, one returns pre-existing conflict.

2. Cross-view conflicts, sequential (Case 3)
- First committed create is propagated by event.
- Subsequent create in another view checks shadow state and returns SHADOW_IDENTICAL or SHADOW_DIFFERENT behavior.

3. Cross-view conflicts, concurrent (Case 4)
- No deterministic winner policy for now.
- Both variants are kept under the same business resource id Rk.
- Resource is flagged RECONCILIATION_NEEDED.
- User references are stored for offline coordination.

4. Echo and duplicate handling
- Use durable inbox-ledger markers per consumer view and event id.
- Deduplication is by event id, not by resource existence.
- Own bus echo is treated as duplicate-event no-op after ledger check.

5. Conflict detection location
- Conflict detection is service-side logic.
- Flow is load first, detect conflict in service, then persist according to decision.

## Policy Matrix (Current)

Command-side policy matrix (adapted to current decisions):

| Case | Scenario | Immediate command outcomes | Event behavior | Converged handling |
|---|---|---|---|---|
| 1 | Same view, sequential | First `201`, second `409 VIEW_PREEXISTING` | One created event from winning command | Normal single-resource state in that view |
| 2 | Same view, concurrent | One `201`, one `409 VIEW_PREEXISTING` | One created event from winning command | Transaction/uniqueness resolve race, no in-memory distributed lock |
| 3 | Cross view, sequential | First view `201`; second create follows `SHADOW_IDENTICAL` or `SHADOW_DIFFERENT` path | First created event is consumed by other view before second command decision | Second command is decided from shadow state |
| 4 | Cross view, concurrent | Both local commands may return `201` | Both created events are consumed; each side runs dedupe + find + conflict detect | Keep both variants under same `Rk`, set `RECONCILIATION_NEEDED`, store user refs, notify clients |

Event-consumer decision matrix (applies in both views):

| Step | Condition | Action |
|---|---|---|
| Inbox insert | `event_id` already seen for consumer view | No-op commit (duplicate/echo). |
| Find | `find(R1) == None` | Save as no-conflict state. |
| Detect conflict | `find(R1) != None` and incoming equivalent | Save as no-conflict/idempotent state. |
| Detect conflict | `find(R1) != None` and incoming differs | Save conflict variant, set `RECONCILIATION_NEEDED`, store user refs, notify clients. |

This matrix is intentionally aligned to the pseudocode below (`find`, `detect_conflict`, decision-based apply).

## Key Pseudocode (Implementation Guide)

Event handling per consumer view:

```rust
begin_tx();
inserted = inbox.try_insert(event_id, consumer_view);
if !inserted {
    // duplicate or own echo
    commit_tx();
    return;
}

found = find(R1);
decision = detect_conflict(found, incoming_data);

if decision == ConflictNeedsReconciliation {
    save_conflict_variant(R1, source_event_id, incoming_data);
    set_reconciliation_status(R1, RECONCILIATION_NEEDED);
    store_user_references(R1);
    commit_tx();
    notify_clients_reconciliation_needed(R1);
} else {
    save_no_conflict(found, incoming_data);
    commit_tx();
}
```

Conflict decision logic (concise):

```rust
fn detect_conflict(found, incoming_data) -> Decision {
    if found.is_none() {
        return NoConflict;
    }
    if equivalent(found, incoming_data) {
        return NoConflict;
    }
    ConflictNeedsReconciliation
}
```

Command-side create (high level):

```rust
begin_tx();
process_create_request(view, R1, Dk);
save_view_and_shadow();
append_outbox_created_event();
commit_tx();
```

## Inbox Ledger Retention Policy
Two-lane retention policy is agreed:

1. If reconciliation case exists for R1
- Keep inbox ledger entries until reconciliation is complete.
- Purge after a grace period.

2. If no reconciliation case exists for R1
- Purge after TTL + grace period.

Operational note:
- Use an operational time buffer when defining TTL/grace horizons.
- Do not rely on blind monthly stream resets for safety.

## Deferred Feature Set (Planned)
- User-driven reconciliation workflow:
  - exchange user references
  - discard one variant
  - keep both and assign new resource id to one
  - merge by user-defined resolution

## Documentation Updated in This Track
- [Data Management Concept Index](data-management-concept/index.md)
- [Case 1 Diagram](data-management-concept/create-conflict-case-1-same-view-sequential.seq.puml)
- [Case 2 Diagram](data-management-concept/create-conflict-case-2-same-view-concurrent.seq.puml)
- [Case 3 Diagram](data-management-concept/create-conflict-case-3-cross-view-sequential.seq.puml)
- [Case 4 Diagram](data-management-concept/create-conflict-case-4-cross-view-concurrent-reconcile.seq.puml)
