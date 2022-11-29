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

---

# 🚀 1단계 - 인수 테스트 기반 리팩터링
## 요구사항
- [x] LineService 리팩터링
- [x] LineSectionAcceptanceTest 리팩터링

## 작업사항
- LineAcceptanceTest 테스트 목록 
  - 지하철 노선 생성
    - 기존의 존재하는 노선명으로 생성시 예외
  - 지하철 노선 목록 조회
  - 지하철 노선 조회
  - 지하철 노선 수정
  - 지하철 노선 제거
- LineService 기능 목록
  - saveLine - 노선 저장
  - findLines - 노선 목록 조회
  - findLineById - 아이디로 지하철 노선 단일 조회
  - findLineResponseById - 아이디로 지하철 노선 저회 후 LineResponse로 반환
  - updateLine - 노선 이름과 색 업데이트
  - deleteLineById - 노선 삭제
  - addLineStation - 구간 추가 -> Line으로 비즈니스 로직 이동
  - removeLineStation - 구간 삭제 -> Line으로 비즈니스 로직 이동
  - ~~getStations - 노선에 추가된 역 조회~~
  - ~~findUpStation - 상행 종점 조회~~
- Line 기능 목록
  - addLineStation - 구간 추가
  - removeLineStation - 구간 삭제
  - getStations - 노선에 추가된 역 조회
  - findUpStation - 상행 종점 조회

### 요구사항 설명
#### 인수 테스트 기반 리팩터링
1. Domain으로 옮길 로직을 찾기
   - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   - 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   - SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거

#### 인수 테스트 통합
- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
