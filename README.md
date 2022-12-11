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

## Step1 인수 테스트 기반 리팩터링
### 미션 요구사항
- LineService 리팩터링
- LineSectionAcceptanceTest 리팩터링
### 미션 목적: 리팩터링
- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기
### 미션 요구사항 설명
- Domain으로 옮길 로직 찾기
    * 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고 도메인으로 옮기기
    * 객체지향 생활체조 참고
        * 규칙 1: 한 메서드에 오직 한 단계의 들여쓰기(indent)만 한다.
        * 규칙 2: else 예약어를 쓰지 않는다.
        * 규칙 3: 모든 원시값과 문자열을 포장한다.
        * 규칙 4: 한 줄에 점을 하나만 찍는다.
        * 규칙 5: 줄여쓰지 않는다(축약 금지).
        * 규칙 6: 모든 엔티티를 작게 유지한다.
        * 규칙 7: 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
        * 규칙 8: 일급 콜렉션을 쓴다.
        * 규칙 9: 게터/세터/프로퍼티를 쓰지 않는다.
- Domain의 단위 테스트 작성하기
    * 서비스 레이어에서 옮겨올 로직의 기능 테스트
    * SectionTest나 LineTest 클래스가 생성될 수 있음
- 로직을 옮기기
    * 기존 로직을 지우지 않고 새로운 로직 만들어 수행
    * 정상 동작 확인 후 기존 로직 제거
### LineService 리팩터링 대상 기능목록
- [X] 지하철 노선 생성
- [X] 지하철 노선 목록 조회
- [X] 지하철 노선 조회
- [X] 지하철 노선 수정
- [X] 지하철 노선 삭제
- [X] 지하철 노선 내 구간 추가
- [X] 지하철 노선 내 역 삭제(구간 삭제)
#### LineSectionAScceptanceTest 리팩터링
* [X] API 검증이 아닌 시나리오, 흐름을 검증하는 테스트로 리팩터링
* [X] 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러 시나리오 만들어 인수 테스트 작성
#### 인수 조건 예시
  ```text
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
#### Step1 회고 작성
- 미션 요구사항뿐만 아니라, 다른 리팩토링 대상이 있으면 주도적으로 처리필요
  - 요구사항외 이 내용도 바꿔도 될까? 라는 고민이 있으면 합당하다면 수행
- 의미 있는 Commit 단위를 TDD 기반으로 작성 필요
- 메서드 순서 클린코드 기반하여 작성 public / private(호출되는 순서)
