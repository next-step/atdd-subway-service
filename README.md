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

## Step1 

1. LineSectionAcceptanceTest refactoring
    - 화면 시나리오에 맞춘 인수테스트 재작성
2. LineService -> Domain으로 옮길 로직 찾기
    - getStations, addSection, removeLineStation
3. Domain에 추가될 로직에 대한 단위 테스트 작성
4. LineService의 로직을 Domain으로 refactoring
5. 테스트 확인 및 코드 점검

## Step2

1. Step1 피드백 반영
2. 최단 경로 조회 인수 테스트 만들기
    - 테스트 픽스쳐 작성
    - 성공 및 예외 인수테스트 작성
3. 최단 경로 조회 기능 구현
    - PathFinder 도메인 설계 후 도메인 테스트 작성 및 기능 구현
    - 나머지 최단 경로 조회 기능 구현(Controller, Service)                 