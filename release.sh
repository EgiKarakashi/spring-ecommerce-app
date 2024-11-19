#!/bin/bash

# Ensure version numbers are provided as arguments
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: ./release.sh <release_version> <next_snapshot_version>"
  exit 1
fi

RELEASE_VERSION=$1
NEXT_SNAPSHOT_VERSION=$2

# Step 1: Set the release version
echo "Setting release version to $RELEASE_VERSION..."
mvn versions:set-property -Dproperty=revision -DnewVersion=$RELEASE_VERSION
mvn versions:commit

# Step 2: Commit release version changes and tag
echo "Committing release version and tagging..."
git add pom.xml
git commit -m "Release version $RELEASE_VERSION"
git tag -a "v$RELEASE_VERSION" -m "Release version $RELEASE_VERSION"
git push origin main --tags

# Step 3: Set the next snapshot version
echo "Setting next snapshot version to $NEXT_SNAPSHOT_VERSION..."
mvn versions:set-property -Dproperty=revision -DnewVersion=$NEXT_SNAPSHOT_VERSION
mvn versions:commit

# Step 4: Commit next snapshot version changes
echo "Committing next snapshot version..."
git add pom.xml
git commit -m "Start development on version $NEXT_SNAPSHOT_VERSION"
git push origin main

echo "Release and versioning complete!"
