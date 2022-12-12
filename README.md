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

## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항

- LineService 비즈니스 로직 도메인으로 옮기기
  - [X] saveLine
    - 정적 메소드 from 사용하기
    - Sections 일급컬렉션 사용하여 비즈니스 로직 옮기기
    - Distance 값 타입 변경
  - [X] findLines
  - [X] findLineById
  - [X] findLineResponseById
  - [X] updateLine
  - [X] addLineStation
    - 비즈니스 로직 도메인으로 이동
  - [X] removeLineStation
    - 비즈니스 로직 도메인으로 이동

- LineSectionAcceptanceTest 리팩터링
  - [X] 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
    - dynamicTest 활용하여 시나리오 검증

## 2단계 - 경로 조회 기능

### 요구사항

- [X] 최단 경로 조회 인수 테스트 만들기
  - PathAcceptanceTest 생성
- [X] 최단 경로 조회 기능 구현하기
  - Path 도메인 관련 기능 구현하기

## 3단계 - 인증을 통한 기능 구현

### 요구사항

- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [X] 인증 - 내 정보 조회 기능 완성하기
- [X] 인증 - 즐겨 찾기 기능 완성하기
  - [X] 즐겨찾기 추가하기
  - [X] 즐겨찾기 조회하기
  - [X] 즐겨찾기 삭제하기

## 4단계 - 요금 조회

### 요구사항

- [X] 경로 조회 시 거리 기준 요금 정보 포함하기
- [X] 거리별 추가 요금 계산
- [X] 노선별 추가 요금 정책 추가
- [X] 로그인 사용자의 경우 연령별 요금 할인 적용
  - 청소년 (13세 이상~19세 미만) : 운임에서 350원을 공제한 금액의 20%할인
  - 어린이 (6세 이상~ 13세 미만) : 운임에서 350원을 공제한 금액의 50%할인


<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
