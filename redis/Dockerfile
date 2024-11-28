#FROM --platform=linux/amd64 openjdk:17
FROM redis:latest


COPY redis/ .

# Redis를 사용자 정의 설정 파일로 실행

CMD ["java", "-jar", "-Dserver.address=0.0.0.0", "/app/app.jar"]