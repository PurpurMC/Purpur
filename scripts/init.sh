#!/usr/bin/env bash

sourceBase=$(dirname "$SOURCE")/../
cd "${basedir:-$sourceBase}" || exit

basedir=$(pwd -P)
cd - || exit

function bashColor() {
  if [ "$2" ]; then
    echo -e "\e[$1;$2m"
  else
    echo -e "\e[$1m"
  fi
}
function bashColorReset() {
  echo -e "\e[m"
}

function cleanupPatches() {
  cd "$1" || exit
  for patch in *.patch; do
    gitver=$(tail -n 2 "$patch" | grep -ve "^$" | tail -n 1)
    diffs=$(git diff --staged "$patch" | grep -E "^(\+|\-)" | grep -Ev "(From [a-z0-9]{32,}|\-\-\- a|\+\+\+ b|.index|Date\: )")

    testver=$(echo "$diffs" | tail -n 2 | grep -ve "^$" | tail -n 1 | grep "$gitver")
    if [ "x$testver" != "x" ]; then
      diffs=$(echo "$diffs" | tail -n +3)
    fi

    if [ "x$diffs" == "x" ]; then
      git reset HEAD "$patch" >/dev/null
      git checkout -- "$patch" >/dev/null
    fi
  done
}
function basedir() {
  cd "$basedir" || exit
}
function gethead() {
  (
    cd "$1" || exit
    git log -1 --oneline
  )
}
