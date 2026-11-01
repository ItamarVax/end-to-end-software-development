#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p testlib
jar=testlib/junit-platform-console-standalone-6.0.0.jar
expected=d2aa0758805b83c4253a3899671a58fffcad208419b5e52d6ff7cbe1b23c6297
if [[ ! -f "$jar" ]]; then
    tmp=$(mktemp testlib/junit-download.XXXXXX)
    trap 'rm -f "$tmp"' EXIT
    curl --fail --location --proto '=https' \
        https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/6.0.0/junit-platform-console-standalone-6.0.0.jar \
        -o "$tmp"
    printf '%s  %s\n' "$expected" "$tmp" | shasum -a 256 -c -
    mv "$tmp" "$jar"
fi
printf '%s  %s\n' "$expected" "$jar" | shasum -a 256 -c -
