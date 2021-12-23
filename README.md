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

## 1단계 - 인수 테스트 기반 리팩터링
### 지하철 노선 관련 기능 인수 테스트
- Feature: 지하철 구간 관련 기능
    - Background:
        - Given: 지하철역 등록되어 있음
        - And: 지하철 노선 등록되어 있음
        - And: 지하철 노선에 지하철역 등록되어 있음
    - Scenario: 지하철 구간 관리
      - When: 지하철 구간 등록 요청
      - Then: 지하철 구간 등록됨
      - When: 지하철 노선에 등록된 역 목록 조회 요청
      - Then: 등록한 지하철 구간이 반영된 역 목록이 조회됨
      - When: 지하철 구간 삭제 요청
      - Then: 지하철 구간 삭제됨
      - When: 지하철 노선에 등록된 역 목록 조회 요청
      - Then: 삭제한 지하철 구간이 반영된 역 목록이 조회됨

## 2단계 - 경로 조회 기능
### 최단 경로 조회 인수 테스트
- Feature: 최단 경로 조회 기능
  - Background:
    - Given: 지하철역 등록되어 있음
    - And: 지하철 노선 등록되어 있음
    - And: 지하철 노선에 지하철역 등록되어 있음
  - Scenario: 최단 경로 조회
    - When: 최단 경로 조회 요청
    - Then: 최단 경로 조회됨

## 3단계 - 인증을 통한 기능 구현
### 1. 토근 발급 기능 (로그인) 인수 테스트
- Feature: 로그인 기능
    - Scenario: 로그인을 시도한다.
        - Given 회원 등록되어 있음
        - When 로그인 요청
        - Then 로그인 됨
    - Scenario: 로그인을 시도하지만 실패한다.
        - Give: 회원 등록되어 있음
        - When 로그인 요청
        - Then 로그인 실패
    - Scenario: 유효하지 않은 토큰으로 내 정보 조회시 실패한다.
        - Give 유효하지 않을 토큰이 있음
        - When 내 정보조회 요청
        - Then 내 정보조회 실패
### 2. 인증 - 내 정보 관리 기능 완성하기 인수 테스트
- Feature: 내 정보 관리 기능
    - Background: 
      - Given 회원 등록되어 있음
      - And 로그인에 성공해 토큰을 가지고 있음

    - Scenario: 내 정보를 관리
        - When 내 정보조회 요청
        - Then 내 정보조회 성공
        - When 내 정보수정 요청
        - Then 내 정보수정 성공
        - When 내 정보삭제 요청
        - Then 내 정보삭제 성공
### 3. 인증 - 즐겨 찾기 기능 완성하기 인수 테스트
- Feature: 즐겨찾기를 관리한다.
    - Background:
        - Given: 지하철역 등록되어 있음
        - And 지하철 노선 등록되어 있음
        - And 지하철 노선에 지하철역 등록되어 있음
        - And 회원 등록되어 있음
        - And 로그인 되어있음
    - Scenario: 즐겨찾기를 관리
        - When 즐겨찾기 생성을 요청
        - Then 즐겨찾기 생성됨
        - When 즐겨찾기 목록 조회 요청
        - Then 즐겨찾기 목록 조회됨
        - When 즐겨찾기 삭제 요청
        - Then 즐겨찾기 삭제됨
## 4단계 - 요금 조회
### 1. 경로 조회 시 거리 기준 요금 정보 포함하기
- 기본운임(10km 이내) : 1,250
- 이용 거리초과 시 추가 운임
  - 10km초과 ~ 50km까지(5km마다 100원)
  - 50km초과 시(8km마다 100원)
- 지하철 경로 검색 인수 조건 수정

### 2. 노선별 추가 요금 정책 추가
- 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
- 900원 추가 요금이 있는 노선 12km 이용 시 이용 거리 초과 추가 운임이 추가 되어 1,350원 -> 2,250원
- 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용

### 3. 로그인 사용자의 경우 연령별 할인 정책 추가
- 청소냔(13세 이상~19세 미만) : 운임에서 350원을 공제한 금액의 20% 할인
- 어린이(6세 이상 ~ 13세 미만) : 운임에서 350원을 공제한 금액의 50% 할인
