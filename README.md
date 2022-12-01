<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
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
# 🚀 1단계 - 인수 테스트 기반 리팩터링
## 요구사항
- LineService 리팩터링
- LineSectionAcceptanceTest 리팩터링

# 🚀 2단계 - 경로 조회 기능
## 요구사항
- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현하기 

## 예외 상황 테스트
 - 출발역과 도착역이 같은 경우
 - 출발역과 도착역이 연결이 되어 있지 않은 경우
 - 존재하지 않은 출발역이나 도착역을 조회 할 경우

# 🚀 3단계 - 인증을 통한 기능 구현
## 요구사항
- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기

## 인수테스트

### 로그인 기능
- 이메일과 패스워드를 이용하여 요청 시 Access Token을 응답하는 기능 구현
- 잘못된 이메일과 패스워드를 사용하여 로그인 요청시 로그인을 실패한다.
- 유효하지 않은 토큰으로 "내 정보"를 요청할 경우 요청에 실패한다.

### 내 정보 조회 기능
- 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
- 로그인 후 발급 받은 토큰을 포함해서 요청
- `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기

### 즐겨 찾기 기능
- 즐겨찾기 생성
- 즐겨찾기 목록 조회
- 즐겨찾기 삭제
- 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기





