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

<br>

---

### Step3
- [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
   - Feature: 로그인 기능
     - Scenario: 로그인을 시도한다.
       - Given 회원 등록되어 있음
       - When 로그인 요청
       - When 내 정보 조회 요청
       - Then 내 정보 조회 됨
- [x] 인증 - 내 정보 관련 기능 완성하기
   - Feature: 내 정보 기능
     - Scenario: 내 정보 관리
       - Given 회원 등록되어 있음
       - Given 로그인 되어있음
       - When 내 정보 조회
       - Then 내 정보 조회 됨
       - When 내 정보 수정
       - When 내 정보 조회
       - Then 수정된 정보로 표시 됨
       - When 내 정보 삭제
       - Then 내 정보 삭제 됨
- [x] 인증 - 즐겨 찾기 기능 완성하기
   - Feature: 즐겨찾기를 관리한다.
     - Background 
       - Given 지하철역 등록되어 있음
       - And 지하철 노선 등록되어 있음
       - And 지하철 노선에 지하철역 등록되어 있음
       - And 회원 등록되어 있음
       - And 로그인 되어있음
     - Scenario: 즐겨찾기를 관리
       - When 즐겨찾기 생성을 요청
       - Then 즐겨찾기 생성됨
       - When 즐겨찾기 목록 조회 요청
       - Then 즐겨찾기 목록 조회됨
       - When 즐겨찾기 삭제 요청
       - Then 즐겨찾기 삭제됨
       - When 즐겨찾기 목록 조회 요청
       - Then 즐겨찾기 빈 목록 조회됨

### Step2
- [x] 최단 경로 조회 인수 테스트 만들기
  - [x] FAIL! 출발역과 도착역이 같은 경우
  - [x] FAIL! 출발역과 도착역이 연결이 되어 있지 않은 경우
  - [x] FAIL! 존재하지 않은 출발역이나 도착역을 조회 할 경우
  - [x] 최단 경로 역 목록과 총 거리를 반환한다
- [x] 최단 경로 조회 기능 구현하기

### Step1
- [x] 인수 테스트 통합
  - [x] API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
  - [x] 반드시 하나의 시나리오로 통합할 필요는 없음, 하나 이상의 시나리오가 필요한 경우 여러개를 작성할 수 있음

- Feature: 지하철 구간 관련 기능
  - Background 
    - Given 지하철역 등록되어 있음
    - And 지하철 노선 등록되어 있음
    - And 지하철 노선에 지하철역 등록되어 있음
  - Scenario: 지하철 구간을 관리
    - 실패! When 구간 중복 등록 요청
    - 실패! Then 구간 등록 실패
    - 실패! When 존재하지 않는 역으로 구간 등록 요청
    - 실패! Then 구간 등록 실패
    - 실패! When 존재하지 않는 역 삭제 요청
    - 실패! Then 역 삭제 실패
    - 실패! When 역이 두개일 때 역 삭제 요청
    - 실패! Then 역 삭제 실패
    - When 지하철 구간 2개 등록 요청
    - When 지하철 노선에 등록된 역 목록 조회 요청
    - Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    - When 지하철 구간 2개 삭제 요청
    - When 지하철 노선에 등록된 역 목록 조회 요청
    - Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨

- [x] 인수 테스트 기반 리팩터링
  - [x] LineService의 비즈니스 로직을 도메인으로 옮기기
  - [x] 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
  - [x] 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

How To.
1. Domain으로 옮길 로직을 찾기
   - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   - 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   - SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거
