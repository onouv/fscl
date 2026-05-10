# Utils Scripts

This folder contains small helper scripts for local FSCL development with Docker Compose.

- `infra-up`
  - Starts infra services only (`postgres`, `nats`) from `compose/process-stack.yaml`.

- `infra-down`
  - Stops infra services (`postgres`, `nats`).

- `psql-up`
  - Starts the `postgres-cli` tools container (and required infra profile context).

- `psql`
  - Runs `psql` inside the already running `postgres-cli` container.
  - Passes all arguments through.

- `nats-cli-up`
  - Starts the `nats-cli` tools container (and required infra profile context).

- `tools-up`
  - Starts both tools containers together (`postgres-cli`, `nats-cli`).

- `tools-down`
  - Stops both tools containers together (`postgres-cli`, `nats-cli`).

- `natscli`
  - Runs `nats` CLI inside the already running `nats-cli` container.
  - Uses `NATS_SERVER` from that container.
  - Passes all arguments through.

- `prep-process-view`
  - Starts infra (`postgres`, `nats`).
  - Starts only the process view outbox sidecar (`process-outbox-publisher`).
  - Writes `.vscode/process-api.debug.env` for host-side API debugging.

- `create`
  - posting create requests to API endpoints.

# Minimal Workflow

### 1. Start infra:

```sh
./utils/infra-up
```

### 2. Start both CLI tool containers:

```sh
./utils/tools-up
```

Alternative: start them individually:

```sh
./utils/psql-up
./utils/nats-cli-up
```

### 3. Use the CLIs:

```sh
./utils/psql
./utils/psql -c 'select now();'

./utils/natscli stream ls
./utils/natscli consumer ls fscl-events
```

### 4. Prepare process view debugging with outbox sidecar:

```sh
./utils/prep-process-view
```

### 5. Start the view service debugger in VS Code and send requests, e.g. by using `utils/create`

### 6. Stop infra when done:

```sh
./utils/infra-down
```

## Notes

- Scripts source `compose/load-secrets.sh` where needed.
- `psql` and `natscli` expect their corresponding `*-up` container to be running.
- `tools-up` is the fastest way to make both CLIs available.
- Scripts are shell-friendly and work in normal terminals and tmux.
