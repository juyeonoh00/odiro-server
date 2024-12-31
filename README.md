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

// 수정 해야됌
## OverView
- Odiro Server는 확장성과 성능 최적화를 고려한 백엔드 시스템으로, Redis와 MySQL을 활용한 데이터 관리 및 Docker를 통한 컨테이너화된 환경에서 운영됩니다.
- 이 프로젝트는 강력한 기술 스택과 신중한 설계를 통해 안정적인 서비스를 제공합니다.

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

<img src="img\ERD.png" alt="RedChart ERD" width="600">
</details>


## 주요 기능
1. **Plan 관리**
   - 장소 및 찜한 장소를 등록, 조회, 삭제를 가능하게 하고, 정식 방문지는 방문 순서를 부여하여 일정 관리
   - 대량의 댓글 데이터를 효율적으로 처리하기 위한 페이지네이션 적용
3. **소셜 기능**
   - 친구 추가 및 채팅 기능을 구현하여 새로운 유저들과의 교류를 지향
5. **여행 추천 서비스**
   - 축제 정보 API를 활용하여 전국의 다양한 축제 정보를 상세 페이지에서 확인 가능
   - Kakao map API를 활용하여 장소 등록 시 대표 이미지를 크롤링
  
   
## 기술적 의사 결정
1. **Stomp 사용**
   - Pub/Sub 모델을 통해 클라이언트 상태를 직접 관리하지 않아도 되며, 세션 정보 저장으로 인한 서버 부담을 최소화
   - 트래픽 증가 시 메시지 브로커(RabbitMQ, ActiveMQ 등)와 통합할 수 있도록 구조를 설계
2. **S3 사용**
   - 프로필 사진은 유저의 개인정보임으로 안정성을 중시, planner의 특성상 타 국가에서 원활한 사용이 가능해야하므로 글로벌 서비스에 안정적인 S3를 채택
3. **Docker를 통한 AWS 배포**
   - docker compose를 사용하지 않고, 각각의 aws 환경에서 앱, mysql, redis 각각의 도커 파일을 띄우는 방식으로 활용하여 한번에 각각 스케일업 할 수 있도록 확장성 보장

  

## 트러블 슈팅 및 성능 개선
1. **Redis를 활용한 데이터 조회 최적화**
   - MySQL 연결 이슈 발생 시 `docker-compose.yml`의 설정을 점검하고, 네트워크 상태를 최적화하여 문제 해결




