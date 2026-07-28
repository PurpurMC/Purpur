#!/usr/bin/env bash

# requires curl & jq

# upstreamCommit --paper HASH --pufferfish HASH
# flag: --paper HASH - (Optional) the commit hash to use for comparing commits between paper (PaperMC/Paper/compare/HASH...HEAD)
# flag: --pufferfish HASH - the commit hash to use for comparing commits between pufferfish (pufferfish-gg/Pufferfish/compare/HASH...HEAD)

function getCommits() {
    curl -H "Accept: application/vnd.github.v3+json" https://api.github.com/repos/"$1"/compare/"$2"..."$3" | jq -r '.commits[] | "'"$1"'@\(.sha[:8]) \(.commit.message | split("\r\n")[0] | split("\n")[0])" | sub("\\[ci( |-)skip]"; "[ci/skip]")'
}

(
set -e
PS1="$"

paperHash=$(git diff gradle.properties | awk '/^-paperCommit =/{print $NF}')
pufferfishHash=""

TEMP=$(getopt --long paper:,pufferfish: -o "" -- "$@")
eval set -- "$TEMP"
while true; do
    case "$1" in
        --paper)
            paperHash="$2"
            shift 2
            ;;
        --pufferfish)
            pufferfishHash="$2"
            shift 2
            ;;
        *)
            break
            ;;
    esac
done

paper=""
pufferfish=""
updated=""
logsuffix=""

# Paper updates
if [ -n "$paperHash" ]; then
    newHash=$(git diff gradle.properties | awk '/^+paperCommit =/{print $NF}')
    paper=$(getCommits "PaperMC/Paper" "$paperHash" $(echo $newHash | grep . -q && echo $newHash || echo "HEAD"))

    # Updates found
    if [ -n "$paper" ]; then
        updated="Paper"
        logsuffix="$logsuffix\n\nPaper Changes:\n$paper"
    fi
fi

# Pufferfish updates
if [ -n "$pufferfishHash" ]; then
    pufferfish=$(getCommits "pufferfish-gg/Pufferfish" "$pufferfishHash" "HEAD")

    # Updates found
    if [ -n "$pufferfish" ]; then
        updated="Pufferfish"
        logsuffix="$logsuffix\n\nPufferfish Changes:\n$pufferfish"
    fi
fi

# Both have updates
if [ -n "$paper" ] && [ -n "$pufferfish" ]; then
    updated="Paper & Pufferfish"
fi

disclaimer="Upstream has released updates that appear to apply and compile correctly"
log="Updated Upstream ($updated)\n\n${disclaimer}${logsuffix}"

git add gradle.properties

echo -e "$log" | git commit -F -

) || exit 1
