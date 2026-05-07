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
docker compose -p fscl-process -f compose/process-stack.yaml up -d
```

`load-secrets.sh` must be sourced, not executed directly, so the exported variables stay in your current shell.

Use a different Compose project name and stack file for each view to run multiple stacks in parallel.