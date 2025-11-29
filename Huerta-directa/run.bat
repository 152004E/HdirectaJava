@echo off
echo ====================================
echo     EJECUTANDO HUERTA DIRECTA
echo ====================================
echo.
echo Compilando proyecto...
call mvnw.cmd clean compile
if %errorlevel% neq 0 (
    echo ERROR: No se pudo compilar el proyecto
    pause
    exit /b %errorlevel%
)
echo.
echo Compilacion exitosa! Ejecutando aplicacion...
echo.
call mvnw.cmd spring-boot:run
pause

