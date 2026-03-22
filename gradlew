#!/usr/bin/env sh
set -eu

DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
PROPS_FILE="$DIR/gradle/wrapper/gradle-wrapper.properties"
CACHE_DIR="$DIR/.gradle-dist"

if [ ! -f "$PROPS_FILE" ]; then
  echo "gradle-wrapper.properties not found: $PROPS_FILE"
  exit 1
fi

DIST_URL=$(grep '^distributionUrl=' "$PROPS_FILE" | cut -d= -f2- | sed 's#\\:#:#g')
if [ -z "$DIST_URL" ]; then
  echo "distributionUrl is empty in $PROPS_FILE"
  exit 1
fi

ZIP_NAME=$(basename "$DIST_URL")
BASE_NAME=${ZIP_NAME%.zip}
DIST_DIR="$CACHE_DIR/$BASE_NAME"
ZIP_PATH="$CACHE_DIR/$ZIP_NAME"

mkdir -p "$CACHE_DIR"

if [ ! -d "$DIST_DIR" ]; then
  if [ ! -f "$ZIP_PATH" ]; then
    echo "Downloading Gradle distribution: $DIST_URL"
    if command -v curl >/dev/null 2>&1; then
      curl -fL "$DIST_URL" -o "$ZIP_PATH"
    elif command -v wget >/dev/null 2>&1; then
      wget -O "$ZIP_PATH" "$DIST_URL"
    else
      echo "curl or wget is required to download Gradle"
      exit 1
    fi
  fi

  TMP_DIR="$CACHE_DIR/_extract"
  rm -rf "$TMP_DIR"
  mkdir -p "$TMP_DIR"

  if command -v unzip >/dev/null 2>&1; then
    unzip -q "$ZIP_PATH" -d "$TMP_DIR"
  else
    echo "unzip is required to extract Gradle"
    exit 1
  fi

  FIRST_DIR=$(find "$TMP_DIR" -mindepth 1 -maxdepth 1 -type d | head -n 1)
  if [ -z "${FIRST_DIR:-}" ]; then
    echo "Failed to extract Gradle distribution"
    exit 1
  fi
  mv "$FIRST_DIR" "$DIST_DIR"
  rm -rf "$TMP_DIR"
fi

exec "$DIST_DIR/bin/gradle" "$@"
