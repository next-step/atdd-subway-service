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

- [x] LineService 리팩터링 - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고 도메인으로 이동
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
  - [x] removeLineStation 리팩터링
    - Sections 로 로직 이관
    - 메서드 분리

### Step2 - 경로 조회 기능

- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기
  - [x] 경로 요청 모델 생성(Response)
  - [x] directedGraph 초기화 기능 구현
    - [x] Station을 통해 Vertex 추가
    - [x] Section을 통해 Edge 추가
  - [x] source to target 최단경로 반환 메서드 구현
  - [x] 예외 처리
    - [x] 출발역과 도착역이 같은경우
    - [x] 출발역과 도착역이 연결되어 있지 않은 경우
    - [x] 존재하지 않는 출발역이나 도착역을 조회할 경우

### Step3 - 인증을 통한 기능 구현

- [x] MemberAcceptanceTest - manageMyInfo 인수테스트 작성

  ```
  Feature: 회원 관리 기능
  
    Scenario: 회원을 수정, 삭제, 조회 시도
      Given 회원 등록되어 있음
      When 회원 수정 요청
      Then 회원 정보가 수정됨
  		When 회원 정보를 조회
  		Then 회원 정보가 조회됨
      When 회원을 삭제 요청
      Then 회원 정보가 제거됨
  ```

- [x] 토큰 발급 기능 (로그인) 인수 테스트

  ```
  Feature: 로그인 기능
  	Background
  		Given 회원이 등록되어있음
  
    Scenario: 로그인을 시도한다.
      Given 회원 등록되어 있음
      When 로그인 요청
      Then 로그인 됨
    
    Scenario: 등록되지 않은 회원으로 로그인을 시도하고 실패
      Given 회원 등록되어 있지 않음
      When 로그인 요청
      Then 로그인 실패
      
    Scenario: 유효하지 않는 토큰으로 로그인 시도
      Given 회원 등록되어 있음
      When 로그인 요청
      Then 로그인 실패
  ```

- [x] 인증 - 내 정보 조회 기능 구현

  - [x] /members/me 요청시 토큰 확인하도록 수정

- [x] 인증 - 즐겨 찾기 기능 구현

  ```
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

  - [x] 추가기능
    - [x] createFavorite 메서드 구현
  - [x] 조회기능
    - [x] findFavorites 메서드 구현
  - [x] 삭제기능
    - [x] removeFavorite 메서드 구현

### Step4 - 요금 조회

- [x] 경로 조회 시 거리 기준 요금 정보 포함하기

  ```
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

  - 거리별 요금 정책
    - 기본운임(10km이내) : 1250원
    - 이용 거리초과 시 추가운임
      - 10km초과 ~ 50km까지 : 5km당 100원
      - 16km 이동은 기본운임 1250(기본) + 100(5km 초과) = 1350원

- [ ] 노선별 추가 요금 정책 추가

  - 노선에 추가 요금 필드 추가
  - 추가요금 노선이용시 측정 요금에 추가요금 부과
  - 경로 중 추가요금이 있는 노선 환승시 가장 높은 금액의 추가요금만 적용

- [x] 연령별 할인 정책 추가

  - 청소년 : 13세 이상 ~ 19세 미만
  - 어린이 : 6세 이상 ~ 13세 미만
  - [ ] 로그인 사용자 정보를 활용하도록 API개선

