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

## 경로 조회 기능
- 출발역에서 도착역까지 갈 수 있는 최단경로와 최단거리를 조회한다
- 역은 노선의 구간으로 연결되어있다
- 출발역과 도착역이 같으면 경로를 조회할 수 없다
- 출발역에서 도착역까지 연결되어있지 않으면 조회할 수 없다
- 출발역과 도착역이 존재하지 않는 역인 경우 조회할 수 없다

### 인수테스트 조건
- [x] 경로 조회
  - 지하철역과 노선을 등록하고
  - 출발역과 도착역 사이의 경로를 조회하면
  - 최단경로와 최단거리가 조회된다
- [x] 경로 조회 실패
  - 지하철역과 노선을 등록하고
  - 출발역과 도착역이 같으면
  - 경로 조회 실패한다
  - 출발역 또는 도착역이 존재하지 않으면
  - 경로 조회 실패한다
  - 출발역과 도착역이 이어져 있지 않으면
  - 경로 조회 실패한다

## 로그인 기능
- [x] 로그인 성공
  - 회원 등록되어 있음
  - 로그인 요청
  - 로그인 성공
- [x] 로그인 실패
  - 등록되지 않은 계정
  - 로그인 요청
  - 로그인 실패
- [x] 유효하지 않은 토큰
  - 로그인 하지 않음
  - 내 정보 조회
  - 조회 실패

## 내 정보 조회 기능
- [ ] 나의 정보를 관리한다
  - 회원 등록되어 있음
  - 로그인 되어있음
  - 내 정보 조회
  - 조회 성공
  - 내 정보 변경
  - 변경 성공
  - 내 정보 조회
  - 변경된 내용 확인
  - 회원 탈퇴
  - 탈퇴 성공
  - 내 정보 조회
  - 조회 실패

## 즐겨찾기 기능
- [ ] 즐겨찾기를 관리한다
  - 지하철역 등록되어 있음
  - 지하철 노선 등록되어 있음
  - 지하철 노선에 지하철역 등록되어 있음
  - 회원 등록되어 있음
  - 로그인 되어있음
  - 즐겨찾기 생성을 요청
  - 즐겨찾기 생성됨
  - 즐겨찾기 목록 조회 요청
  - 즐겨찾기 목록 조회됨
  - 즐겨찾기 삭제 요청
  - 즐겨찾기 삭제됨
