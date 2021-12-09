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

## 구간 관련 기능
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

## 경로 관련 시나리오
Feature: 경로 관련 기능  
    Background  
        Given 지하철역 등록되어 있음  
        And 지하철 노선 등록되어 있음  
        And 지하철 노선에 지하철역 등록되어 있음  
        And 지하철 구간 등록되어 있음  
    Scenario: 지하철 최단경로 조회  
        When 지하철 경로를 지하철 출발역, 도착역으로 경로 조회 요청  
        Then 최단 경로를 반영한 경로(역들)가 조회됨  
        When 출발역과 도착역이 같은 경로 조회 요청  
        Then 최단_경로_조회_실패  
        When 출발역과 도착역이 연결이 되어 있지 않은 경로 조회 요청  
        Then 최단_경로_조회_실패  
        When 존재하지 않은 출발역이나 도착역의 경로 조회 요청  
        Then 최단_경로_조회_실패    

## 인증 기능 구현

Feature: 로그인 기능   
Scenario: 로그인을 시도한다.   
Given 회원 등록되어 있음   
When 로그인 요청   
Then 로그인 됨  

## 할 일
- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기
- [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [ ] 인증 - 내 정보 조회 기능 완성하기
- [ ] 인증 - 즐겨 찾기 기능 완성하기