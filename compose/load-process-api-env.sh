#!/usr/bin/env bash

script_dir="$(builtin cd -- "$(dirname -- "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd -P)"

load_env() {
  # Load shared secrets first.
  # shellcheck disable=SC1090
  source "${script_dir}/load-secrets.sh" >/dev/null

  export DB_TYPE=postgres
  export DB_HOST=127.0.0.1
  export DB_PORT="${FSCL_PROCESS_DB_HOST_PORT:-5432}"
  export DB_USER="${FSCL_DB_USER:?FSCL_DB_USER is required}"
  export DB_PASSWORD="${FSCL_DB_PASSWORD:?FSCL_DB_PASSWORD is required}"
  export DB_NAME="${FSCL_DB_NAME:?FSCL_DB_NAME is required}"

  export APP_HOST=127.0.0.1
  export APP_PORT="${FSCL_PROCESS_API_HOST_PORT:-3100}"

  export NATS_URL="nats://${FSCL_NATS_USER:?FSCL_NATS_USER is required}:${FSCL_NATS_PASSWORD:?FSCL_NATS_PASSWORD is required}@127.0.0.1:${FSCL_PROCESS_NATS_CLIENT_PORT:-4222}"

  # Useful defaults for interactive debugging; callers can override before sourcing.
  export RUST_LOG="${RUST_LOG:-debug}"
  export RUST_BACKTRACE="${RUST_BACKTRACE:-1}"
}

write_debug_env_file() {
  local output_file="$1"

  mkdir -p "$(dirname "${output_file}")"
  cat > "${output_file}" <<EOF
DB_TYPE=${DB_TYPE}
DB_HOST=${DB_HOST}
DB_PORT=${DB_PORT}
DB_USER=${DB_USER}
DB_PASSWORD=${DB_PASSWORD}
DB_NAME=${DB_NAME}
APP_HOST=${APP_HOST}
APP_PORT=${APP_PORT}
NATS_URL=${NATS_URL}
NATS_JETSTREAM_STREAM=${NATS_JETSTREAM_STREAM:-fscl-events}
NATS_JETSTREAM_DURABLE_CONSUMER=${NATS_JETSTREAM_DURABLE_CONSUMER:-process-api}
NATS_JETSTREAM_ACK_POLICY=${NATS_JETSTREAM_ACK_POLICY:-explicit}
NATS_JETSTREAM_ACK_WAIT=${NATS_JETSTREAM_ACK_WAIT:-30s}
RUST_LOG=${RUST_LOG:-debug}
RUST_BACKTRACE=${RUST_BACKTRACE:-1}
EOF
}

if [[ "${BASH_SOURCE[0]}" != "$0" ]]; then
  load_env
  echo "Loaded process-api host debug environment"
  return 0
fi

case "${1:-}" in
  --write-debug-env)
    load_env
    output_path="${2:-${script_dir}/../.vscode/process-api.debug.env}"
    write_debug_env_file "${output_path}"
    echo "Wrote ${output_path}"
    ;;
  *)
    echo "Usage:" >&2
    echo "  source compose/load-process-api-env.sh" >&2
    echo "  bash compose/load-process-api-env.sh --write-debug-env [output-file]" >&2
    exit 2
    ;;
esac
