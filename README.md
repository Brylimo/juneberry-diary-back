# juneberry diary (backend)
juneberry diary backend project 

## 프로젝트 정보
### 준베리다이어리(juneberry diary)
준베리다이어리는 캘린더, 블로그를 통해 일상과 기록을 공유하는 종합 다이어리 서비스입니다.

### 프로젝트 작업
- 프로젝트 작업 기간
  - 2023.08.15 ~ 현재
- 프로젝트 개발 투입 인원
  - 1명(나)

### 배포 주소
서비스에 접속하려면 [여기](https://juneberrydiary.com/)를 클릭하세요.

## 프로젝트 설명
준베리다이어리는 블로그/다이어리 기능과 캘린더 기록 기능, 지도 기록 기능이 모두 포함된 종합 다이어리 서비스입니다. 일상에서 경험하는 모든 종류의 기록을 준베리다이어리 서비스 하나만으로 해결할 수 있습니다. 또한 여러 종류의 서로 다른 기록을 서로 연동하고 이러한 기록을 통해 유저에게 새로운 인사이트를 제공하기 위해 노력합니다.

## 기술 스택
-  Spring Boot, Redis, PostgreSQL, Spring Security, Spring Data JPA, Querydsl, Docker, Grafana, Prometheus, Nginx, Jenkins, JUnit5, H2 Database, Swagger, Java
-  Jenkins 기반 CI/CD 파이프라인 구축(CI 테스트 포함)
-  Prometheus, Grafana를 이용한 모니터링 시스템 서버 구축
-  JUnit5 + H2 Database 기반 테스트 코드 작성

## 로컬 실행 방법
1. java 17버전 이상 설치 및 코드 클론
2. email.username(spring-boot-starter-mail 이메일 이름), email.password(spring-boot-starter-mail 이메일 패스워드) 환경 변수(Environment Variable) 값 설정
3. active profile을 dev로 설정한 후 실행(실제 서비스와 연결된 DB 사용), 로컬 DB와 연결할 경우 active profile을 local로 설정한 후 실행
4. 스프링부트 실행

포트: 8081

## 아키텍처
![](https://velog.velcdn.com/images/brylimo/post/b109be88-0269-4567-8553-aa0b4ce24760/image.jpg)

## ERD
![](https://velog.velcdn.com/images/brylimo/post/9da74794-1ce3-4a63-a1d5-7c5fa5fddf8d/image.jpg)

## 화면 구성
### 로그인 및 회원가입
![](https://cdn.juneberrydiary.com/5d3b1a67-51cb-42d2-ae2e-718d56f8968f.png)
![](https://cdn.juneberrydiary.com/dd54d50a-b597-45e2-953b-3d4c7ecbc756.jpg)
![](https://cdn.juneberrydiary.com/079656c3-66e3-4bba-a686-8d7f91d1464b.jpg)
![](https://cdn.juneberrydiary.com/70efe8b1-f5c4-4234-b2ec-fc7f91555b26.jpg)
![](https://cdn.juneberrydiary.com/4eca48f0-d397-4d88-8cdc-7b7e5afd9f54.jpg)
![](https://cdn.juneberrydiary.com/36af3b03-5249-4887-953c-73721ababaae.jpg)

### 지도
(※ 지도 관련 기능은 merci 프로젝트를 이용해주세요.)
![](https://cdn.juneberrydiary.com/ab37a0fc-175f-40c1-81c0-3877f829a333.png)
![](https://cdn.juneberrydiary.com/f3f819c4-a054-42f6-a8b3-759a4438a758.png)

### 캘린더
![](https://cdn.juneberrydiary.com/f6141a90-218d-43a3-b873-d64cf8d56d99.png)
![](https://cdn.juneberrydiary.com/22606f3c-29cf-4bbd-91ad-518ce898dbbc.png)
![](https://cdn.juneberrydiary.com/cbca3104-ef68-4e31-becd-b70ea687a762.png)
![](https://cdn.juneberrydiary.com/9b34e21b-da85-4c97-a58e-27bf4c1ef2cf.png)

### 블로그 가입
![](https://cdn.juneberrydiary.com/03451efd-97a2-4253-a69c-66d08e2dd52d.png)
![](https://cdn.juneberrydiary.com/edde3fbf-a912-4de5-9839-0bcee45572d8.png)

### 블로그 리포지토리
(유저가 여러 개의 개인 혹은 팀 블로그를 관리할 수 있습니다.)
![](https://cdn.juneberrydiary.com/54c32264-e5fe-46df-8617-30a926b39add.png)

### 블로그 에디터
(블로그 작성)
![](https://cdn.juneberrydiary.com/e50590d2-8cec-4ff6-9e54-c632dc77ae88.jpg)
![](https://cdn.juneberrydiary.com/f967979b-6fb9-4f69-a137-fa4accf96672.png)
![](https://cdn.juneberrydiary.com/2144ad3b-5a9e-446a-a662-b39412e9a2dd.png)

(블로그 발행)
![](https://cdn.juneberrydiary.com/dbf42017-6397-4d47-80a8-5e2519f899cc.png)
![](https://cdn.juneberrydiary.com/6d45ee47-5fcb-4060-9089-5811fcd45ba4.png)

### 블로그
(메인 블로그, 전체 글)
![](https://cdn.juneberrydiary.com/c44cbb3c-96cc-458e-b13c-1af47a152185.jpg)

(태그 검색)
![](https://cdn.juneberrydiary.com/7424ceed-bca6-48e6-ab97-58707da9b4e8.jpg)

(카테고리 검색)
![](https://cdn.juneberrydiary.com/338312e7-578c-4d17-866e-d6629a4c0266.jpg)

(블로그 포스트)
![](https://cdn.juneberrydiary.com/d3a30ca4-f7f5-414c-b8c3-22cfa522aa20.jpg)

### 블로그 관리
![](https://cdn.juneberrydiary.com/c36c14c6-137b-4e0d-b955-eda7834ee03a.jpg)

### 마이페이지
![](https://cdn.juneberrydiary.com/ef434a5a-c0c1-48d1-b246-41f6b066176e.png)

### not found 페이지
![](https://cdn.juneberrydiary.com/a2eb2266-c96b-41de-a671-8f596eb57cba.png)
