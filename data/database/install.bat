@echo off

REM ############################################
REM ## You can change here your own DB params ##
REM ############################################
REM MYSQL BIN PATH
set mysqlBinPath=C:\Program Files\MySQL\MySQL Server 5.7\bin

REM SERVER
set user=root
set pass=2236
set db=l2hf
set host=localhost

REM ############################################

set mysqldumpPath="%mysqlBinPath%\mysqldump"
set mysqlPath="%mysqlBinPath%\mysql"

echo.----------------------------------
echo.Installation of High-Five database
echo.----------------------------------
echo.

echo.Installing database.
echo.
for %%i in (sql/*.sql) do (
echo install %%i
%mysqlPath% -h %host% -u %user% --password=%pass% -D %db% < sql/%%i
)

echo.
echo Installation complete.
echo.
pause
