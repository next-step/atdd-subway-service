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

## Step1
- [X] LineService 리팩토링

- [X] 인수 테스트 통합
  - [X] LineAcceptanceTest
  - [X] LineSectionAcceptanceTest

## Step2
- [X] 최단 경로 조회 인수 테스트 만들기
  - [X] 출발역과 도착역이 같은 경우 최단 경로 조회 실패
  - [X] 출발역과 도착역이 연결되어 있지 않은 경우 최단 경로 조회 실패
  - [X] 존재하지 않는 역을 출발역이나 도착역으로 조회할 경우 최단 경로 조회 실패
  - [X] 최단 경로가 존재하는 경우 최단 경로 조회 성공
 
- [X] 최단 경로 조회 기능 구현하기

## Step3
- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
  - [X] 로그인 성공 - 내 정보 조회 성공
  - [X] 로그인 실패
  - [X] 로그인 성공 - 잘못된 토큰, 내 정보 조회 실패
- [X] 내 정보 관리 인수 테스트 만들기
- [X] 인증 - 내 정보 조회 기능 완성하기
- [X] 즐겨찾기 관리 인수 테스트 만들기
- [X] 인증 - 즐겨 찾기 기능 완성하기

## Step4
- [X] 경로 조회 시 거리 기준 요금 정보 포함하기
  - [X] 인수 테스트 수정
  - [X] 도메인 영역 수정
- [ ] 노선별 추가 요금 정책 추가
  - [ ] 인수 테스트 추가
  - [ ] 도메인 영역 수정 
- [ ] 연령별 할인 정책 추가
  - [ ] 인수 테스트 추가
  - [ ] 도메인 영역 수정
