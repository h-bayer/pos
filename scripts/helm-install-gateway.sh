#!/usr/bin/env bash
set -euo pipefail
NS=${NS:-pos}
helm upgrade --install gateway deploy/helm/gateway -n "$NS"
