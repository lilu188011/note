cls 
@ECHO OFF 
SET CLEAR_PATH=E: 
SET CLEAR_DIR=C:\Users\Administrator\.m2\repository\
color 0a 
TITLE ClearLastUpdated For Windows 
GOTO MENU 
:MENU 
CLS
ECHO. 
ECHO. * * * *  ClearLastUpdated For Windows  * * * * 
ECHO. * * 
ECHO. * 1 清理*.lastUpdated * 
ECHO. * * 
ECHO. * 2 查看*.lastUpdated * 
ECHO. * * 
ECHO. * 3 退 出 * 
ECHO. * * 
ECHO. * * * * * * * * * * * * * * * * * * * * * * * * 
ECHO. 
ECHO.请输入选择项目的序号： 
set /p ID= 
IF "%id%"=="1" GOTO cmd1 
IF "%id%"=="2" GOTO cmd2 
IF "%id%"=="3" EXIT 
PAUSE 
:cmd1 
ECHO. 开始清理
%CLEAR_PATH%
cd %CLEAR_DIR%
for /r %%i in (*.lastUpdated) do del %%i
ECHO.OK 
PAUSE 
GOTO MENU 
:cmd2 
ECHO. 查看*.lastUpdated文件
%CLEAR_PATH%
cd %CLEAR_DIR%
for /r %%i in (*.lastUpdated) do echo %%i
ECHO.OK 
PAUSE 
GOTO MENU 