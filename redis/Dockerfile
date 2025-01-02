##FROM --platform=linux/amd64 openjdk:17
#FROM redis:latest
#
#
#COPY redis/ .
#
## Redis를 사용자 정의 설정 파일로 실행
#
##CMD ["java", "-jar", "-Dserver.address=0.0.0.0", "/app/app.jar"]
#CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]

# 사용할 베이스 이미지를 설정합니다.
FROM --platform=linux/amd64 redis

# Redis 서버가 리스닝할 포트를 설정합니다.
EXPOSE 6379

# Docker 컨테이너가 시작될 때 실행할 명령어를 설정합니다.
CMD ["redis-server"]