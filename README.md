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

## [STEP1] 인수 테스트 기반 리팩터링
* LineSectionAcceptanceTest 리팩터링
  * 인수 테스트 통합 
    * 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
```
Scenario: 지하철 구간을 관리
    When 지하철 구간 등록 요청
    Then 지하철 구간 등록됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    When 지하철 구간 삭제 요청
    Then 지하철 구간 삭제됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```

* LineService 리팩터링
  * Domain으로 옮길 로직을 찾기 
    * 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 이동
      * addLineStation : findLineById, findStationById 제외한 로직 이동
      * removeLineStation : findLineById, findStationById 제외한 로직 이동
      
  * Domain의 단위 테스트를 작성하기 
    * 이동되는 로직의 기능을 테스트
    * SectionsTest나 LineTest 클래스가 생성
    * addLineStation
      * 새로운 역을 등록할 경우, 새 구간이 추가되어야한다.
      * 새로운 역을 등록할 경우, 추가한 위치로 정렬되어야한다.
      * 상행역 기준으로 중간에 새로운 역을 등록할 경우, 기존 하행역의 상행역이 새로운 역으로 변경되어야한다.
      * 상행역 기준으로 중간에 새로운 역을 등록할 경우, 기존 하행역과 새로운역의 거리는 기존 거리에서 신규 구간 거리를 뺀 값과 같아야한다.
      * 하행역 기준으로 중간에 새로운 역을 등록할 경우, 기존 상행역의 하행역이 새로운 역으로 변경되어야한다.
      * 하행역 기준으로 중간에 새로운 역을 등록할 경우, 기존 상행역과 새로운역의 거리는 기존 거리에서 신규 구간 거리를 뺀 값과 같아야한다.      
      * 신규 구간의 거리가 기존 구간의 거리보다 크거나 같으면 RuntimeException 을 Throw 해야한다.
      * 새로운 구간의 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 RuntimeException 을 Throw 해야한다.
      * 새로운 구간의 상행역과 하행역 둘 중 하나도 노선에 포함되어있지 않다면 RuntimeException 을 Throw 해야한다.
    * removeLineStation
      * 역을 제거하는 경우, 구간이 제거 되어야한다.
      * 역을 제거하는 경우, 제거된 구간을 제외하고 이전 노선의 순서대로 정렬되어야한다.
      * 제거되는 역이 상행역인 구간을 '상행제거 구간', 제거되는 역이 하행역인 구간을 '하행제거 구간' 으로 한다.
      * 중간의 역을 제거할 경우, '상행제거 구간' 의 상행역을 '하행제거 구간' 의 상행역으로 변경해야한다.
      * 중간의 역을 제거할 경우, '상행제거 구간' 의 거리는 '하행제거 구간' 의 거리와 더해진 값이어야 한다.
      * 구간이 하나인 노선에서 마지막 구간을 제거하려 할 때 RuntimeException 을 Throw 해야한다.
      * 노선에 등록되어있지 않은 역을 제거하려 할 때, RuntimeException 을 Throw 해야한다.
    
  * 로직을 옮기기 
    * 기존 로직을 지우지 말고 새로운 로직을 만들어 수행 
    * 정상동작 확인 후 기존 로직 제거

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
