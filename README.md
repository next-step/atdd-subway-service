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

## 요구사항

### "Step 1"

- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링

### "Step 2"

- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

### "Step 3"

- 토큰 발급 기능
    - [x] 토큰 발급(로그인)을 검증하는 인수 테스트(AuthAcceptanceTest) 만들기
    - [x] Bearer Auth 로그인 실패 인수테스트 추가
    - [x] Bearer Auth 유효하지 않은 토큰 인수 테스트 추가
- 내 정보 관련 기능
    - [x] 내 정보 조회 인수테스트 추가
    - [x] 내 정보 수정 인수테스트 추가
    - [x] 내 정보 삭제 인수테스트 추가
- 즐겨 찾기 기능
    - [x] 즐겨찾기 생성 인수테스트 추가
    - [x] 즐겨찾기 목록 조회 인수테스트 추가
    - [x] 즐겨찾기 삭제 인수테스트 추가
    - [x] 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

### "Step 4"

- 경로 조회 시 거리별 요금 정책 적용
    - 기본운임(10㎞ 이내) : 기본운임 1,250원
    - 이용 거리초과 시 추가운임 부과
        - 10km초과∼50km까지(5km마다 100원)
        - 50km초과 시 (8km마다 100원)
    - [x] 인수 테스트 추가/수정
    - [x] 기능 구현하기
- 노선별 추가 요금 정책
    - 노선에 추가 요금 필드를 추가
    - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
        - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
        - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
    - 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
        - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
    - [x] 인수 테스트 추가/수정
    - [x] 기능 구현하기
- 연령별 할인 정책 추가
    - 청소년: 운임에서 350원을 공제한 금액의 20%할인
        - 청소년: 13세 이상~19세 미만
    - 어린이: 운임에서 350원을 공제한 금액의 50%할인
        - 어린이: 6세 이상~ 13세 미만
    - [x] 인수 테스트 추가/수정
    - [ ] 기능 구현하기

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
