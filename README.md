## 4단계 - 요금 조회

### 요구사항

- 경로 조회 시, 거리 기준 요금 정보 포함
    - 기본 운임(10km 이하): 1,250원
    - 추가 운임
        - 10km 초과 ~ 50km (5km 마다 100원)
        - 50km 초과 (8km 마다 100원)
- 노선별 요금 정책
    - 노선에 추가 요금 정보 추가
    - 측정된 요금에 노선 요금 추가
    - 여러 노선을 이용 시, 가장 높은 금액의 추가 요금만 적용
- 연령별 할인 정책
    - 로그인 사용자에게만 적용
    - 청소년(13세 이상 ~ 19세 미만)는 운임에서 350원을 공제한 금액의 20% 할인
    - 어린이(6세 이상 ~ 13세 미만)는 운임에서 350원을 공제한 금액의 50% 할인

### 기능 정리

- 노선에 추가 요금 정보 추가
- 경로 조회 시, 요금 계산
    - 거리 기준 요금 측정
        - 10km 이하 경로 시, 기본 운임 적용
        - 10km 초과 ~ 50km 이하, 추가 운임 적용
        - 50km 초과 시, 추가 운임 적용
    - 노선 별 요금 측정
        - 여러 노선 이용 시, 가장 높은 금액 추가 요금 적용
        - 거리 기준 요금에 더함
    - 연령별 할인 정책 적용
        - 청소년 또는 어린이 할인 적용

### 도메인 설계

- 경로 탐색기
    - 시작역과 도착역이 주어지면, 요금과 함께 최단 경로를 반환한다.

- 거리별 추가 요금 정책
    - 거리에 따른 추가 요금 계산

- 노선
    - 생성 시, 추가요금 정보 추가
    - 추가 요금 조회

- 노선들
    - 노선 중에 추가요금이 가장 높은 노선 조회

## 3단계 - 인증을 통한 기능 구현

### 요구사항

- 토큰 기반 로그인 기능 구현
- 내 정보 조회 기능 구현
- 즐겨찾기 기능 구현
- 각 기능에 시나리오 기반 인수테스트 작성

### 기능 정리

- 로그인 기능
    - 시나리오 기반 인수테스트, AuthAcceptanceTest
    - 예외케이스
        - 아이디 또는 비밀번호 틀림
- 내 정보 조회 기능
    - 시나리오 기반 인수테스트, MemberAcceptanceTest
    - 내 정보 조회, 수정, 삭제 기능 인수 테스트
        - 토큰을 통해 요청
- 즐겨 찾기 기능
    - 시나리오 기반 인수테스트
    - 즐겨찾기 생성, 목록 조회,삭제

## 2단계 - 경로 조회 기능

### 요구사항

- 최단 경로 조회 인수 테스트 작성
- 최단 경로 조회 기능 구현
- 최단 경로 조회 예외 상황
    - 출발역과 도착역이 같으면 안됨
    - 출발역과 도착역이 연결되지 않으면 안됨
    - 존재하지 않은 출발역과 도착역을 조회할 경우 안됨

### 기능 목록

- 최단 경로 조회 인수 테스트
- 최단 경로 조회 기능
- 최단 경로 예외 상황 처리
    - 출발역과 도착역이 같으면 안됨
    - 출발역과 도착역이 연결되지 않으면 안됨
    - 존재하지 않은 출발역과 도착역을 조회할 경우 안됨

## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항

- 지하철 노선 관련 기능 시나리오 작성
- 지하철 노선 서비스 리팩터링

### 기능 목록

- 지하철 노선 관련 기능 인수테스트 시나리오 작성
- 지하철 노선 서비스 리팩터링
    - [x] 예외처리
    - [x] 노선 조회
    - [x] 노선 목록 조회
    - [x] 노선 저장
    - [x] 노선 수정
    - [x] 노선에 구간 추가
    - [x] 노선에 구간 삭제
- 노선
    - [x] 노선 생성
    - [x] 역들 조회
    - [x] 노선 정보 수정
- 구간들
    - [x] 역들 조회
    - [x] 생성
    - [x] 구간 추가
    - [x] 역 추가
    - [x] 역 삭제
- 구간
    - [x] 구간 생성
    - [x] 상행역 업데이트
    - [x] 하행역 업데이트
    - [x] 구간 병합
- 거리
    - [x] 생성
    - [x] 더하기
    - [x] 빼기
    - [x] 크기 비교

--

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
