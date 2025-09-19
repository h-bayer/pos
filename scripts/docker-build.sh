#!/usr/bin/env bash
set -euo pipefail
REGISTRY=${REGISTRY:-}
VERSION=${VERSION:-0.0.1}
MODULES_FILE=${1:-modules.txt}
while read -r m; do
  [[ -z "$m" ]] && continue
  IMAGE_TAG="${m}:${VERSION}"
  if [[ -n "${REGISTRY}" ]]; then IMAGE_TAG="${REGISTRY}/${IMAGE_TAG}"; fi
  echo "==> Building image ${IMAGE_TAG}"
  docker build -f docker/Dockerfile.jvm \
    --build-arg MODULE="$m" \
    -t "${IMAGE_TAG}" .
done < "$MODULES_FILE"
