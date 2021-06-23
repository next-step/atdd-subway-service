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


## 1단계 요구사항

* 시나리오 테스트 분석하고 새로운 시나리오를 작성하여 요구사항에 정리한다.
    * Feature: 지하철 구간 관련 기능

    * Background
        * Given 지하철역 등록되어 있음
        * And 지하철 노선 등록되어 있음
        * And 지하철 노선에 지하철역 등록되어 있음

    *  Scenario: 지하철 구간을 순서 없이 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2.  지하철 노선을 등록한다.
            1. When 지하철 양재역 - 정자역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간을 제일 상행을 제외하고 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2. 지하철 노선을 제외한다.
            1. When 지하철 강남역 제외 요청
            2. Then 지하철 구간 제외됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간을 제일 하행을 제외하고 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2. 지하철 노선을 제외한다.
            1. When 지하철 광교역 구간 제외 요청
            2. Then 지하철 구간 제외됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간 중 중간을 제외하고 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
               And 지하철 광교역 - 정자역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2. 지하철 노선을 제외한다.
            1. When 지하철 광교역 구간 제외 요청
            2. Then 지하철 구간 제외됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간 등록 실패를 한다.
        1. 지하철 노선에 이미 등록되어 있는 역을 등록한다.
            1.  When 지하철 강남역 - 광교역 구간 등록 요청
            2. Then 지하철 노선에 지하철역 등록 실패
        2. 지하철 노선에 등록되지 않은 역을 기준으로 등록한다.
            1. When 지하철 노선에 연결되지 않는 구간 양재역 - 정자역 새로 등록
            2. Then 지하철 노선에 지하철역 등록 실패
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회
        4. 지하철 구간 등록시 거리가 큰 값으로 등록한다.
            1. When 지하철 노선에 강남역 - 정자역 새로 등록 요청. 요청시 거리를 100으로 한다.
            2. Then 지하철 노선에 지하철역 등록 실패

    *  Scenario: 지하철 구간 제외 실패를 한다.
        1. 지하철 노선에 없는 구간을 제외한다.
            1. When 지하철 노선에 연결되지 않는 구간 정자역 제외 요청
            2. Then 지하철 노선에 지하철역 제외 실패
        2. 지하철 노선 구간이 하나만 있는 경우에 구간을 제외한다..
            1. When 지하철 노선에 연결된 구간 강남역 제외 요청
            2. Then 지하철 노선에 지하철역 제외 실패

* 계획한 시나리오를 LineSectionAcceptanceTest when과 then으로 작성한다.
* LineService를 분석하고 어떤 도메인으로 옮길 수 있을지 요구사항에 정리한다.
    * saveLine의 메서드를 sections 일급 컬렉션으로 생성하여 처리
    * findLines 메서드에서 응답을 Line 도메인에서 생성되도록 수정
    * findLineResponseById 메서드 Line으로 이동
    * addLines 메서드에서 station 유효성 검사를 station 메서드에서 진행하도록
    * 구간에 대한 검사는 sections 에서
* LineService의 로직을 단계별로 분리한다. 분리 시 도메인으로 새로운 코드를 작성하고 테스트 완료 후 기존 로직을 제거한다. 이 후 테스트를 다시 실행하여 모두 통과 되는지 확인한다.
* LineService 로직 분기를 단계별로 진행하고, 완료 후 리팩토링을 진행한다.
    * 각 도메인의 역할에 맞는 행동을 하고 있는지 체크
    * 행동에 대한 메시지를 정확히 전달하고 있는지 체크
    * import 순서나 사용하지 않는 메서드가 있는지 체크