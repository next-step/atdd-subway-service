<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-service">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.

---

# 1단계 : 인수 테스트 기반 리팩터링

### 1. AcceptanceTest 리팩터링
- [X] RestAssured 중복코드를 AcceptanceTest 로 이동
- [X] LineSectionAcceptanceTest 인수 테스트 코드와 인수 테스트에 사용되는 메소드를 분리(클래스 분리)
- [X] LineAcceptanceTest 인수 테스트 코드와 인수 테스트에 사용되는 메소드를 분리(클래스 분리)
- [X] MemberAcceptanceTest 인수 테스트 코드와 인수 테스트에 사용되는 메소드를 분리(클래스 분리)
- [X] StationAcceptanceTest 인수 테스트 코드와 인수 테스트에 사용되는 메소드를 분리(클래스 분리)

### 2. LineService 리팩터링
- [X] LineService 의 비즈니스 로직을 도메인으로 옮기기
- [X] Domain 의 단위 테스트를 작성하기
    - [X] Line
      - [X] LineName
      - [X] LineColor
    - [X] Station
      - [X] StationName
    - [X] Distance
    - [X] Section
    - [X] Sections
- [X] 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
- [X] 정상 동작 확인 후 기존 로직 제거

---

# 2단계 : 경로 조회 기능

### 1. 최단 경로 조회 인수 테스트 만들기
- [X] 출발역에서 도착역까지 최단경로를 조회한다.
- [X] 예외 케이스
    - [X] 출발역과 도착역이 같은 경우
    - [X] 출발역/도착역이 노선상에 존재하지 않은 경우
    - [X] 출발역과 도착역이 연결되어 있지 않은 경우

### 2. 최단 경로 조회 기능 구현하기
- [X] PathFinder 내부에서 jgrapht 라이브러리 사용
- [X] 출발역에서 도착역 까지의 최단 루트 구하기
- [X] 출발역에서 도착역 까지의 최단 거리 구하기
- [X] 최단거리 Test 코드 작성

---

# 3단계 : 인증을 통한 기능 구현

### 1. 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [X] 토큰 발급(로그인)을 검증하는 AuthAcceptanceTest 인수 테스트 작성
- [X] 예외 케이스
    - [X] 등록되지 않은 회원정보로 토큰을 요청하는 경우
    - [X] 토큰 없이 요청을 보낸 경우
    - [X] 유효하지 않은 Bearer 토큰으로 요청을 보낸 경우
- [X] 이메일과 패스워드를 이용하여 요청 시 access token 을 응답하는 기능을 구현

### 2. 인증 - 내 정보 조회 기능 완성하기
- [X] MemberAcceptanceTest 에 내 정보 조회/수정/사제 기능 인수 테스트 추가
- [X] @AuthenticationPrincipal 활용하여 기능 구현
- [X] AuthenticationPrincipalArgumentResolver 활용하여 기능 구현

### 3. 인증 - 즐겨 찾기 기능 완성하기
- [ ] 즐겨찾기 기능 구현
- [ ] 인증을 포함하여 즐겨찾기 관련 인수 테스트 작성
    - [ ] 즐겨찾기 생성
    - [ ] 즐겨찾기 목록 조회
    - [ ] 즐겨찾기 삭제
    - [ ] 예외 케이스
        - [ ] 최단경로를 찾을 수 없는 출발역/도착역인 경우 즐겨찾기 등록 실패