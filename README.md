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

## Step1 - 인수 테스트 기반 리팩터링

#### 요구사항 1
- LineService 리팩터링
#### 요구사항 2
- (선택) LineSectionAcceptanceTest 리팩터링
#### 구현 리스트
- [x] 기존 서비스 복사하기 ( strangler pattern )
- [x] Domain으로 옮길 로직을 찾기
    - [x] [1]saveLine메소드의 getStations.
    - [x] [2]findLines메소드의 persistLines.stream().
    - [x] [3]findLineResponseById메소드의 getStations.
    - [x] [4]addLineStation메소드의 line.getSections().stream(), line.getSections().add
    - [x] [5]removeLineStation메소드의 line.getSections().stream(), line.getSections().add, ifPresent
    - [x] [6]getStations메소드의 line.getSections().stream()
    - [x] [7]findUpStation메소드의 line.getSections().stream()
- [x] Domain의 단위 테스트를 작성하기
    - [x] [1] ~ [7] 단위 테스트 작성
- [x] 로직을 옮기기
    - [x] 새로운 로직 만들어서 수행
    - [x] 정상 동작 확인 후 기존 로직 제거 
- [x] 인수 테스트 통합 리팩토링
