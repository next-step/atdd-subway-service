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

## 🚀 4단계 - 요금 조회

요구사항

- [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
- [ ] 노선별 추가 요금 정책 추가
  - 기본운임(10㎞ 이내) : 기본운임 1,250원
  - 이용 거리초과 시 추가운임 부과
    - 10km초과∼50km까지(5km마다 100원)
    - 50km초과 시 (8km마다 100원)
- [ ] 연령별 할인 정책 추가
  - 청소년: 운임에서 350원을 공제한 금액의 20%할인
  - 어린이: 운임에서 350원을 공제한 금액의 50%할인
  ```
  - 청소년: 13세 이상~19세 미만
  - 어린이: 6세 이상~ 13세 미만
  ```
  
  ```
  Feature: 지하철 경로 검색
  
    Scenario: 두 역의 최단 거리 경로를 조회
      Given 지하철역이 등록되어있음
      And 지하철 노선이 등록되어있음
      And 지하철 노선에 지하철역이 등록되어있음
      When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
      Then 최단 거리 경로를 응답
      And 총 거리도 함께 응답함
      And ** 지하철 이용 요금도 함께 응답함 **
  ```

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
