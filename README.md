# Spring Boot + jQuery + Thymeleaf SSR Demo

## 프로젝트 개요

Spring Boot를 사용한 서버 사이드 렌더링(SSR) 데모 프로젝트입니다.
Thymeleaf 템플릿 엔진과 jQuery를 활용한 사용자 관리 애플리케이션입니다.

## 기술 스택

### Backend
- **Spring Boot 3.2.0**
- **Java 17**
- **Thymeleaf** (서버 사이드 템플릿 엔진)
- **Gradle 8.5**

### Frontend
- **jQuery 3.6.0** (DOM 조작 및 AJAX)
- **HTML5/CSS3**

## 실행 방법

### 1. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 2. 브라우저 접속
```
http://localhost:8080
```

## API 엔드포인트

- `GET /` - 홈 페이지
- `GET /jquery` - jQuery 데모 페이지
- `GET /api/users` - 사용자 목록
- `POST /api/users` - 사용자 추가
- `GET /api/stats` - 통계

## 주요 기능

- 서버 사이드 렌더링 (SSR)
- jQuery 상태 관리 (Global State Pattern)
- 사용자 필터링 (전체/성인만/최신 3명)
- 실시간 상태 디버거
