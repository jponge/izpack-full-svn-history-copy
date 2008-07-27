#!/bin/sh
#
# This patch can be used when TestLangPacks.jar is installed under
# IzPack.
# If you use this patch "$1" has to be your language file to be
# tested _always_!
#
java -jar TestLangPacks.jar $1 $2 $3
