# FSCL Dev Compose

This scaffolding follows the same config split as the Kubernetes manifests:

- `.env.shared`: shared local-development values used across services
- `fscl-process-svc/.env`: process-api local values
- `fscl-outbox-publisher/.env`: outbox publisher local values

Code-level messaging contract constants such as `OUTBOX_NOTIFY_CHANNEL` stay in `fscl-messaging` and are not carried in the env files.

Start the local stack with:

```sh
cp .env.shared.example .env.shared
cp fscl-process-svc/.env.example fscl-process-svc/.env
cp fscl-outbox-publisher/.env.example fscl-outbox-publisher/.env
docker compose -f compose/infra.yaml -f compose/process-stack.yaml up
```

The Compose files intentionally override `DB_HOST` and `NATS_URL` for container-to-container networking while leaving the checked-in examples usable for host-based `cargo run` as well.