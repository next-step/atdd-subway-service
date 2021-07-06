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

## TODO

### 경로 조회 기능
- 출발역과 도착역을 요청하면, 최단거리의 구간을 반환한다.
- 예외 상황
    - 출발역과 도착역이 같은 경우
    - 출발역과 도착역이 연결이 되어 있지 않은 경우
    - 존재하지 않은 출발역이나 도착역을 조회 할 경우

```
Feature: 지하철 경로 검색

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리도 함께 응답함
    And 지하철 이용 요금도 함께 응답함
```
    
### 인증을 통한 기능 구현
- 로그인 인수 테스트
```
Feature: 로그인 기능
           
 Scenario : 로그인을 시도한다.
   Given 회원 등록되어 있음
   When 로그인 요청
   Then 로그인 됨
   
 Scenario : 로그인을 시도한다.
   When 등록되지 않은 회원 로그인 요청
   Then 로그인 실패
```    

- 내 정보 조회 기능
```
Feature: 나의 정보를 관리한다.

  Background 
    Given 나의 정보는 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음
    
  Scenario: 나의 정보를 조회한다
    When 나의 정보를 요청한다
    Then 나의 정보가 조회됨
    
  Scenario: 나의 정보를 수정한다
    When 나의 정보를 수정요청한다
    Then 나의 정보가 수정됨
    
  Scenario: 나의 정보를 삭제한다
    When 나의 정보를 삭제요청한다
    Then 나의 정보가 삭제됨
```
- 즐겨찾기 기능
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

### 요금 조회
- 경로 조회 시 거리 기준 요금 정보 포함하기
```
* 기본운임(10㎞ 이내) : 기본운임 1,250원
* 이용 거리초과 시 추가운임 부과
   - 10km초과∼50km까지(5km마다 100원)
   - 50km초과 시 (8km마다 100원)
```
- 노선별 추가 요금 정책 추가
- 로그인 한 회원의 경우 연령별 할인 정책 추가

## 참고
[그래프 알고리즘 라이브러리](https://jgrapht.org/guide/UserOverview#graph-algorithms)
[REST Assured Authentication](https://www.baeldung.com/rest-assured-authentication)

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
