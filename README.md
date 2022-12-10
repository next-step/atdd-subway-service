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

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md)
licensed.

## 요구사항 정리

### 1단계

1. Domain으로 옮길 로직을 찾기

- LineService 구간 추가 로직 적절한 도메인으로 이동
- LineService 구간 제거 로직 적절한 도메인으로 이동
- LineService 정류장 조회 로직 적절한 도메인을

2. Domain의 단위 테스트를 작성하기

- 구간 추가 단위테스트 작성
- 구간 추가시 해당 구간이 이미 있을떄에 대한 오류 단위테스트 작성
- 구간 추가시 연결된 구간이 없으면 오류 단위테스트 작성
- 구간 제거 단위테스트 작성.
- 구간 제거시 모든 구간이 제거되는경우 오류 단위테스트 작성.

### 피드백 요구사항 정리.

- 요구사항을 정리한다.
- 서비스와 DTO 생성을 분리.
- sections멤버 변수를 바로 초기화 해주어 null 체크
- Distance 원시값 포장
- Distance 단위테스트 작성
- entity 생성자 protected 처리
- Arrays.asList() 보단 Collection.emptyList() 사용
- 메소드 길이 10줄 미만으로 줄이기 - 함수추출
- 단위 테스트 BDD패턴으로 변경

### 2단계 경로 조회 기능 요구사항 명세

* 통합 인수 테스트 작성
    * Feature: 지하철 경로 관련 기능
        * Background
            * Given 지하철역 등록되어 있음
            * AND 지하철 노선 등록되어 있음
            * AND 지하철 노선에 지하철 역 등록되어 있음
        * Scenario
            * WHEN 지하철 출발역에서 도착역 최단경로 조회
            * THEN 지하철 출발역에서 도착역 최단경로 목록 조회됨
* Domain 단위 테스트 작성
    * Stations 와 Section을 이용하여 경로추적 도메인 생성
    * 경로추적 도메인에서 최단경로 희망 정류장을 인자로 하여 최단경로를 반환하는 함수 생성
        * 예외사항
            * 출발역과 도착역이 같은경우 에러 발생
            * 출발역과 도착역이 연결되있지 않은 경우 에러 발생
            * 경로추적 도메인에 없는 출발역과 도착역을 설정하는 경우.
    * 경로 추적 도메인에서 최단경로 총 거리 조회

### 피드백 사항 정리

* Sections 와 외부 라이브러리 완전 분리
* Fixture 메소드와 테스트 메소드 분리 [완료]

### 3단계 인증을 통한 기능 구현

#### 토큰발급

* 토큰을 발급 후 검증하는 인수테스트 생성 [완료]
* 토큰 발급 후 /members/me 요청 보내면 정상 [완료]
* 토큰 미발급시 404 처리 [완료]
* 토큰 발급 예외처리 [완료]

#### 내 정보 조회 기능

* 내 정보 기능 인수테스트 작성 [완료]
* 토큰을 통한 접근제어 하면서 기능 구현 [완료]

#### 즐겨 찾기 기능 구현

* 즐겨찾기 인수테스트 시나리오 테스트 작성 [완료]
* 즐겨찾기 API구현 [완료]
    * 즐겨 찾기 생성 구현 [완료]
    * 즐겨 찾기 목록 조회 구현 [완료]
    * 즐겨 찾기 삭제 구현 [완료]

### 4단계 - 요금 조회

#### 기존 피드백 사항 정리

* 즐겨찾기 인수테스트 Fixture 분리.
* AuthorizationException 에 "아이디 또는 패스워드가 일치하지 않습니다" 구체적 메시지 전달
* AuthorizationException Handling not found -> forbidden 변경

#### 요구사항

* 경로 조회 시 거리 기준 요금 정보 포함 [완료]

    * 10KM 이내 기본 운임 요금
    * 10 ~ 50KM (5KM 마다 100원)
    * 50KM 초과시 (8KM 당 100원)

* 노선별 추가 요금 정책 추가
    * 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가[완료]
    * 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용 [완료]
* 연령별 할인 정책 추가

    * 청소년(13-19세) 350원 공제한 금액의 20% 할인
    * 어린이(6~13세) 350원 공제한 금액의 50% 할인
