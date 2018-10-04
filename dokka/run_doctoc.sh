#!/usr/bin/env bash

# need to source nvm environment for non-interactive environment
. ~/.nvm/nvm.sh

doctoc dokka/src/main/java/wiki/Components.kt --github --notitle
