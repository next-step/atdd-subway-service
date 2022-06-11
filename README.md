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

---

### 1단계 - 인수테스트 기반 리팩터링
 1. LineService 리팩터링
    - [] 도메인으로 옮길 비즈니스 로직을 확인하고 옮기기
      - [X] Line 도메인 리팩토링
      - [X] Section 도메인 리팩토링
      - [X] Station 도메인 리팩토링
      - [] LineService에서 도메인으로 옮겨올 비즈니스 로직 리팩토링
    - [] 옮겨진 비즈니스 로직을 확인하는 도메인 테스트 코드 작성

 2. LineSectionAcceptanceTest 리팩터링
    - [X] AcceptanceTest 리팩토링 : 공통적으로 사용될 수 있는 부분 추출
      - [X] LineAcceptanceTest 기준 리팩토링 진행
      - [X] LineSectionAcceptTest 기준 리팩토링 진행
    - [X] 테스트코드와 테스트에서 사용되는 메소드 분리
    - [X] 통합 인수테스트 작성 및 패스
      - 지하철 구간을 관리
        * When. 지하철 구간 등록 요청
        * Then. 지하철 구간 등록됨.
        * When. 지하철 노선에 등록된 역 목록 조회 요청
        * Then. 등록된 지하철 구간이 반영된 역 목록이 조회됨
        * When. 지하철 구간 삭제 요청
        * Then. 지하철 구간이 삭제됨.
        * When. 지하철 노선에 등록된 역 목록 조회 요청
        * Then. 삭제한 지하철 구간이 반영된 역 목록이 조회됨


### 2단계 - 경로 조회 기능
1. 최단 경로 조회 인수 테스트 만들기
   - [X] 출발역에서 도착역 까지 최단거리를 조회한다. 인수테스트 만들기
     - When. 출발역과 도착역을 요청한다.
     - Then. 최단거리의 지하철역과 총 거리가 조회된다.
   - [X] 예외상황에 대한 인수테스트를 만든다.
       - 출발역과 도착역이 같은 경우, 최단경로 조회가 실패한다.
         - When. 출발역과 도착역을 같은 역으로 요청한다.
         - Then. ~~최단 경로 조회가 실패한다.~~ 조회된 역이 요청한 역 1개이며, 거리가 0이다.
       - 존재하지 않은 출발역을 조회하는 경우, 최단경로 조회가 실패한다.
         - Given. 새로운 역을 하나 등록한다.
         - When. 노선에 연결되어 있지 않은 새로운 역을 출발역으로 하여 최단 경로를 조회한다.
         - Then. 최단 경로 조회가 실패한다.
       - 존재하지 않은 도착역을 조회하는 경우, 최단경로 조회가 실패한다.
         - Given. 새로운 역을 하나 등록한다.
         - When. 노선에 연결되어 있지 않은 새로운 역을 도착역으로 하여 최단 경로를 조회한다.
         - Then. 최단 경로 조회가 실패한다.
2. 최단 경로 조회 기능 구현하기
   - [X] 최단 경로를 조회하는 PathFinder 를 구현하며 내부에서 jgrapht 라이브러리를 이용한다.
     - PathFinder는 인터페이스로 구성하며 이를 구현한 구현체는 jgrapht 라이브러리를 이용하여 구현
     - https://jgrapht.org/guide/UserOverview#graph-algorithms
   - [X] 출발역에서 도착역까지 최단 루트(지하철역)을 구한다.
   - [X] 출발역에서 도착역까지 최단 거리(Distance)를 구한다. 


### 3단계 - 인증을 통한 기능구현
1. 토큰 발급 기능(로그인) 인수테스트 만들기
  - [X] 토큰 발급(로그인)을 검증하는 인수테스트 만들기 - AuthAcceptanceTest
  - [X] 토큰 발급(로그인) 예외 케이스에 대한 인수테스트 만들기
    - [X] 등록되지 않은 회원정보로 요청하는 경우 (로그인 실패)
    - [X] 유효하지 않은 토큰으로 요청하는 경우
  - [X] 이메일과 패스워드를 이용하여 요청 시 AccessToken 을 응답하는 기능 구현하기
   
2. 인증 - 내 정보 조회하기 기능 완성하기
  - [X] /member/me 요청 시 로그인 후 발급받은 토큰을 포함해서 요청하기
  - [X] MemberAcceptanceTest 내 정보 조회 인수테스트에 조회, 수정, 삭제 기능에 대한 인수테스트 작성하기

3. 인증 - 즐겨찾기 기능 완성하기
  - 인증을 포함하여 즐겨찾기 인수 테스트 작성하기
    - [X] 즐겨찾기 생성
      - Mockito를 이용한 Service Layer 테스트코드 작성
    - [X] 즐겨찾기 목록 조회
    - [X] 즐겨찾기 삭제
  - [X] 즐겨찾기 도메인 기능 개발 
  - 예외케이스에 대한 인수테스트 작성
    - [X] 존재하지 않는 역을 즐겨찾기 할 수 없다. 
    - [X] 내가 생성하지 않은 즐겨찾기는 삭제할 수 없다.
  - [X] 즐겨찾기 기능에 대한 통합 인수테스트 작성

### 4단계 - 요금조회

*(용어 기준 : 요금 = 운임료 = Fare)*
1. 경로 조회 시 거리 기준 요금 정보 포함하기
- [X]  지하철 경로 검색의 인수 조건 변경에 다른 인수 테스트 수정
- (추가) 지하철 경로 검색 시 거리 기준에 따른 요금 정보를 응답으로 포함한다.
- [X] 거리 기준에 따른 요금정보 처리 기능 개발 TDD
  - 거리 기준의 3가지 요금 타입 (**FareSectionType** : 타입(거리 시작 기준, 추가금 발생 단위 거리)))
    - 기본(10km 이내) : 1,250원 → **BASIC(0, 0)**
    - 10km 초과 ~ 50km 까지 구간 : 5km 당 100원 추가 → **SECTION_10_TO_50(10, 5)**
    - 50km 초과 ~  : 8km 당 100원 추가 → **SECTION_OVER_50(50, 8)**
- [X] 거리 기준의 요금정보 처리 모듈 (**FareCalculator**)
  - 금액 타입 (**FareType**)
    - 기본금액 : 1,250원
    - 기본금액 구간 초과 시 n Km 당 100 추가금
  

2. 노선별 추가 요금 정책 추가
- [X]  노선(Line) 도메인에 추가 운임료(**Fare**) 추가하기
  - 추가 운임료는 0 이상의 금액으로 설정가능하다.
- [X]  추가 운임료가 있는 노선을 이용할 경우 측정된 요금에 추가 운임료가 포함되어 총 금액이 산출된다.
  - 최단거리 경로의 Stations Collection을 바탕으로 구간 Section을 판단하고 Section의 노선정보를 이용하여 추가 운임료를 판단
- [X]  경로 중 추가 운임료가 있는 노선을 환승하여 이용할 경우 가장 높은 금액의 추가 운임료만 적용된다.

3. 연령별 할인 정책 추가
- [X]  노선 조회 시 로그인한 사용자 정보 포함하여 요청할 수 있도록 변경
- ~~[X]  비로그인 상태에서 노선 조회 시 Exception 처리~~
- [X] 비로그인 상태에서도 노선은 조회 가능하며, 비로그인인 경우 연령별 운임할인정책을 적용받을 수 없다.
- [X]  어린이(6이상 13미만) : 운임에서 350원을 공제한 금액의 50% 할인
- [X]  청소년(13이상 19미만) : 운임에서 350원을 공제한 금액의 20% 할인 
