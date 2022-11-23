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


# Step1 요구사항
- [ ] LineService 리팩터링
  1. Domain으로 옮길 로직을 찾기
     스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정  <br>
  2. Domain의 단위 테스트를 작성하기 <br>
     서비스 레이어에서 옮겨 올 로직의 기능을 테스트  <br>
     SectionsTest나 LineTest 클래스가 생성될 수 있음  <br>
  3. 로직을 옮기기
     기존 로직을 지우지 말고 새로운 로직을 만들어 수행 <br>
     정상 동작 확인 후 기존 로직 제거 <br>

- [ ]  (선택) LineSectionAcceptanceTest 리팩터링 