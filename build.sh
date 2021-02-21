#!/bin/bash -xe

#SBT
ORG="com.bilal-fazlani"
PLUGIN_NAME="myplugin"
SCALA_VERSION="3.0.0-M3"
PLUGIN_VERSION="0.1.0-SNAPSHOT"
MAIN_CLASS="com.bilalfazlani.myplugin.RuntimePlugin"

#PLUGIN
REFRESH="10m"
EXTENSION="bin"
# PLUGIN_FILENAME="$PLUGIN_NAME.$REFRESH.$EXTENSION"
PLUGIN_FILENAME="$PLUGIN_NAME.$REFRESH.$EXTENSION"
PLUGINS_DIR="$HOME/projects/bitbar-plugins"
PLUGIN_PATH="$PLUGINS_DIR"/"$PLUGIN_FILENAME"
META_PATH="myplugin/src/main/resources/plugin-config.txt"

#OTHERS
export COURSIER_REPOSITORIES="ivy2Local|central|sonatype:releases|sonatype:snapshots|jitpack"

# ----------- PUBLISHING PLUGIN -------------------------
rm -rf ~/.ivy2/local/"$ORG"/"$PLUGIN_NAME"_"$SCALA_VERSION"/
#PUBLISH_PLUGIN="true" sbt ";clean ;publishLocal"
PUBLISH_PLUGIN="true" sbt "publishLocal"
coursier bootstrap -f -o "$PLUGIN_PATH" -M "$MAIN_CLASS" --standalone "$ORG":"$PLUGIN_NAME"_"$SCALA_VERSION":"$PLUGIN_VERSION"
chmod +x "$PLUGIN_PATH"
# ----------- PUBLISHING PLUGIN END ---------------------


# ----------- PUBLISHING METADATA -----------------------
xattr -w "com.ameba.SwiftBar" $(cat "$META_PATH" | base64) "$PLUGIN_PATH"
# ----------- PUBLISHING METADATA END -------------------