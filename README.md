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

## Step2 - 경로 조회 기능
### 구현 기능 명세
- Domain 레벨
    - [x] 최단 경로 조회
    - [x] Station A와 B가 경로로 연결되어 있는지 확인
    - [x] 경로 노드 내 역이 존재하는 지 확인

- Service 레벨
    - [x] 출발역 - 도착역 최단 경로 조회
    - [x] 출발역과 도착역이 같은 경우 예외 발생
    - [x] 출발역과 도착역이 연결이 되어 있지 않은 경우 예외 발생
    - [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우 예외 발생

- Controller 레벨
    - [x] 출발역 - 도착역 최단 경로 조회
    - [x] 예외 Input 에 대해 Bad Request 반환

<br>

## Step2 - 경로 조회 기능
### 구현 기능 명세
- 토큰 발급 인수 테스크 (AuthAcceptanceTest)
  - [x] 로그인 정상
  - [x] 로그인 실패
  - [x] 유효하지 않은 토큰
  
- 나의 정보 관리 테스트 (MemberAcceptanceTest)
  - [x] 나의 정보 조회
  - [x] 나의 정보 수정
  - [x] 나의 정보 삭제
  
- 즐겨찾기 기능 구현 및 테스트 (FavoriteAcceptanceTest)
  - [ ] 즐겨찾기 추가
  - [ ] 즐겨찾기 조회
  - [ ] 즐겨찾기 수정
  - [ ] 즐겨찾기 삭제
  

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
