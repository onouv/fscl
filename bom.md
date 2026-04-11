
# SOFTWARE BOM
| Folder                    | git repo/ container       | k8s               | Description |
| ---                       | ---                       | ---               |---|
| `fscl/`                   | `fscl`                    | n/a               | Overall documentation, project management |
| `fscl-core/`              | `fscl-core`               | n/a               | Rust backend library of core types and utils |
| `fscl-frontend/`          | `fscl-frontend`           | `fscl/frontend`        | Common web frontend |
| `fscl-process-svc/`       | `fscl-process-svc`        | `fscl-process/api`     | Microservice for process view (Rust) |
| `fscl-outbox-publisher`   | `fscl-outbox-publisher`   | `outbox-publisher`| Sidecar service to publish FSCL domain events into a NATS/JetStream cluster | 
| n/a                       | `fscl-database`           | `fscl-process/database` | postgres database for process view | 