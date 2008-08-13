@echo off
REM This patch can be used when language files are located in the same
REM directory as TestLangPacks.jar.
REM You need following files: eng.xml and your language xml file.

java -jar TestLangPacks.jar %1 %2 %3
