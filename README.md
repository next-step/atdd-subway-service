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


# 1단계
인수테스트 기반 리팩토링
- Domain으로 옮기기
    - addLineStation, removeLineStation, getStations Line.java로 옮기기
    
- Domain의 단위 테스트를 작성하기
    - SectionsTest, LineTest
    
- 로직을 옮기기
    - newAddLineStation, newRemoveLineStation, newgetStation 한벌 새로 만들어서 작업
  
# 2단계 
최단거리 구하기
예외사항
  - 출발역과 도착역이 같은 경우
  - 출발역과 도착역이 연결이 되어 있지 않은 경우
  - 존재하지 않은 출발역이나 도착역을 조회 할 경우

# 3단계
토큰 발급 인수 테스트 만들기
  - 토큰 발급 검증하는 인수 테스트 만들기
    
```
Feature:로그인 기능
Scenario : 로그인을 시도한다.
  Given 회원 등록되어 있음
  When 로그인 요청
  Then 로그인 됨
```

Bearer Auth 유효하지 않은 토큰 인수 테스트
    
내 정보 조회 기능
  - 토큰을 통한 인증 @AuthenticationPrincipal @AuthenticationPrincipalArgumentResolver을 활용

즐겨 찾기 기능 구현

```
Feature : 즐겨찾기를 관리한다.
Background 
  Given 지하철역 등록되어 있음
  And 지하철 노선 등록되어 있음
  And 지하철 노선에 지하철역 등록되어 있음
  And 회원 등록되어 있음
  And 로그인 되어있음
  
Scenario : 즐겨찾기를 관리
  When 즐겨찾기 생성을 요청
  Then 즐겨찾기 생성됨
  When 즐겨찾기 목록 조회 요청
  Then 즐겨찾기 목록 조회됨
  When 즐겨찾기 삭제 요청
  Then 즐겨찾기 삭제됨
```

# 4단계

## 거리별 요금 정책
10km 초과 시 5km마다 100원
50km 초과 시 8km 마다 100원

## 노선별 추가 요금 정책
노선 경로별 가장 높은 금액의 추가 요금만 적용

## 연령별 요금 할인 적용
청소년(13세 이상~19세 미만) 기본 요금 : 720원, 추가요금 20% 할인

어린이(6세 이상 ~ 13세 미만) 기본 요금 : 450원, 추가요금 50% 할인

- ### 예시

  | 거리 | 어른 | 청소년 | 어린이 |
  | --- | --- | --- | --- |
  | 10km까지 | 1,250 | 720 | 450 |
  | 15km까지 | 1,350 | 800 | 500 |
  | 20km까지 | 1,450 | 880 | 550 |
  | 25km까지 | 1,550 | 960 | 600 |
  | 30km까지 | 1750 | 1,120 | 700 |