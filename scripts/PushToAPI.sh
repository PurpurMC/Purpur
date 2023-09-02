sha256() {
  sha256sum $1 | awk '{print $1}'
}

prop() {
  grep "${1}" gradle.properties | cut -d'=' -f2 | sed 's/\r//'
}
echo "$tag"
project_id="nanaica"
project_name="nanaica"
mcversion=$(prop mcVersion)
ctime=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
pre=$(prop preVersion)
if [ $pre = "true" ]; then
  channel="experimental"
else
  channel="default"
fi
promoted=false

number=$(git log --oneline master ^`git describe --tags --abbrev=0` | wc -l)
changes=$(git log --pretty='%H<<<%s>>>' -"$number" | sed ':a;N;$!ba;s/\n//g')
jar_name="leaves-$mcversion.jar"
jar_sha256=`sha256 $jar_name`

# v2
curl --location --request POST "https://api.nanaicamc.tk/v2/commit/build" --header "Content-Type: application/json" --header "Authentication: Bearer $secret_v2" --data-raw "{\"project_id\":\"$project_id\",\"version\":\"$mcversion\",\"channel\":\"$channel\",\"changes\":\"$changes\",\"jar_name\":\"$jar_name\",\"sha256\":\"$jar_sha256\",\"tag\":\"$tag\"}"
