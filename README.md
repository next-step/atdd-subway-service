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

## 인수 테스트 리팩터링

- 테스트를 유지보수 하기 좋은 상태로 만들기 위해 리팩터링이 필요
- 예외 상황에 대한 인수 테스트를 단위 테스트로 단계를 낮추어 관리하면 테스트에 필요한 시간과 비용을 줄일 수 있음
- 리팩터링 시 중요한 점은 기존에 검증하고 있던 프로덕션 코드를 리팩터링 후에도 여전히 잘 검증되고 있음을 보장받을 수 있어야 함

## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항

- LineSectionAcceptanceTest 리팩터링
  - [x] 흩어져 있는 테스트를 한 곳에 통합 시도
- LineService 리팩터링
  - LineService의 비즈니스 로직을 도메인으로 옮기기
    - [x] Domain으로 옮길 로직 찾기
      - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮기기
      - 객체지향 생활체조 규칙 명심!
    - [x] Domain의 단위 테스트 작성
      - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
      - SectionTest나 LineTest클래스가 생성될 수 있음
    - [ ] 로직 옮기기
      - 기존 로직을 지우지 않고 새로운 로직을 만들어 수행
      - 정상 동작 확인 후 기존 로직 제거

```gherkin
Feature: 지하철 구간 관련 기능

  Background
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 지하철 구간 등록 요청
    Then 지하철 구간 등록됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    When 지하철 구간 삭제 요청
    Then 지하철 구간 삭제됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```

### 1단계 미션 피드백 사항
- [x] SectionsTest 거리에 대한 검증 실시
- [x] 한 줄에 점을 하나만 찍을 것 -> 가독성, 협업 차원에서 유의미하다.
- [x] @Transactional(readOnly=true) 클래스 레벨에서 선언하고, 데이터 쓰기 작업이 있을 경우 명확하게 나타내도록 하기
- [x] 엔티티 기본 생성자 숨기기!!
- [x] 주 생성자, 부 생성자 구분지어 사용하기!
- [x] 엔티티 cascade 옵션 고민해서 사용하기
- [x] jpa entity field 제약조건 공부하기
- [x] Stations 로직 점검 및 불필요 시 걷어내기

## 2단계 - 경로 조회 기능

### 요구사항
- 최단 경로 조회 인수 테스트 만들기
  - [x] PathAcceptanceTest 작성
- 최단 경로 조회 기능 구현하기
  - [x] 인수 테스트 성공 시키기
  - [x] 컨트롤러 및 DTO 작성
  - [x] PathFinder 도메인 테스트 작성
  - [x] 서비스 테스트 작성
  - [x] 서비스 레이어 구현

### 2단계 미션 피드백 사항
- [x] MockitoExtensionTest 오타 수정
- [x] PathServiceTest @InjectMocks 사용으로 인해 setUp 메소드 불필요하므로 제거
- [x] PathFinder에 대해 외부라이브러리 의존성을 제거하기 위해 의존성 주입 전략을 세워 리팩터링
- [x] PathFinderTest default 접근제어자 수정
- [x] Section.setGraphEdge 네이밍 set -> add로 수정
- [x] 예외 처리 시, 유의미한 에러 메시지 작성

## 3단계 - 인증을 통한 기능 구현
- [ ] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [ ] 인증 - 내 정보 조회 기능 완성하기
- [ ] 인증 - 즐겨 찾기 기능 완성하기

### 구현 명세
- 토큰 발급 기능 (로그인) 인수 테스트 작성 및 구현
  - [ ] 인수 테스트 작성
  - [ ] 토큰 발급 테스트 작성
  - [ ] email, password를 받는 요청DTO 작성
  - [ ] Controller 작성
  - [ ] accessToken을 반환하는 응답DTO 작성

```gherkin
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```