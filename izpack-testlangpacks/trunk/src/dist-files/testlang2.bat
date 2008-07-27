@echo off
REM This patch can be used when TestLangPacks.jar is installed under
REM IzPack.
REM Example: C:\Program Files\IzPack\izpack-testlangpacks
REM If you use this patch "%1" has to be your language file to be
REM tested _always_!

java -jar TestLangPacks.jar ..\bin\langpacks\installer\%1 -b ..\bin\langpacks\installer\eng.xml %2 %3
