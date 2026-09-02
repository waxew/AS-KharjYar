#!/usr/bin/env bash
set -euo pipefail

PACKAGE="com.asteam.kharjyar.debug"
PREVIOUS_APK="${RUNNER_TEMP}/previous-debug.apk"
CURRENT_APK="${RUNNER_TEMP}/current-debug.apk"

# Install and launch the known-good previous build first.
adb install "$PREVIOUS_APK"
adb shell monkey -p "$PACKAGE" -c android.intent.category.LAUNCHER 1
sleep 5

# Place a marker in private app storage. A normal Android update must preserve this data.
adb shell run-as "$PACKAGE" touch files/upgrade-sentinel
adb shell run-as "$PACKAGE" ls files/upgrade-sentinel >/dev/null

# Upgrade in place with the current build signed by the same temporary QA key.
adb install -r "$CURRENT_APK"
adb shell run-as "$PACKAGE" ls files/upgrade-sentinel >/dev/null

# Relaunch after upgrade and require a running process with no crash entry for this package.
adb logcat -c
adb shell am force-stop "$PACKAGE"
adb shell monkey -p "$PACKAGE" -c android.intent.category.LAUNCHER 1
sleep 8

PID="$(adb shell pidof -s "$PACKAGE" | tr -d '\r')"
test -n "$PID"

if adb logcat -d -b crash | grep -F "$PACKAGE"; then
  echo "Crash detected after upgrade" >&2
  exit 1
fi

echo "Upgrade persistence and launch smoke test passed."
