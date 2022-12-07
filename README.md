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

## Step3. 인증을 통한 기능 구현

### 요구사항

- [x] 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
  - [x] `AuthAcceptanceTest` 의 빈 테스트 메소드 채워넣으면서 기능 구현하기
  - [x] 유효하지 않은 토큰으로 `/members/me` 요청 시 예외 처리 테스트 및 기능 구현
- [x] 인증 - 내 정보 조회 기능 완성하기
  - [x] `manageMyInfo` 테스트 메소드에 인수 테스트 추가
  - [x] 내 정보 조회/수정/삭제 기능을 `/members/me` URI 요청으로 동작하도록 검증하는 테스트 추가
  - [x] `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
    - [x] `@AuthenticationPrincipal` 과 `AuthenticationPrincipalArgumentResolver` 활용하기
- [ ] 인증 - 즐겨찾기 기능 완성하기
  - [x] 즐겨찾기 생성/목록조회/삭제 인수테스트
  - [x] 즐겨찾기 생성/목록조회/삭제 기능구현
  - [x] `Favorites`, `Member` 도메인 테스트 코드
