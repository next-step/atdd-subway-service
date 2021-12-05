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

## step3 요구사항
- [ ] 토큰 발급 기능 인수 테스트
- [ ] 인증
  - [ ] 내 정보 조회
  - [ ] 즐겨 찾기

## step2 요구사항
- [X] 최단 경로 조회 인수 테스트 만들기
- [X] 최단 경로 조회 기능 구현하기
- [X] 예외 처리
  - [X] 출발역과 도착역이 같은 경우
  - [X] 출발역과 도착역이 연결이 되어 있지 않은 경우
  - [X] 존재하지 않우 출발역이나 도착역을 조회한 경우

## step1 요구사항
- [X] LineSectionAcceptanceTest 리팩터링
  - [X] 구간 인수 테스트 통합
  - [X] RestAssured http 메소 유틸로 추출
- [X] LineService 리팩터링 
  - [X] Sections 모델 추가
  - [X] 노선에서 역 목록 불러오기
  - [X] 역 추가 함수
  - [X] 역 삭제 함수
  - [X] 응답 모델에 엔티티로부터 응답 모델로 변경 함수 추가