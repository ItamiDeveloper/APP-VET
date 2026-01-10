@echo off
echo ============================================
echo EJECUTANDO SCRIPT DE INICIALIZACION
echo ============================================
echo.

REM Ejecutar el script SQL
mysql -u root -p -h 127.0.0.1 veterinaria_saas < "C:\Users\Itami\APP-VET\INICIALIZAR-DATOS-BASICOS.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo SCRIPT EJECUTADO EXITOSAMENTE
    echo ============================================
    echo.
    echo Se han creado:
    echo   - 4 Roles (SUPER_ADMIN, ADMIN, VETERINARIO, RECEPCIONISTA^)
    echo   - 4 Planes (GRATIS, BASICO, PROFESIONAL, EMPRESARIAL^)
    echo   - 5 Categorias de productos
    echo.
    echo Ahora puedes registrar veterinarias desde el frontend
    echo.
) else (
    echo.
    echo ============================================
    echo ERROR AL EJECUTAR EL SCRIPT
    echo ============================================
    echo.
    echo Por favor verifica:
    echo   1. MySQL esta instalado y en el PATH
    echo   2. La contrasena de root es correcta
    echo   3. La base de datos veterinaria_saas existe
    echo.
)

pause
