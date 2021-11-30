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


### [1단계] - 인수 테스트 기반 리팩터링

- [ ] LineSectionAcceptanceTest 리팩터링
    - [X] 인수 테스트 통합
        ```
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
        ```
    - [ ] 인수 테스트 리펙터링

- [ ] LineService 리팩터링
    - [ ] 인수 테스트 기반 리팩터링
        - Domain으로 옮길 로직 찾기
        - Domain의 단위 테스트 작성하기
        - 로직 옮기기