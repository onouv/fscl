#!/usr/bin/env bash

set -euo pipefail

BASE_URL="${PROCESS_API_BASE_URL:-http://127.0.0.1:3100}"
ENDPOINT="${BASE_URL%/}/api/v2/component/"

require_command() {
    if ! command -v "$1" >/dev/null 2>&1; then
        echo "Missing required command: $1" >&2
        exit 1
    fi
}

pretty_print_json() {
    if command -v jq >/dev/null 2>&1; then
        jq .
    elif command -v python3 >/dev/null 2>&1; then
        python3 -m json.tool
    else
        cat
    fi
}

prompt_required() {
    local label="$1"
    local value=""

    while [[ -z "$value" ]]; do
        read -r -p "$label: " value
    done

    printf '%s' "$value"
}

build_payload() {
    if command -v python3 >/dev/null 2>&1; then
        RESOURCE_ID="$resource_id" NAME="$name" DESCRIPTION="$description" python3 - <<'PY'
import json
import os

print(json.dumps({
    "id": os.environ["RESOURCE_ID"],
    "name": os.environ["NAME"],
    "description": os.environ["DESCRIPTION"],
}))
PY
    elif command -v jq >/dev/null 2>&1; then
        jq -cn \
            --arg id "$resource_id" \
            --arg name "$name" \
            --arg description "$description" \
            '{id: $id, name: $name, description: $description}'
    else
        echo "Missing required command: python3 or jq" >&2
        exit 1
    fi
}

require_command curl

resource_id="$(prompt_required "resource_id")"
name="$(prompt_required "name")"
read -r -p "description: " description

payload="$(build_payload)"

tmp_body="$(mktemp)"
trap 'rm -f "$tmp_body"' EXIT

http_status="$(curl -sS \
    -o "$tmp_body" \
    -w '%{http_code}' \
    -X POST "$ENDPOINT" \
    -H 'Content-Type: application/json' \
    --data "$payload")"

echo "POST $ENDPOINT"
echo "HTTP $http_status"
pretty_print_json < "$tmp_body"

if [[ ! "$http_status" =~ ^2 ]]; then
    exit 1
fi