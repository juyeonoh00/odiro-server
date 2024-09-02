@REM #!/bin/sh
@REM ./gradlew build
@REM
@REM docker build --tag odiro -f Dockerfile .
@REM
@REM docker-compose -f docker-compose.yml up -d mysql-container2
@REM docker-compose -f docker-compose.yml logs -f mysql-container2
@REM sleep 10
@REM docker-compose -f docker-compose.yml up -d odiro-container2
@REM docker-compose -f docker-compose.yml logs -f odiro-cotainer2

@echo off
call gradlew build

docker build --tag juyeonoh00/odiro -f Dockerfile .
@REM docker push juyeonoh00/odiro

cd ./mysql
docker build --tag juyeonoh00/mysql -f Dockerfile .
@REM docker push juyeonoh00/mysql
cd ..

docker-compose down

docker-compose -f docker-compose.yml up -d mysql-container2
@echo off
echo Waiting for 10 seconds...
PowerShell -Command "Start-Sleep -Seconds 10"
echo Resuming execution.
docker-compose -f docker-compose.yml up -d odiro-container2















