# FSCL Dev Compose

Each view is modeled as a self-contained stack file named `<viewname>-stack.yaml`.
The corresponding API service inside the stack is named `<viewname>-api`.

Example:

- `compose/process-stack.yaml`
- service `process-api`
- service `process-outbox-publisher` (view-specific outbox sidecar instance)

Credentials are not stored in tracked `.env` files. Instead, Compose reads credential values
from exported environment variables loaded by a local script.

Code-level messaging contract constants such as `OUTBOX_NOTIFY_CHANNEL` stay in `fscl-messaging` and are not carried as deployment config.

## Local Secrets Flow

1. Create local secrets file from template:

```sh
cp compose/secrets.example compose/secrets.local
```

2. Edit `compose/secrets.local` with your local credential values.

3. Export them into your shell and start the stack:

```sh
source compose/load-secrets.sh
docker compose -p fscl -f compose/process-stack.yaml --profile infra --profile app up -d
```

`load-secrets.sh` must be sourced, not executed directly, so the exported variables stay in your current shell.

For host-side process-api debugging, load API env vars as well:

```sh
source compose/load-process-api-env.sh
```

## NATS CLI Without Local Install

`compose/process-stack.yaml` provides an optional `nats-cli` service using `natsio/nats-box`.
It is gated behind the `tools` profile, so it only runs when explicitly requested.

Start the stack with tools profile:

```sh
source compose/load-secrets.sh
docker compose -p fscl -f compose/process-stack.yaml --profile infra --profile tools up -d
```

Start only infrastructure services (Postgres + NATS) for local debugging:

```sh
source compose/load-secrets.sh
docker compose -p fscl -f compose/process-stack.yaml --profile infra up -d
```

`infra` starts only base infrastructure services (`postgres` and `nats`).
It does not start view services (`process-api`, `process-outbox-publisher`) or CLI tool containers.

Run NATS CLI commands via the tools container:

```sh
docker compose -p fscl -f compose/process-stack.yaml exec nats-cli \
	nats --server "$NATS_SERVER" stream ls
```

Open an interactive shell in the CLI container:

```sh
docker compose -p fscl -f compose/process-stack.yaml exec nats-cli sh
```

If your main stack is already running without tools, you can start only the CLI service:

```sh
docker compose -p fscl -f compose/process-stack.yaml --profile infra --profile tools up -d nats-cli
```

## PostgreSQL CLI Without Local Install

`compose/process-stack.yaml` also provides an optional `postgres-cli` service in the `tools` profile.

Start infra plus tools:

```sh
source compose/load-secrets.sh
docker compose -p fscl -f compose/process-stack.yaml --profile infra --profile tools up -d
```

Open a `psql` shell (connection env vars are preconfigured in the container):

```sh
docker compose -p fscl -f compose/process-stack.yaml exec postgres-cli psql
```

Run one-off SQL command:

```sh
docker compose -p fscl -f compose/process-stack.yaml exec postgres-cli \
	psql -c 'select now();'
```
