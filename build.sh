#!/bin/bash -xe

REFRESH="5s"
NAME="my-plugin"
ORG="tech.bilal"
SCALA_VERSION="2.13"
EXTENSION="sh"
FILENAME="$NAME.$REFRESH.$EXTENSION"
PLUGINS_DIR=~/projects/bitbar-plugins
VERSION=0.1.0-SNAPSHOT

rm -rf ~/.ivy2/local/"$ORG"/bitbar4s_"$SCALA_VERSION"/
sbt ";clean ;publishLocal"
coursier bootstrap -f -o "$PLUGINS_DIR"/"$FILENAME" --standalone "$ORG":bitbar4s_"$SCALA_VERSION":"$VERSION"
chmod +x "$PLUGINS_DIR"/"$FILENAME"
