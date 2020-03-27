#!/usr/bin/env bash
(
    set -e
    basedir="$(cd "$1" && pwd -P)"
    workdir="$basedir/Paper/work"
    mcver=$(< "$workdir/BuildData/info.json" grep minecraftVersion | cut -d '"' -f 4)
    purpurjar="$basedir/Purpur-Server/target/purpur-$mcver.jar"
    vanillajar="$workdir/Minecraft/$mcver/$mcver.jar"

    (
        cd "$basedir/Paperclip"
        mvn clean package "-Dmcver=$mcver" "-Dpaperjar=$purpurjar" "-Dvanillajar=$vanillajar"
    )
    cp "$basedir/Paperclip/assembly/target/paperclip-${mcver}.jar" "$basedir/purpurclip.jar"

    echo ""
    echo ""
    echo ""
    echo "Build success!"
    echo "Copied final jar to $(cd "$basedir" && pwd -P)/purpurclip.jar"
) || exit 1
