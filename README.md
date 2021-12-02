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

### 미션 1 요구사항
- [X] LineService 리팩터링
  - [X] Domain으로 옮길 로직 찾기
  - [X] save로직을 정적팩토리 메소드로 옮기고 리팩터링 
  - [X] getStations을 Line으로 옮기고 리팩터링 
  - [X] findUpStation을 Line으로 옮기고 리팩터링
  - [X] removeLineStation의 내부 로직을 Line으로 옮기고 리팩터링
  - [X] addLineStation 내부를 함수추출법 이용하여 리팩터링

- [X] LineSectionAcceptanceTest 리팩터링
  - [X] 생략가능한 클래스명 삭제(StationAcceptanceTest, LineAcceptanceTest)
  - [X] `지하철역_등록`에서 `자하철_구간_등록`으로 변경
  - [X] 시나리오 호름 테스트 작성(performScenario)

- [X] Section, LineResponse, Line 클래스 정적팩토리 메서드 작성

- [X] Line 클래스 테스트 케이스 작성
- [X] Section 클래스 테스트 케이스 작성

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
