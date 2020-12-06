#!/bin/bash -xe

REFRESH="5s"
NAME="myapp"
EXTENSION="sh"
FILENAME="$NAME.$REFRESH.$EXTENSION"


rm -rf /Users/bilal/.ivy2/local/tech.bilal/bitbar4s_3.0.0-M2/
sbt ";clean ;publishLocal"
coursier bootstrap -f -o ~/projects/bitbar-plugins/"$FILENAME" --standalone tech.bilal:bitbar4s_3.0.0-M2:0.1.0-SNAPSHOT
chmod +x ~/projects/bitbar-plugins/"$FILENAME"
