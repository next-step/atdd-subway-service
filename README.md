<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
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

## 🚀 1단계 - 인수 테스트 기반 리팩터링

### Requirment

인수 테스트 기반 리팩터링
- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

1. Domain으로 옮길 로직을 찾기
- 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
- 객체지향 생활체조를 참고

2. Domain의 단위 테스트를 작성하기
- 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
- SectionsTest나 LineTest 클래스가 생성될 수 있음

3. 로직을 옮기기
- 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
- 정상 동작 확인 후 기존 로직 제거

### TODO

[ ] Refactor LineService 

  - addLineStation
    - [ ] 단위테스트
    - [ ] Refactor

  - getStations()
    - [ ] 단위테스트
    - [ ] Refactor
    
  - removeLineStation
    - [ ] 단위테스트
    - [ ] Refactor

[ ] Refactor LineSectionAcceptanceTest 

  - [ ] Add Scenario Test





 
