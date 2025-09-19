#!/usr/bin/env bash
set -euo pipefail
helm dependency update deploy/helm/pos || true
