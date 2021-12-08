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


### [1단계] - 인수 테스트 기반 리팩터링

- [X] LineSectionAcceptanceTest 리팩터링
    - [X] 인수 테스트 통합
        ```
            Feature: 지하철 구간 관련 기능
            
            Background
            Given 지하철역 등록되어 있음
            And 지하철 노선 등록되어 있음
            And 지하철 노선에 지하철역 등록되어 있음
            
            Scenario: 지하철 구간을 관리
            When 지하철 구간 등록 요청
            Then 지하철 구간 등록됨
            When 지하철 노선에 등록된 역 목록 조회 요청
            Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
            When 지하철 구간 삭제 요청
            Then 지하철 구간 삭제됨
            When 지하철 노선에 등록된 역 목록 조회 요청
            Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        ```
    - [X] 인수 테스트 리펙터링
- [X] LineService 리팩터링
    - [X] 인수 테스트 기반 리팩터링
        - Domain으로 옮길 로직 찾기
        - Domain의 단위 테스트 작성하기
        - 로직 옮기기

### [2단계] - 경로 조회 기능
- [X] 최단 경로 조회 인수 테스트 만들기
  - [X] 성공 케이스
  - [X] 출발역과 도착역이 같은 경우
  - [X] 출발역과 도착역이 연결이 되어 있지 않은 경우
  - [X] 존재하지 않은 출발역이나 도착역을 조회 할 경우
  - [X] 구간에 지하철역이 존재하지 않을 경우
- [X] 최단 경로 조회 기능 구현하기

### [3단계] - 인증을 통한 기능 구현
- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
    - [X] 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
    - [X] `AuthAcceptanceTest` 인수 테스트 만들기
- [X] 인증 - 내 정보 조회 기능 완성하기
- [X] 인증 - 즐겨 찾기 기능 완성하기
  ```
      Feature: 즐겨찾기를 관리한다.
    
      Background 
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
        And 회원 등록되어 있음
        And 로그인 되어있음
    
      Scenario: 즐겨찾기를 관리
        When 즐겨찾기 생성을 요청
        Then 즐겨찾기 생성됨
        When 즐겨찾기 목록 조회 요청
        Then 즐겨찾기 목록 조회됨
        When 즐겨찾기 삭제 요청
        Then 즐겨찾기 삭제됨
  ```
- [X] 즐겨찾기 추가 예외 처리
    - [X] 지하철 역에 등록되지 않는 지하철 역을 추가했을 경우(등록)
    - [X] 유효하지 않는 경로를 선택했을 경우(등록)
- [X] 도메인 테스트
- [X] 테스트 리펙터링
- [X] 코드 리펙터링

### 4단계 - 요금 조회
- [X] 경로 조회 시 거리 기준 요금 정보 포함하기
    - [X] 기본운임(10km이내) : 1,250원
    - [X] 이용 거리 초과 시 추가운임 부과
        - [X] 10km초과∼50km까지(5km마다 100원)
        - [X] 50km초과 시 (8km마다 100원)
- [ ] 노선별 추가 요금 정책 추가
    - [X] 노선에 추가 요금 필드 추가
    - [X] 노선 등록시 추가 요금 등록
    - [ ] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    - [ ] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
- [ ] 연령별 할인 정책 추가
    - [ ] 청소년: 운임에서 350원을 공제한 금액의 20%할인
    - [ ] 어린이: 운임에서 350원을 공제한 금액의 50%할인
```
- 청소년: 13세 이상~19세 미만
- 어린이: 6세 이상~ 13세 미만
```