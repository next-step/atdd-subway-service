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

## :pencil2: 요구사항 정리

### Step1 - 인수 테스트 기반 리팩터링

- [x] LineSectionAcceptanceTest 리팩터링
  - API 검증보다 시나리오, 흐름 검증 테스트로 리팩터링
  - 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
  
  ```
  Feature: 지하철 구간 관련 기능
  
    Background 
      Given 지하철역 등록되어 있음
      And 지하철 노선 등록되어 있음 강남-광교
      And 지하철 노선에 지하철역 등록되어 있음
  
    Scenario: 지하철 구간을 순서 상관없이 등록하고 지하철역을 제거
    	When 최초 추가된 노선 조회 요청
      Then 노선에 등록된 역 목록 확인
      When 지하철 구간 추가 등록
      Then 지하철 구간 추가 등록됨
      When 지하철 노선에 등록된 역 목록 조회 요청
      Then 지하철 노선에 등록된 역 목록 조회 확인
      When 지하철 구간 제거
      Then 지하철 구간 추가 등록됨
      When 지하철 노선에 등록된 역 목록 조회 요청
      Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
  
  	Scenario: 중복된 지하철 구간을 등록
  		When 동일한 지하철 구간 등록
  		Then 지하철 구간 등록 오류
  
  	Scenario: 노선과 관계없는 역을 기준으로 등록
  		When 관계없는 지하철역 구간 등록
  		Then 지하철 구간 등록 오류
  	
  	Scenario: 노선에 역이 두개 있을 때 제거
  		When 최초 등록된 역중 하나로 제거
  		Then 지하철 구간 등록 오류
  ```

- [ ] LineService 리팩터링 - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고 도메인으로 이동
  - [x] saveLine 리팩터링
    - getStations 도메인으로 이동 및 메서드 분리
    - LineEntity 개선 - List<Section> 일급컬렉션으로 분리 및 로직 이동
    - lineResponse 개선
  - [x] findLines 리팩터링
  - [x] findLineResponseById 리팩터링
  - [x] addLineStation 리팩터링
    - 등록된 역 목록에 구간이 등록되어있는지 확인하는 로직을 분리
    - 구간 등록의 예외처리로직 도메인으로 이동 - Sections로
    - 상행 구간 추가로직 이동
    - 하행 구간 추가로직 이동
    - Distance 일급객체로 분리
    - List<Station> 일급컬렉션으로 분리
  - [ ] removeLineStation 리팩터링



