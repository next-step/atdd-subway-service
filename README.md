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

## 요구사항 분석

### 경로 조회 기능 구현

1. 최단 경로 조회 인수 테스트
   *  최단 경로 라이브러리 학습
   *  외부 라이브러리 테스트 -> 구현하는 로직 검증(지하철 경로 탐색)
   *  인수 테스트 시나리오 작성
    ```ruby
    Feature: 지하철 경로 탐색 기능
    
      Background: 지하철, 노선 등록
        Given 지하철역 등록되어 있음지(출발지, 목적지)
        And 지하철 노선 등록되어 있음(테스트를 위해 여러 노선)
        And 지하철 노선에 지하철역 등록되어 있음
        And 지하철 노선에 구간 등록되어 있음
    
      Scenario: 지하철 경로 탐색
        When 출발역와 도착역 경로 요청
        Then 연결된 한 노선의 최단거리 조회
        Then 포함된 여러 노선의 구간 거리를 비교하여 최단거리 조회
     ```
2. 최단 경로 조회 기능 구현
   * [x] 출발역과 도착역이 같은 경우 (RuntimeException)
   * [x] 출발역과 도착역이 연결이 되어 있지 않은 경우 (NothingException)
   * [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우 (NothingException)

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
