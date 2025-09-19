#!/usr/bin/env bash
set -euo pipefail
NS=${NS:-pos}
RELEASE=${RELEASE:-pos}
VALUES=${VALUES:-deploy/helm/pos/values-dockerdesktop.yaml}
helm upgrade --install "$RELEASE" deploy/helm/pos -n "$NS" -f "$VALUES"
