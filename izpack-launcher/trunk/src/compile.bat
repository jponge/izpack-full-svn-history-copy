@echo off
color F0


if not defined WXWIN goto wxwin_not_defined


make -f Makefile.mingw install

goto end



:wxwin_not_defined

echo WXWIN environment variable was not defined.
echo these is required to compile.
echo enter SET WXWIN=C:\Path\To\WXWIN_ROOT or
echo set the variable in the control-panel

pause 

:end