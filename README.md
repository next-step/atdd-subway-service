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

## 1단계  - 인수 테스트 기반 리팩터링 요구사항
* [x] LineSectionAcceptanceTest 리팩터링
~~~
Feature: 지하철 구간 관련 기능

Background
Given 지하철역 등록되어 있음
And 지하철 노선 등록되어 있음
And 지하철 노선에 지하철역 등록되어 있음

Scenario: 지하철 구간 관리 성공 검증
When 지하철 구간 등록 요청
Then 지하철 구간 등록됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
When 지하철 구간 삭제 요청
Then 지하철 구간 삭제됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨

Scenario: 지하철 구간 관리 실패 검증
When 지하철 중복된 구간 등록 요청
Then 지하철 구간 등록 실패됨
When 지하철 노선에 등록되지 않은 역을 등록요청
Then 지하철 구간 등록 실패됨
When 지하철 노선에 등록된 지하철역이 두개일 때 한 역을 삭제 요청
Then 지하철 구간 삭제 실패됨
~~~
* [x] LineService 리팩터링
  * [x] getStations() -> Line 도메인으로 이동
  * [x] Sections 일급콜렉션 생성
  * [x] 구역 추가 기능 적절한 도메인으로 이동
  * [x] 구역 제거 기능 적절한 도메인으로 이동
  * [x] RestControllerAdvice, ExceptionHandler 를 사용하여 에러 처리
