#!/usr/bin/env bash
# get base dir regardless of execution location
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
	DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
	SOURCE="$(readlink "$SOURCE")"
	[[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
. $(dirname $SOURCE)/init.sh

if [[ "$1" == up* ]]; then
	(
		cd "$basedir/Paper/"
		git fetch && git reset --hard origin/master
		cd ../
		git add Paper
	)
fi

paperVer=$(gethead Paper)
cd "$basedir/Paper/"

./paper patch

cd "Paper-Server"
mcVer=$(mvn -o org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=minecraft_version | sed -n -e '/^\[.*\]/ !{ /^[0-9]/ { p; q } }')

basedir
. $basedir/scripts/importmcdev.sh

minecraftversion=$(cat $basedir/Paper/work/BuildData/info.json | grep minecraftVersion | cut -d '"' -f 4)
version=$(echo -e "Paper: $paperVer\nmc-dev:$importedmcdev")
tag="${minecraftversion}-${mcVer}-$(echo -e $version | shasum | awk '{print $1}')"
echo "$tag" > $basedir/current-paper

$basedir/scripts/generatesources.sh

cd Paper/

function tag {
(
	cd $1
	if [ "$2" == "1" ]; then
		git tag -d "$tag" 2>/dev/null
	fi
	echo -e "$(date)\n\n$version" | git tag -a "$tag" -F - 2>/dev/null
)
}
echo "Tagging as $tag"
echo -e "$version"

forcetag=0
if [ "$(cat $basedir/current-paper)" != "$tag" ]; then
	forcetag=1
fi

tag Paper-API $forcetag
tag Paper-Server $forcetag

pushRepo Paper-API $PAPER_API_REPO $tag
pushRepo Paper-Server $PAPER_SERVER_REPO $tag

