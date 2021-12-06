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

- [X] Distance 클래스 테스트 케이스 작성
- [X] Line 클래스 테스트 케이스 작성
- [X] Section 클래스 테스트 케이스 작성
- [X] Sections 클래스 테스트 케이스 작성

### 커멘트 사항 수정
- [X] HTTP api 호출 메서드 추출
- [X] 예외처리핸들러 공통 클래스(CommonExceptionHandler) 작성
- [X] 가독성 증가를 위하여 변수를 선언하여 작성
- [X] StationService가 아닌 StationRepository에 의존하도록 변경
- [X] LineService에서 LineResponse를 만드는 부분을 해당객체서 역할 이전
- [X] 읽기 전용 메서드에 `@Transactional(readOnly = true)` 추가
- [X] 함수 재활용을 통한 코드 리팩터링
- [X] 일급콜렉션 Sections를 만들고 Service Layer의 로직을 Sections으로 이전
- [X] 스트림의 반복 변수 it를 의미있는 변수명으로 변경
- [X] Optional을 안티패턴 제거 후 Optional답게 사용하기
- [X] 거리 변수 객체로 포장 하기 
- [X] 불필요한 AcceptanceTest를 상속부분 제거

### 미션 2 요구사항
- [X] 경로를 의미하는 클래스(Path) 작성
- [X] 최소경로를 구하는 역할을 담당하는 클래스(PathFinder) 작성
- [X] 최소경로 조회를 인수테스트하는 클래스(PathAcceptanceTest) 작성
- [X] 경로조회 URL처리를 담당하는 클래스 PathController의 작성
- [X] 경로조회 데이터 처리를 담당하는 클래스 PathService의 작성
- [X] 경로조회 관련 응답메시지를 담는 클래스(PathReponse) 작성

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
