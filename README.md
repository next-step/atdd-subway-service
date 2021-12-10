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
    - 노선에 구간 삭제
- 노선
    - [x] 노선 생성
    - [x] 역들 조회
    - 노선 정보 수정
- 구간들
    - [x] 역들 조회
    - [x] 생성
    - [x] 구간 추가
    - [x] 역 추가
    - 역 삭제
- 구간
    - [x] 구간 생성
    - [x] 상행역 업데이트
    - [x] 하행역 업데이트
    - 구간 병합
- 거리
    - 생성
    - 더하기
    - 빼기

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
