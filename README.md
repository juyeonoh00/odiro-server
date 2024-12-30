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

<details><summary><strong>API 명세서</strong></summary>

스웨거 링크 추가 예정
</details>


## 주요 기능
1. **사용자 인증 및 권한 관리**
   - JWT 기반의 인증 토큰 관리
2. **데이터 CRUD 기능**
   - MySQL을 통한 데이터의 생성, 조회, 수정, 삭제
3. **고성능 데이터 처리**
   - Redis를 활용한 캐싱 및 실시간 데이터 관리

## 기술적 의사 결정
1. **Spring Boot**를 선택한 이유
   - 높은 생산성과 방대한 커뮤니티 지원
2. **Redis** 도입 이유
   - 빠른 데이터 접근 속도 제공 및 서버 부하 감소
3. **Docker Compose** 사용 이유
   - 로컬 개발 및 배포 환경의 일관성 유지

## 트러블 슈팅 및 성능 개선
1. **데이터베이스 연결 문제**
   - MySQL 연결 이슈 발생 시 `docker-compose.yml`의 설정을 점검하고, 네트워크 상태를 최적화하여 문제 해결
2. **캐싱 불일치 문제**
   - Redis와 MySQL 간 데이터 일관성을 보장하기 위한 TTL(Time To Live) 설정 및 동기화 메커니즘 적용
3. **성능 최적화**
   - API 호출 빈도가 높은 엔드포인트에 대한 쿼리 최적화 및 인덱스 추가로 응답 시간 단축
