#!/usr/bin/env bash
set -euo pipefail
REGISTRY=${REGISTRY:-}
VERSION=${VERSION:-0.0.1}
MODULES_FILE=${1:-modules.txt}
if [[ -z "$REGISTRY" ]]; then
  echo "REGISTRY is empty. Skipping push (images stay local)."; exit 0; fi
while read -r m; do
  [[ -z "$m" ]] && continue
  docker push "${REGISTRY}/${m}:${VERSION}"
done < "$MODULES_FILE"
