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

## 🚩 구현 기능 목록
### 인수테스트 기반 리팩터링
- [x] LineSectionAcceptanceTest 리팩터링
    - [x] API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 한다.
```
Feature: 지하철 구간 관련 기능
Given:
  지하철 역 등록되어 있다.
  노선 	등록되어 있다.
  지하철 노선에 1구간(지하철역 2개) 등록되어 있다.

Scenario: 지하철 노선 구간 관리
When: 노선에 새로운 구간 추가 요청
Then: 노선에 구간 추가됨
When: 지하철 노선에 등록된 역 목록 조회 요청
Then: 추가한 역을 포함한 역목록이 조회됨
When: 하행역에 대해 구간 삭제 요청
Then: 구간 삭제됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then: 삭제한  역이 포함되지 않은 역목록 조회됨

```  
- [x] LineService 리팩터링 - 비지니스 로직을 도메인으로 옮기기
    - [x] 도메인으로 옮길 로직 찾기 - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고 도메인으로 옮긴다.
        - [x] getStations
        - [x] List<StationResponse> -> StationResponses 일급컬렉션
        - [x] addLineStation 로직 -> Line, Sections 클래스..
        - [x] removeLineStation 로직 -> Line, Sections 클래스로..
    - [x] 도메인의 단위 테스트 작성
    - [x] 로직 옮기기 - 도메인 리팩터링
        - [x]  distance wrapping 하기
        - [x] sections 일급컬렉션으로 변경
        - [x] 연관관계 편의 메서드는. Section 쪽으로..
        - [x] 기본 생성자 접근 제한 (protected)

### 경로 조회 기능
- [x] 최단 경로 조회 인수 테스트 만들기
    - [x] 최단 경로 인수 테스트 시나리오
```
Feature: 지하철 경로 조회
Given:
  지하철 역 등록되어 있다.
  노선 등록되어 있다.
  노선에 지하철 역 등록되어 있다.

Scenario: 지하철 최단 경로 조회
When: 최단 경로 조회 요청
Then: 최단 경로의 지하철역 목록 조회됨
```  
- [x] 최단 경로 조회 기능 구현하기
    - [x] 경로 조회 도메인 테스트 케이스 작성
    - [x] 경로 조회 도메인 구현
    - [x] 예외 케이스 테스트 케이스 작성
    - [x] 예외 케이스 처리
        - [x] 출발역과 도착역이 같은 경우
        - [x] 출발역과 도착역이 연결되어 있지 않은 경우
        - [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우


### 인증을 통한 기능 구현
- [ ] 토큰 발급 기능 (로그인) 인수테스트 만들기
```
Feature: 인증 기능

Background:
  Given: 회원 등록되어 있음. email@email.com/password

Scenario: 로그인을 시도한다.
  When: 로그인 요청
  Then: 로그인 됨

Scenario: 이메일 혹은 비밀번호 달라서 로그인 시도 실패
  When: 로그인 요청 shinmj@email.com/password
  Then: 로그인 실패
  When: 로그인 요청 email@email.com/password11
  Then: 로그인 실패

Scenario: 유효하지 않은 토큰으로 내정보 조회 
  When: 내정보 조회 요청 Bearer invalidToken
  Then: 조회 실패
```
- [ ] 인증. - 내 정보 조회 기능 완성하기
    - [ ] 인수 테스트 작성
```
Feature: 내정보 조회 기능

Scenario: 나의 정보를 관리한다. (조회, 수정, 삭제)
  Given: 회원 등록되어 있음.
    And: 로그인 되어있음. (token)
  When: 나의 정보 조회 요청
  Then: 나의 정보 조회됨
  When: 나의 정보 수정 요청
  Then: 나의 정보 수정됨
  When: 나의 정보 삭제 요청
  Then: 나의 정보 삭제됨
```
- [ ] 토큰을 통한 인증 구현
    - [ ] 토큰 유효성 검증 후 사용자 정보 입력 (`@AuthenticationPrincipal` , `AuthenticationPrincipalArgumentResolver`)
    - [ ] 기능에 어노테이션 달기 -> 기능 구현 확인  

- [ ] 인증 - 즐겨 찾기 기능 완성하기
    - [ ]  인수 테스트
```
Feature: 즐겨찾기를 관리한다.

Background:
 Given: 지하철 역 등록되어 있음
   And: 지하철 노선 등록되어 있음
   And: 지하철 노선에 지하철 역 등록되어 있음
   And: 회원 등록되어 있음
   And: 로그인 되어 있음

Scenario: 즐겨찾기를 관리한다.
  When: 즐겨 찾기 생성을 요청
  Then: 즐겨 찾기 생성됨
  When: 즐겨 찾기 목록 조회 요청
  Then: 즐겨 찾기 목록 조회됨
  When: 즐겨 찾기 삭제 요청
  Then: 즐겨 찾기 삭제됨
```
- [ ] 기능 구현 TDD
    - [ ] 구간으로 관리  From- To
    - [ ] `member` 와 다대일 단방향 연관관계, 즐겨찾기가 n !   



## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
