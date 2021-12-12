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



## 인수테스트 시나리오
- 지하철 노선
    ```
    Feature: 지하철 노선 기능
    
      Background
        Given 지하철역 등록되어 있음
      
      Scenario: 지하철 구간을 관리
        When 지하철 노선 등록 요청
        Then 지하철 노선 등록됨
        When 지하철 노선 목록 조회 요청
        Then 등록한 지하철 노선 목록이 조회됨
        When 지하철 노선 수정 요청
        Then 등록한 지하철 노선 수정됨  
        When 지하철 노선 삭제 요청
        Then 지하철 노선 삭제됨
    ```

- 지하철 구간
    ```
    Feature: 지하철 구간 기능
    
      Background
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
      
      Scenario: 지하철 구간 정상 기능
        When 지하철 노선에 여러개의 역을 순서 상관 없이 등록 요청
        Then 지하철 구간 등록됨
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        When 지하철 구간 삭제 요청
        Then 지하철 구간 삭제됨
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
  
      Scenario: 지하철 구간 예외 발생
        When 노선에 이미 등록되어 있는 역을 등록 요청
        Then 지하철 구간 등록 실패됨
        When 지하철 노선에 등록되지 않은 역을 등록 요청
        Then 지하철 구간 등록 실패됨
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 등록한 지하철 구간이 반영되지 않은 역 목록이 조회됨
        When 등록된 지하철역이 두개일 때 지하철 구간 삭제 요청
        Then 지하철 구간 삭제 실패됨
        When 지하철 노선에 등록된 역 목록 조회 요청
        Then 삭제한 지하철 구간이 반영되지 않은 역 목록이 조회됨
    ```

- 경로 조회
    ```
    Feature: 경로 조회 기능
    
      Background
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
      
      Scenario: 최단 경로 조회 정상 기능
        Given 출발역과 도착역이 등록되어 있음
        And 출발역과 도착역이 같지 않음
        And 출발역과 도착역이 연결이 되어 있음
        When 최단 경로 조회 요청
        Then 최단 경로 조회됨
  
      Scenario: 최단 경로 조회 예외 발생
        Given 출발역이나 도착역이 등록되어 있지 않음
        Or 출발역과 도착역이 같음
        Or 출발역과 도착역이 연결이 되어 있지 않음
        When 최단 경로 조회 요청
        Then 최단 경로 조회 실패됨
    ```

- 로그인
    ```
    Feature: 로그인 기능

      Scenario: 로그인 정상 기능
        Given 회원 등록되어 있음
        And 로그인 정보 일치함 
        When 로그인 요청
        Then 로그인 성공

      Scenario: 로그인 예외 발생
        Given 회원 등록되어 있지 않음
        Or 로그인 정보 일치하지 않음
        When 로그인 요청
        Then 로그인 실패
    ```

- 회원
    ```
    Feature: 회원 기능

      Scenario: 회원 정보 관리 정상 기능
        Given 회원 등록되어 있음
        When 회원 정보 조회 요청
        Then 회원 정보 조회됨
        When 회원 정보 수정 요청
        Then 회원 정보 수정됨
        When 회원 정보 삭제 요청
        Then 회원 정보 삭제됨

      Scenario: 내 정보 관리 정상 기능
        Given 내 정보 등록되어 있음
        When 로그인 요청
        Then 토큰 반환됨
        When 토큰으로 내 정보 조회 요청
        Then 내 정보 조회됨
        When 토큰으로 내 정보 수정 요청
        Then 내 정보 수정됨
        When 토큰으로 내 정보 삭제 요청
        Then 내 정보 삭제됨
    ```

- 즐겨찾기
    ```
    Feature: 즐겨찾기 기능

      Background 
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
        And 회원 등록되어 있음
    
      Scenario: 즐겨찾기 관리 정상 기능
        Given 로그인 되어있음
        And 유효한 토큰 사용
        When 즐겨찾기 생성을 요청
        Then 즐겨찾기 생성됨
        When 즐겨찾기 목록 조회 요청
        Then 즐겨찾기 목록 조회됨
        When 즐겨찾기 삭제 요청
        Then 즐겨찾기 삭제됨

      Scenario: 즐겨찾기 관리 정상 기능
        Given 로그인 되어있지 않음
        Or 유효하지 않은 토큰 사용
        Or 출발역과 도착역이 같음
        When 즐겨찾기 생성을 요청
        Then 즐겨찾기 생성 실패됨
        When 즐겨찾기 목록 조회 요청
        Then 즐겨찾기 목록 조회 실패됨
        When 즐겨찾기 삭제 요청
        Then 즐겨찾기 삭제 실패됨
    ```
