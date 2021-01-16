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


## Step1 - 인수 테스트 기반 리팩터링
### 인수 테스트 통합
- 시나리오를 검증하는 테스트로 리팩터링 
```
Feature - 지하철 구간 관련 기능
    Background
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
    Scenario: 지하철 구간을 관리
        When 지하철 구간 등록 요청
        Then 지하철 구간 등록됨(실패)
        When 지하철 구간 등록 요청
        Then 지하철 구간 등록됨(성공)
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        When 지하철 구간 삭제 요청
        Then 지하철 구간 삭제됨(성공)
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        When 지하철 구간 삭제 요청
        Then 지하철 구간 삭제됨(실패)
```
- 인수 테스트 기반 리팩터링
    - LineService 비지니스 로직을 Domain으로 이동
    - 한번에 많은 부분보단 나눠서 부분부분 리팩터링
    - 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링
    - 순서
        - 1. Domain으로 옮길 로직 찾기
        - 2. Domain 단위 테스트 작성
        - 3. 로직 이동
                - 기존 로직은 두고 새로운 로직을 만들어 수행
                - 나중에 기존 로직을 삭제 

## Step2 - 경로 조희 기능
### 요구사항
- 최단 경로 조회 인수 테스트 만들기
   - PathAcceptanceTest(인수테스트) -> PathServiceTest(기능테스트) 순차적으로 구현
   
- 최단 경로 조회 기능 구현하기 
    - 시작역과 도착역의 최단거리를 구하는 기능

## Step3 - 인증을 통한 기능 구현
### 요구사항
* 토큰 발급 기능 (로그인) 인수 테스트 만들기
    * AuthAcceptanceTest
    ```
        Feature: 로그인 기능
            Scenario: 로그인을 시도한다.
                Given 회원 등록되어 있음
                When  로그인 요청
                Then  로그인 됨
    ```
    * 이메일과 패스워드를 이용하여 요청하기
    * access token을 응답하는 기능
    * Bearer Auth 유효하지 않은 토큰 인수 테스트
        * 유효하지 않는 토큰으로 /members/me 요청을 보낼 경우 예외처리
        
* 인증 - 내 정보 조회 기능 완성하기
    * MemberAcceptanceTest
        * /members/me URI 요청으로 동작을 검증 
        * 로그인 후 발급 받은 토큰을 포함해서 요청
        ```
            Feature: 회원 정보 관리 기능
                Scenario: 나의 정보를 관리한다
                    Given 로그인이 되어있음
                    When  나의 정보 조회를 요청
                    Then  나의 정보가 조회됨
    
                    Given 로그인이 되어있음
                    When  나의 정보 수정을 요청
                    Then  나의 정보가 수정됨        
                    
                    Given 로그인이 되어있음
                    When  나의 정보 삭제를 요청
                    Then  나의 정보가 삭제됨 
        ```
    * 토큰을 통한 인증
        * /members/me URI 요청 시, 토큰을 확인하여 로그인 정보를 받아옴
        * @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용
        * 조회, 수정, 삭제 기능이 제대로 동작하도록 구현

* 인증 - 즐겨 찾기 기능 완성하기 


