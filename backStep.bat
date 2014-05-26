@ECHO off
CLS
:start
ECHO.
ECHO    ================== WARNING ==================
ECHO    Don't forget to commit first the modifications you want to keep.
ECHO    It will reset all changes and remove new files that are not under version control
ECHO.
SET /p choice=Back on to the Step 4 (y/n)? 
IF /I '%choice%'=='y' GOTO next
IF /I '%choice%'=='n' GOTO end
ECHO "%choice%" is not valid, please reply y(es) or n(o)
ECHO.
GOTO start

:next
ECHO.
ECHO === Reset and Clean your working directory
git reset --hard
git clean -fd
ECHO.
ECHO === Checkout Step 4
git checkout Step4
ECHO.
GOTO end

:end

