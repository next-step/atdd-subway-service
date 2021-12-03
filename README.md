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

## 🚩 구현 기능 목록
### 인수테스트 기반 리팩터링
- [x] LineSectionAcceptanceTest 리팩터링
    - [x] API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 한다.
```
Feature: 지하철 구간 관련 기능
Given:
  지하철 역 등록되어 있다.
  노선 	등록되어 있다.
  지하철 노선에 1구간(지하철역 2개) 등록되어 있다.

Scenario: 지하철 노선 구간 관리
When: 노선에 새로운 구간 추가 요청
Then: 노선에 구간 추가됨
When: 지하철 노선에 등록된 역 목록 조회 요청
Then: 추가한 역을 포함한 역목록이 조회됨
When: 하행역에 대해 구간 삭제 요청
Then: 구간 삭제됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then: 삭제한  역이 포함되지 않은 역목록 조회됨

```  
- [ ] LineService 리팩터링 - 비지니스 로직을 도메인으로 옮기기
    - [ ] 도메인으로 옮길 로직 찾기 - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고 도메인으로 옮긴다.
    - [ ] 도메인의 단위 테스트 작성
    - [ ] 로직 옮기기

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
