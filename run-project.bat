@echo off
title AssistAI - Intelligent Job Application Assistant
echo ========================================================
echo       AI-Powered Intelligent Job Application Assistant
echo ========================================================
echo.

echo Choose your database option:
echo [1] Run MySQL database via Docker Compose (Recommended - Zero Setup)
echo [2] Use a locally installed MySQL instance
echo.
set DB_CHOICE=1
set /p DB_CHOICE="Enter choice [1 or 2, default is 1]: "

if "%DB_CHOICE%"=="2" (
    set /p DB_PASS="Enter your local MySQL Root Password: "
    set MYSQL_PASSWORD=%DB_PASS%
) else (
    echo.
    echo Starting MySQL database via Docker Compose...
    docker compose up -d
    set MYSQL_PASSWORD=1234
    echo Database container started successfully!
)

echo.
echo [1/3] Launching Spring Boot Backend...
start cmd /k "title AssistAI Backend (Port 8080) && cd backend && mvn clean spring-boot:run"

echo [2/3] Launching React Dev Server (Vite)...
start cmd /k "title AssistAI Frontend (Port 5173) && cd frontend && npm run dev"

echo [3/3] Waiting for servers to spin up (5s)...
timeout /t 5 >nul

echo Opening browser at http://localhost:5173 ...
start http://localhost:5173

echo.
echo ========================================================
echo System launched! Keep terminal windows open.
echo Close individual terminal sessions to terminate.
echo ========================================================
echo.
pause
