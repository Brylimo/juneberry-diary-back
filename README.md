# juneberry diary backend
juneberry diary backend project

준베리다이어리는 크게 지도에 장소를 저장하는 기능, 캘린더 및 투두리스트 기능, 블로그 기능을 제공합니다.

### juneberry diary backend api 서버 개발 스택
- 백엔드
  -  Spring Boot, redis, postgresql, spring security, spring data jpa, querydsl, docker, nginx, jenkins, java
  -  jenkins 서버를 별도로 둬서 CI/CD 작업을 자동화
  -  어플리케이션 서버는 nginx를 리버스 프록시로 사용하고 도커를 위에 띄우는 구조

### juneberry diary 프로젝트 작업
- 프로젝트 작업 기간
  - 2023.08.15 ~ 현재
- 프로젝트 개발 투입 인원
  - 1명(나)

### 로컬 실행 방법
1. java 17버전 이상 설치 및 코드 클론
2. active profile을 dev로 설정한 후 실행(실제 서비스와 연결된 DB 사용), 로컬 DB와 연결할 경우 active profile을 local로 설정한 후 실행
3. 스프링부트 실행

포트: 8081
