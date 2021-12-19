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

## 1단계 구현 목록
- LineSectionAcceptanceTest 리팩터링
  - [x] 새로운 시나리오 형식의 테스트 추가
  - [x] 기존 테스트 삭제
- LineService 리팩터링
  - [x] Line.getOrderedStations
  - [x] Line.addSection
  - [x] Sections.add
  - [x] Line.removeSection
  - [x] Sections.remove
  - [x] Sections.getOrderedStations

## 2단계 구현 목록
- [x] 커스텀 예외 fillinStackTrace 오버라이드
- [x] line.addSection의 유효성검사 및 로직 순서 수정
- [x] Section.Equals, HashCode가 비즈니스키를 사용하도록 수정
- [x] Sections.findUpStation의 null 반환 수정
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 인수테스트 통과할 정도로만 구현
- 최단 경로 조회 기능 구현하기
  - 유닛 테스트 작성 (도메인으로 넣으면서 리팩토링)
    - [x] 성공
    - [x] 출발역과 도착역이 같으면 예외
    - [x] 출발역과 도착역이 연결되어 있지 않은 경우 예외
    - [x] 존재하지 않는 출발역이나 도착역을 조회하는 경우 예외

## 3단계 구현 목록
- [x] 토큰 발급 기능 (로그인) 인수테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
  - [x] 인수 테스트
  - [x] 구현
- 인증 - 즐겨 찾기 기능 완성하기
  - [x] 인수 테스트 추가
  - [ ] 구현