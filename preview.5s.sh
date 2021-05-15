#!/bin/bash -e

PLUGIN_NAME=com.bilalfazlani.myplugin.ImagePlugin
START="running $PLUGIN_NAME"
END="Total time"

cd "$HOME"/projects/scala/swiftbar4s
sbtn --client "myplugin/runMain $PLUGIN_NAME $1 $2 $3" | grep -A1000 "$START" | grep -B1000 "$END" | grep -Ev "$START|$END"