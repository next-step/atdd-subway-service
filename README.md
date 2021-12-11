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

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md)
licensed.

## 인수 테스트 기반 TDD 미션

### 1단계 - 인수 테스트 기반 리팩터링

- [X] LineSectionAcceptanceTest 리팩터링
- [X] LineSectionAddAcceptanceTest 라인 추가관련 분리
- [X] LineSectionRemoveAcceptanceTest 라인 제거관련 분리
- [X] LineService 리팩터링
- [X] `@ControllerAdvice` 적용
- [X] 상황별 예외처리
- 테스트
    - [X] SectionTest
    - [X] SectionsTest
    - [X] LineTest
    - [X] LineRepositoryTest
    - [X] LineServiceTest

### 2단계 - 경로 조회 기능

- `PathAcceptanceTest` 구현
    - [X] 정상 경로 조회
    - [X] 같은 역 경로 조회 실패
    - [X] 이어지지 않는 경로 조회 실패
- `PathController` 구현
    - [X] 경로조회 라우터 추가
- [X] `PathService` 경로 조회 서비스 구현
- [X] `PathFactory` 인터페이스 활용하여 외부 모듈 의존역전시키기
- infrastructure 패키지 분리
    - [X] `PathFactory` 구현체 구현

#### 인수 시나리오

```
Feature: 지하철 경로 조회 관련 기능
  Background
    Given 지하철역_등록되어_있음
    And 지하철_노선_등록되어_있음

  Scenario: 최단경로_조회
    When 경로_조회
    Then 최단경로_조회_됨
    Then 최단경로_조회_길이_계산됨

  Scenario: 같은_역_경로_조회_실패
    When 경로_조회
    Then 경로_조회_실패됨
    
  Scenario: 이어지지_않는_경로_조회_실패
    When 경로_조회
    Then 경로_조회_실패됨
    
```

#### 2단계 피드백 개선사항

- `SectionEdge`, `SubwayGraph` 상속을 활용하여 개선해보기

---

### 3단계 - 인증을 통한 기능 구현

- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
    - [X] `이메일` 과 `패스워드`를 이용하여 요청 시 `access token`을 응답하는 기능을 구현하기
- [X] `AuthAcceptanceTest`  완성하기
- [X] `AuthAcceptanceTest` 실패 케이스 완성하기
    - [X] `Bearer Auth` 유효하지 않은 토큰 인수 테스트
- [X] 인증 - 내 정보 조회 기능 완성하기
- [X] 인증 - 즐겨 찾기 기능 완성하기
    - [X] 즐겨찾기 생성
    - [X] 즐겨찾기 목록조회
    - [X] 즐겨찾기 삭제

#### 인수 시나리오

```text

Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
    
Feature: 즐겨찾기를 관리한다.

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음

  Scenario: 즐겨찾기를 관리
    When 즐겨찾기 생성을 요청
    Then 즐겨찾기 생성됨
    When 즐겨찾기 목록 조회 요청
    Then 즐겨찾기 목록 조회됨
    When 즐겨찾기 삭제 요청
    Then 즐겨찾기 삭제됨

```

### 4단계 - 요금 조회

- [ ] 노선에 `추가요금` 필드 추가하기
    - [ ] 노선 테스트 코드 리팩토링
- [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
    - [ ] 노선별 추가 요금정책
- [ ] 로그인 유저일 경우
    - [ ] 연령별 할인 요금정책

#### 요금 정책

```text
- 거리별 요금 정책
  - 기본운임(10㎞ 이내) : 기본운임 1,250원
  - 이용 거리초과 시 추가운임 부과
  - 10km초과∼50km까지(5km마다 100원)
  - 50km초과 시 (8km마다 100원)
- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
  - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
  - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
- 로그인 사용자의 경우 연령별 요금 할인 적용
  - 청소년: 운임에서 350원을 공제한 금액의 20%할인
  - 어린이: 운임에서 350원을 공제한 금액의 50%할인
```

#### 인수 조건 수정 시나리오

```text
Feature: 지하철 경로 검색

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리도 함께 응답함
    And ** 지하철 이용 요금도 함께 응답함 **
```