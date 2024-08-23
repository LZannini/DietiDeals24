@echo off
REM Setup script for building and running Docker containers

echo =============================
echo Building backend Docker image
echo =============================

REM Build the backend Docker image
docker build -t backend .
IF %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to build the backend image.
    pause
    exit /b %ERRORLEVEL%
)
echo Backend image built successfully.

echo =============================
echo Starting Docker containers
echo =============================

REM Run docker-compose to start containers
docker-compose up -d
IF %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to start Docker containers.
    pause
    exit /b %ERRORLEVEL%
)
echo Docker containers started successfully.

echo ===========================
echo Setup completed successfully
echo ===========================

REM Wait for the user to press a key before closing
pause
