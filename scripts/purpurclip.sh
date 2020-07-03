#!/usr/bin/env bash

# Copied from https://github.com/PaperMC/Paper/blob/d54ce6c17fb7a35238d6b9f734d30a4289886773/scripts/paperclip.sh
# License from Paper applies to this file

(
  set -e
  basedir="$(cd "$1" && pwd -P)"
  workdir="$basedir/Paper/work"
  mcver=$(grep <"$workdir/BuildData/info.json" minecraftVersion | cut -d '"' -f 4)
  purpurjar="$basedir/Purpur-Server/target/purpur-$mcver.jar"
  vanillajar="$workdir/Minecraft/$mcver/$mcver.jar"

  (
    cd "$workdir/Paperclip"
    mvn clean package "-Dmcver=$mcver" "-Dpaperjar=$purpurjar" "-Dvanillajar=$vanillajar"
  )
  cp "$workdir/Paperclip/assembly/target/paperclip-${mcver}.jar" "$basedir/purpurclip.jar"

  echo ""
  echo ""
  echo ""
  echo "Build success!"
  echo "Copied final jar to $(cd "$basedir" && pwd -P)/purpurclip.jar"
) || exit 1
