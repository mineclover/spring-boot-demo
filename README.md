# Spring Boot + jQuery + Thymeleaf SSR Demo

## 프로젝트 개요

Spring Boot를 사용한 서버 사이드 렌더링(SSR) 데모 프로젝트입니다.
Thymeleaf 템플릿 엔진과 jQuery를 활용한 사용자 관리 및 세션 관리 애플리케이션입니다.

## 기술 스택

### Backend
- **Spring Boot 3.2.0**
- **Java 17**
- **Thymeleaf** (서버 사이드 템플릿 엔진)
- **Gradle 8.5**

### Frontend
- **jQuery 3.6.0** (DOM 조작 및 AJAX)
- **HTML5/CSS3**
- **Thymeleaf Fragments** (재사용 가능한 UI 컴포넌트)

### Deployment
- **Docker** (컨테이너화)
- **Docker Compose** (오케스트레이션)

### Code Quality
- **Checkstyle** (Java 코드 스타일 검사)
- **PMD** (Java 정적 분석)
- **SpotBugs** (버그 패턴 검색)
- **ESLint** (JavaScript 린팅)
- **JaCoCo** (테스트 커버리지)
- **Husky** (Git pre-commit hooks)
- **GitHub Actions** (CI/CD 파이프라인)

## 실행 방법

### 로컬 실행

#### 1. Gradle로 실행
```bash
./gradlew bootRun
```

#### 2. JAR 빌드 후 실행
```bash
./gradlew bootJar
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### Docker 실행 (추천)

#### 1. Docker Compose 사용 (가장 간단)
```bash
# 빌드 및 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 중지
docker-compose down
```

#### 2. Docker 직접 사용
```bash
# 이미지 빌드
docker build -t spring-boot-demo .

# 컨테이너 실행
docker run -d -p 8080:8080 --name spring-boot-demo spring-boot-demo

# 로그 확인
docker logs -f spring-boot-demo

# 중지 및 삭제
docker stop spring-boot-demo
docker rm spring-boot-demo
```

### 브라우저 접속
```
http://localhost:8080
```

## 페이지 구성

- `GET /` - 홈 페이지 (엔드포인트 인덱스)
- `GET /jquery` - jQuery 상태 관리 데모
- `GET /session` - 세션 관리 데모 (로그인/게시판)
- `GET /components` - UI 컴포넌트 라이브러리

## API 엔드포인트

### 공개 API
- `GET /api/hello` - Hello World API
- `GET /api/users` - 사용자 목록
- `POST /api/users` - 사용자 추가
- `GET /api/stats` - 통계

### 세션 API
- `GET /session/users` - 사용자 목록 조회
- `POST /session/login` - 로그인 (세션 생성)
- `POST /session/logout` - 로그아웃 (세션 삭제)
- `GET /session/current` - 현재 로그인 사용자
- `GET /session/my-info` - 개인 정보 (로그인 필요)
- `GET /session/posts` - 게시글 목록
- `POST /session/posts` - 게시글 작성 (로그인 필요)
- `DELETE /session/posts/{id}` - 게시글 삭제 (본인/관리자)
- `POST /session/posts/restore-demo` - 데모 게시글 복원 (관리자)

## 주요 기능

### 1. jQuery 상태 관리
- Global State Pattern 구현
- 사용자 필터링 (전체/성인만/최신 3명)
- 실시간 상태 디버거
- AJAX 통신

### 2. 세션 관리
- 사용자 간 세션 전환
- 로그인/로그아웃 기능
- 세션 기반 인증
- 권한별 접근 제어 (USER/ADMIN)

### 3. 게시판 기능
- 게시글 작성/삭제 (로그인 필요)
- 본인 게시글만 삭제 가능
- 관리자는 모든 게시글 삭제 가능
- Toast 알림 시스템

## 품질 관리

### 코드 품질 도구 실행

#### 모든 품질 검사 실행
```bash
./gradlew qualityCheck
```

#### 개별 도구 실행
```bash
# Java 코드 스타일 검사
./gradlew checkstyleMain checkstyleTest

# PMD 정적 분석
./gradlew pmdMain pmdTest

# SpotBugs 버그 검색
./gradlew spotbugsMain

# 테스트 및 커버리지
./gradlew test jacocoTestReport

# JavaScript 린팅
npm run lint
npm run lint:fix  # 자동 수정
```

#### 리포트 확인
- **Checkstyle**: `build/reports/checkstyle/main.html`
- **PMD**: `build/reports/pmd/main.html`
- **SpotBugs**: `build/reports/spotbugs/main.html`
- **JaCoCo**: `build/reports/jacoco/test/html/index.html`
- **Test**: `build/reports/tests/test/index.html`

### Pre-commit Hooks

Git commit 전에 자동으로 코드 품질 검사가 실행됩니다.

```bash
# Husky 및 의존성 설치
npm install

# pre-commit hook 설치 (자동)
npm run prepare
```

커밋 시 다음이 자동 실행됩니다:
- JavaScript: ESLint (자동 수정)
- Java: Checkstyle, PMD

### CI/CD

GitHub Actions를 통한 자동화된 품질 검사:
- **Push/PR 시 자동 실행**
- 코드 품질 검사 (Checkstyle, PMD, SpotBugs, ESLint)
- 테스트 실행 및 커버리지 측정
- Docker 이미지 빌드 (main 브랜치)

워크플로우 파일: `.github/workflows/ci.yml`

### 4. UI 컴포넌트 라이브러리
- **Card** - 정보 카드
- **Button** - 버튼 (primary, secondary, success, danger)
- **Alert** - 알림 메시지 (success, error, info, warning)
- **Badge** - 배지/태그
- **Spinner** - 로딩 스피너 (small, medium, large)
- **Input Field** - 폼 입력 필드
- **User Card** - 사용자 프로필 카드

### 5. Thymeleaf Fragment 시스템
재사용 가능한 컴포넌트 기반 UI 구축
```html
<div th:replace="~{fragments/components :: card(
    'Title', 'Description', '🎯', '/link', 'Button Text'
)}"></div>
```

## Docker 이미지 특징

- **Multi-stage build**: 최종 이미지 크기 최소화
- **보안**: non-root 사용자로 실행
- **헬스체크**: 자동 상태 모니터링 포함
- **최적화**: Gradle 의존성 캐싱으로 빌드 속도 향상

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/         # 컨트롤러
│   │   │   ├── HelloController.java
│   │   │   ├── DataController.java
│   │   │   ├── PageController.java
│   │   │   ├── ComponentController.java
│   │   │   └── SessionController.java
│   │   ├── service/            # 서비스 레이어
│   │   ├── repository/         # 레포지토리
│   │   ├── model/              # 엔티티 모델
│   │   └── dto/                # DTO
│   └── resources/
│       ├── templates/          # Thymeleaf 템플릿
│       │   ├── fragments/      # 재사용 컴포넌트
│       │   ├── index.html
│       │   ├── jquery.html
│       │   ├── session.html
│       │   └── components-demo.html
│       └── static/             # 정적 리소스
│           ├── css/
│           └── js/
```

## 개발 환경

- Java 17 이상 필요
- Gradle 8.5 이상
- Docker (선택사항, 배포용)

## 라이센스

MIT License