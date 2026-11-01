#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
if [[ -n "${JAVA_HOME:-}" ]]; then
    jdk="$JAVA_HOME"
elif [[ -x /opt/homebrew/opt/openjdk@25/bin/javac ]]; then
    jdk=/opt/homebrew/opt/openjdk@25
else
    jdk=$(/usr/libexec/java_home -v 25)
fi
if [[ "$("$jdk/bin/javac" -version 2>&1)" != javac\ 25* ]]; then
    echo 'JDK 25 is required. Set JAVA_HOME to a JDK 25 installation.' >&2
    exit 2
fi
jar=testlib/junit-platform-console-standalone-6.0.0.jar
if [[ ! -f "$jar" ]]; then
    echo 'JUnit is missing. Run bash scripts/setup-tests.sh first.' >&2
    exit 2
fi
sources=()
for directory in model/src logic/src src tests; do
    if [[ -d "$directory" ]]; then
        while IFS= read -r -d '' file; do
            sources+=("$file")
        done < <(find "$directory" -type f -name '*.java' -print0)
    fi
done
# Delete only this runner's generated class directory to avoid stale test results.
rm -rf build/test-classes
mkdir -p build/test-classes
if (( ${#sources[@]} > 0 )); then
    "$jdk/bin/javac" --release 25 -cp "lib/*:$jar" -d build/test-classes "${sources[@]}"
fi
classpath="build/test-classes"
for dependency in lib/*.jar; do
    [[ -f "$dependency" ]] && classpath="$classpath:$dependency"
done
"$jdk/bin/java" -jar "$jar" execute --class-path "$classpath" \
    --scan-class-path --fail-if-no-tests --disable-banner
