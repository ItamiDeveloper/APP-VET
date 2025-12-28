@echo off
echo ================================================
echo EJECUTAR SCRIPT SQL EN MYSQL
echo ================================================
echo.
echo Este script ejecutará SETUP-DATABASE-COMPLETO.sql
echo en tu base de datos MySQL.
echo.
echo IMPORTANTE:
echo - MySQL debe estar corriendo (XAMPP, MySQL Workbench, etc.)
echo - Usuario por defecto: root
echo - Password: (vacío por defecto)
echo.
pause

set MYSQL_PATH=C:\xampp\mysql\bin\mysql.exe
set SQL_FILE=SETUP-DATABASE-COMPLETO.sql

if not exist "%MYSQL_PATH%" (
    echo ERROR: No se encontró MySQL en %MYSQL_PATH%
    echo Por favor, edita este archivo y ajusta la ruta de MySQL
    pause
    exit /b 1
)

echo.
echo Ejecutando script SQL...
echo.

"%MYSQL_PATH%" -u root < "%SQL_FILE%"

if %ERRORLEVEL% equ 0 (
    echo.
    echo ================================================
    echo ✅ SCRIPT EJECUTADO EXITOSAMENTE
    echo ================================================
    echo.
    echo La base de datos veterinaria_saas ha sido creada
    echo con todos los datos de prueba.
    echo.
    echo Próximos pasos:
    echo 1. Reinicia el backend si está corriendo
    echo 2. Inicia sesión con: admin / admin123
    echo.
) else (
    echo.
    echo ================================================
    echo ❌ ERROR AL EJECUTAR EL SCRIPT
    echo ================================================
    echo.
    echo Verifica:
    echo - Que MySQL esté corriendo
    echo - Que el usuario root tenga acceso
    echo - Que no haya errores de sintaxis en el SQL
    echo.
)

pause
