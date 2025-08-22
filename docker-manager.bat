@echo off
echo ========================================
echo     LMS Films - Docker Setup
echo ========================================

echo.
echo Escolha uma opcao:
echo 1. Iniciar todos os servicos (MongoDB + App)
echo 2. Iniciar apenas MongoDB
echo 3. Parar todos os servicos
echo 4. Rebuild da aplicacao
echo 5. Ver logs da aplicacao
echo 6. Acessar MongoDB CLI
echo 0. Sair

set /p choice="Digite sua opcao (0-6): "

if "%choice%"=="1" goto start_all
if "%choice%"=="2" goto start_mongo
if "%choice%"=="3" goto stop_all
if "%choice%"=="4" goto rebuild
if "%choice%"=="5" goto logs
if "%choice%"=="6" goto mongo_cli
if "%choice%"=="0" goto exit
goto invalid

:start_all
echo Iniciando todos os servicos...
docker-compose up -d
echo.
echo Servicos iniciados!
echo MongoDB: http://localhost:27017
echo Mongo Express: http://localhost:8081
echo LMS Films API: http://localhost:8080
goto end

:start_mongo
echo Iniciando apenas MongoDB...
docker-compose up -d mongodb mongo-express
echo.
echo MongoDB iniciado!
echo MongoDB: http://localhost:27017
echo Mongo Express: http://localhost:8081
goto end

:stop_all
echo Parando todos os servicos...
docker-compose down
echo Servicos parados!
goto end

:rebuild
echo Reconstruindo a aplicacao...
docker-compose down
docker-compose build --no-cache lms-app
docker-compose up -d
echo Aplicacao reconstruida e iniciada!
goto end

:logs
echo Logs da aplicacao:
docker-compose logs -f lms-app
goto end

:mongo_cli
echo Conectando ao MongoDB CLI...
docker exec -it lms-mongodb mongosh -u admin -p admin123 --authenticationDatabase admin
goto end

:invalid
echo Opcao invalida!
goto end

:exit
echo Saindo...
goto end

:end
pause
