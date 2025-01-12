# 여행 플랜 소셜 플랫폼

[2024.03.01-2025.01.01]

## 목차

- [OverView](#overview)
- [기술스택 및 아키텍처](#기술-스택-및-아키텍처)
    - [기술스택](#기술-스택)
    - [아키텍처](#아키텍처)
- [명세서](#명세서)
- [주요 기능](#주요-기능)
- [기술적 의사 결정](#기술적-의사-결정)
- [트러블 슈팅 및 성능 개선](#트러블-슈팅-및-성능-개선)

## OverView
- 여행 메이트들과 함께 플래너를 작성하고, 이를 카테고리화하여 공유할 수 있는 소셜 플랫폼
- 친구 추가 및 채팅 기능을 통해 다양한 유저와 교류하고 소통할 수 있도록 지원

## 기술 스택 및 아키텍처

### 기술 스택

**Language & Library** &nbsp; : &nbsp;
<img src="https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java 17">
<img src="https://img.shields.io/badge/SpringBoot-3.3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.3.2">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data JPA">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white" alt="Spring Security">
<img src="https://img.shields.io/badge/OAuth2.0-4285F4?style=for-the-badge&logo=oauth&logoColor=white" alt="OAuth 2.0">
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" alt="JWT">

**Database & Caching** &nbsp; : &nbsp;
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">

**etc** &nbsp; : &nbsp;
<img src="https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=WebRTC&logoColor=white" alt="WebSocket">
<img src="https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white" alt="Apache Kafka">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle">
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white" alt="Git">

### 아키텍처

<img src="img\odiro_architecture.png" alt="Odiro Architecture" width="600">

## 명세서

<details><summary><strong>ERD</strong></summary>

<img src="img\odiro_erd.png" alt="Odiro ERD" width="600">
</details>


## 주요 기능
1. **로그인 서비스**
   - Spring Cloud Gateway와 Spring WebFlux Security를 활용한 인가 관리 구현
   - OAuth 2.0 프로토콜을 활용한 소셜 로그인 기능 구현 및 보안 강화
   - JWT 토큰 기반으로 Redis에 Refresh Token을 저장하고, 암호화된 Access Token만을 클라이언트에 전달하여 토큰 탈취 리스크 최소화 
2. **Plan 서비스**
   - 장소 및 찜한 장소를 등록, 조회, 삭제를 가능하게 하고, 정식 방문지는 방문 순서를 부여하여 일정 관리
   - 대량의 댓글 데이터를 효율적으로 처리하기 위한 페이지네이션 적용
3. **소셜 서비스**
   - 친구 추가 및 채팅 기능을 구현하여 새로운 유저들과의 교류를 지향
4. **여행 추천 서비스**
   - 축제 정보 API를 활용하여 전국의 다양한 축제 정보를 상세 페이지에서 확인 가능
   - Kakao map API를 활용하여 장소 등록 시 대표 이미지를 크롤링
     
## 기술적 의사 결정
1. **Stomp 사용**
   - Pub/Sub 모델을 통해 클라이언트 상태를 직접 관리하지 않아도 되며, 세션 정보 저장으로 인한 서버 부담을 최소화
   - 트래픽 증가 시 메시지 브로커(RabbitMQ, ActiveMQ 등)와 통합할 수 있도록 구조를 설계
2. **S3 사용**
   - 프로필 사진은 유저의 개인정보임으로 안정성을 중시, planner의 특성상 타 국가에서 원활한 사용이 가능해야하므로 글로벌 서비스에 안정적인 S3를 채택

## 트러블 슈팅 및 성능 개선
1. **Redis를 활용한 데이터 조회 최적화**
    - **문제 상황**:
        - 기존 RDB 테이블의 일부 스키마 데이터를 조회할 때, 복잡한 쿼리와 대량의 데이터로 인해 응답 속도가 느려지는 문제가 발생.
    - **해결 방안**:
        - Redis를 읽기 전용 테이블로 활용하여, 데이터를 Key-Value 구조로 저장
        - Key 패턴 기반 검색을 통해 빠르게 데이터를 조회하고, Redis 데이터를 활용해 필요한 RDB 데이터를 효율적으로 필터링
    - **구현**:
        - Spring RedisTemplate을 사용하여 Redis에서 패턴 기반 키 검색 및 데이터를 조회.
        - 조회된 데이터를 PlanRepository와 결합해 필요한 정보만 RDB에서 가져오도록 설계.
        - 필터 조건과 랜덤 샘플링 로직을 추가하여 특정 조건에 맞는 데이터를 반환.
    - **성과**:
       - mysq 사용 시 (읽기 db 사용 x)
     
         <img src="img\mysql_500_100.png" alt="Mysql 실행 결과" width="400">
     
       - redis 사용 시
     
         <img src="img\redis_500_100.png" alt="Redis 실행 결과" width="400">
     
         → Redis를 활용한 조회로 서버 부하를 최소화하고 응답 성능 최적화하여 처리량 **41.73%%** 상승

3. **Docker를 통한 AWS 배포**
   - docker-compose 사용 시 스케일 업 할 경우 모든 서비스가 복사 되어 확장 되는 문제 확인
   - docker compose를 사용하지 않고, 각각의 aws 환경에서 앱, mysql, redis 각각의 도커 파일을 띄우는 방식으로 활용하여 한번에 각각 스케일업 할 수 있도록 확장성 보장

