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

## 1단계 - 인수 테스트 기반 리팩터링
### 요구사항
- LineSectionAcceptanceTest 리팩터링
- LineService 리팩터링
- API 를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어
인수 테스트를 작성할 수 있음

### 구현할 기능 목록
- [x] LineService 에서 Domain 으로 옮길 로직
  - [x] 지하철 목록 가져오기
  - [x] 구간 등록
  - [x] 구간 삭제

## 2단계 - 경로 조회 기능
### 요구사항
- 최단 경로 조회 인수테스트 만들기
- 최단 경로 조회 기능 구현하기

### 구현할 기능 목록
- [x] 최단 경로 조회
- [x] 예외
  - [x] 출발역과 도착역이 같은 경우
  - [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
    - [x] 구간 저장 과정에서 예외 처리가 되어있어서 출발역과 도착역이 연결되어 있지 않은 경우는 없음
  - [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우

## 3단계 - 인증을 통한 기능 구현
### 요구사항
- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기

### 구현할 기능 목록
- [x] 로그인 기능
  - [x] 토큰을 통한 인증
  - [x] 잘못된 토큰 정보
  - [x] 잘못된 로그인 정보
- [x] 내 정보 조회 기능
  - [x] 조회
  - [x] 수정
  - [x] 삭제
- [x] 즐겨 찾기 기능
  - [x] 조회
  - [x] 생성
  - [x] 삭제

## 4단계 - 요금 조회
### 요구사항
- 경로 조회 시 거리 기준 요금 정보 포함하기
- 노선별 추가 요금 정책 추가
- 연령별 할인 정책 추가

### 구현할 기능 목록
- [x] 경로 조회 시 거리 기준 요금 정보 포함
  - [x] 기본 운임 (10km 이내) : 1,250원
  - [x] 이용 거리초과 추가운임 부과
    - [x] 10km 초과 ~ 50km 까지(5km 마다 100원)
    - [x] 50km 초과 시 (8km 마다 100원)
- [x] 노선별 추가 요금 정책
  - [x] 노선에 추가 요금 추가
  - [x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
- [x] 로그인 사용자의 경우 연령별 요금 할인 적용
  - [x] 청소년
    - [x] 13세 이상 ~ 19세 미만
    - [x] 350원을 제외한 금액의 20% 할인
  - [x] 어린이
    - [x] 6세 이상 ~ 13세 미만
    - [x] 350원을 제외한 금액의 50% 할인

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
