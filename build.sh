cd ./redis
./gradlew clean build
docker build --tag juyeonoh00/redis -f Dockerfile .
docker push juyeonoh00/redis
docker run --name redis -d -p 6382:6379 juyeonoh00/redis
cd ..

cd ./mysql
./gradlew clean build
docker build --tag juyeonoh00/mysql -f Dockerfile .
docker push juyeonoh00/mysql
docker run --name mysql -d -p 3306:3306 juyeonoh00/mysql
cd ..