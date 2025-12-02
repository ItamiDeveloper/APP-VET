@echo off
echo ================================================
echo PROBANDO API REST VETERINARIA
echo ================================================
echo.

echo 1. Esperando que la aplicacion este lista...
timeout /t 3 /nobreak > nul

echo.
echo 2. Probando LOGIN con usuario admin...
echo.
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
echo.
echo.

echo 3. Probando LOGIN con usuario veterinario...
echo.
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"veterinario\",\"password\":\"vet123\"}"
echo.
echo.

echo 4. Obteniendo lista de planes (sin token)...
echo.
curl -X GET http://localhost:8080/api/planes
echo.
echo.

echo ================================================
echo Copia el TOKEN del login y pegalo en la siguiente variable:
echo set TOKEN=tu_token_aqui
echo Luego ejecuta: curl -X GET http://localhost:8080/api/planes -H "Authorization: Bearer %TOKEN%"
echo ================================================
pause
