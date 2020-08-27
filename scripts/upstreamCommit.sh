#!/usr/bin/env bash
(
  set -e
  PS1="$"

  function changelog() {
    base=$(git ls-tree HEAD "$1" | cut -d' ' -f3 | cut -f1)
    cd "$1" && git log --oneline "${base}"...HEAD
  }
  paper=$(changelog Paper)

  updated=""
  logsuffix=""
  if [ -n "$paper" ]; then
    logsuffix="$logsuffix\n\nPaper Changes:\n$paper"
    if [ -z "$updated" ]; then updated="Paper"; else updated="$updated/Paper"; fi
  fi
  disclaimer="Upstream has released updates that appear to apply and compile correctly"

  if [ -n "$1" ]; then
    disclaimer=("$@")
  fi

  log="${UP_LOG_PREFIX}Updated Upstream ($updated)\n\n${disclaimer[*]}${logsuffix}"

  echo -e "$log" | git commit -F -

) || exit 1
