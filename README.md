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


## 요구사항 
*[x] LineSectionAcceptanceTest 리팩터링
*[x] LineService 리팩터링
*[x] 최단 경로 조회 인수 테스트 만들기
*[x] 최단 경로 조회 기능 구현하기
*[ ] 토큰 발급 기능(로그인) 인수 테스트 만들기
*[ ] 인증 - 내 정보 조회 기능 완성하기
*[ ] 인증 - 즐겨찾기 기능 완성하기


## 작업 목록
1. Domain으로 옮길 로직을 찾기<br>
   스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정<br>
   객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기<br>
   서비스 레이어에서 옮겨 올 로직의 기능을 테스트<br>
   SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기<br>
   기존 로직을 지우지 말고 새로운 로직을 만들어 수행<br>
   정상 동작 확인 후 기존 로직 제거

## 시나리오
### Feature: 지하철 노선 관련 기능
#### Scenario: 지하철 노선의 최단 경로를 조회한다.
* Given 지하철 노선과 지하철역이 등록되어있다.
* When 지하철 노선의 최단 경로 조회를 요청한다.
* Then 지하철 노선의 최단 경로가 조회된다.

#### Scenario: 출발역과 도착역이 같게 최단 경로를 조회한다.
* Given 지하철 노선과 지하철역이 등록되어있다.
* When 출발역과 도착역이 같게 최단 경로 조회를 요청한다.
* Then 지하철 노선의 최단 경로가 조회가 실패한다.

#### Scenario: 출발역과 도착역이 연결되지 않게 최단 경로를 조회한다.
* Given 지하철 노선과 지하철역이 등록되어있다.
* When 연결되지 않은 출발역과 도착역으로 최단 경로 조회를 요청한다.
* Then 지하철 노선의 최단 경로가 조회가 실패한다.

#### Scenario: 존재하지 않은 출발역으로 최단 경로를 조회한다.
* Given 지하철 노선과 지하철역이 등록되어있다.
* When 존재하지 않은 출발역으로 최단 경로 조회를 요청한다.
* Then 지하철 노선의 최단 경로가 조회가 실패한다.

#### Scenario: 존재하지 않은 도착역으로 최단 경로를 조회한다.
* Given 지하철 노선과 지하철역이 등록되어있다.
* When 존재하지 않은 도착역으로 최단 경로 조회를 요청한다.
* Then 지하철 노선의 최단 경로가 조회가 실패한다.

### Feature: 로그인 기능
#### Scenario: 로그인을 시도한다.
* Given 회원 등록되어 있음
* When 로그인 요청
* Then 로그인이 성공한다.

#### Scenario: 등록되지 않은 이메일로 로그인을 시도한다.
* When 등록되지 않은 이메일로 로그인 요청
* Then 로그인이 실패한다. ( 토큰 발급 실패 )

#### Scenario: 등록되지 않은 패스워드로 로그인을 시도한다.
* Given 회원 등록되어 있음
* When 등록되지 않은 패스워드로 로그인 요청
* Then 로그인이 실패한다. ( 토큰 발급 실패 )

#### Scenario: 유효하지 않은 토큰으로 정보 조회 
* Given 회원 등록되어 있음 ( 토큰 발급 )
* When 유효하지 않은 토큰으로 내 정보 조회 요청
* Then 조회가 실패한다.

### Feature: 내 정보 관리 기능 관련 전체 인수 테스트
#### Scenario: 내 정보를 관리 한다.
* Given 회원 등록되어 있음
* When 로그인 요청
* Then 로그인 성공
* When 내 정보 조회 요청
* Then 내 정보 조회 성공
* When 내 정보 업데이트 요청
* Then 내 정보 업데이트 성공
* When 내 정보 삭제 요청
* Then 내 정보 삭제 성공
* When 로그인 요청
* Then 로그인 실패



## 기능 목록
1. 등록된 지하철 노선에서 최단 경로 찾는 기능
2. 토큰 기반으로 로그인 멤버를 가져오는 기능


## API 명세

* GET /paths 
  * Request
    1. long "source" // 출발역
    2. long "target" // 도착역
    <br>
  * Response
    ```json
    {
      "stations": 
      [
        {
          "id": 1,
          "name": "강남역",
          "createdAt": "2020-05-09T23:54:12.007"
        },
        {
          "id": 6,
          "name": "양재역",
          "createdAt": "2020-05-10T23:54:12.007"
        }
      ],
      "distance": 10
    }
    ```
