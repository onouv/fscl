#!/usr/bin/env bash

if [[ "${BASH_SOURCE[0]}" == "$0" ]]; then
  echo "Run this script with: source compose/load-secrets.sh" >&2
  exit 1
fi

script_dir="$(builtin cd -- "$(dirname -- "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd -P)"
secrets_file="${script_dir}/secrets.local"

if [[ ! -f "${secrets_file}" ]]; then
  echo "Missing ${secrets_file}. Create it from compose/secrets.example." >&2
  return 1
fi

set -a
# shellcheck disable=SC1090
source "${secrets_file}"
source_status=$?
set +a

if [[ ${source_status} -ne 0 ]]; then
  echo "Failed to load ${secrets_file}" >&2
  return "${source_status}"
fi

echo "Loaded FSCL compose secrets from ${secrets_file}"
