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

## 3단계 요구사항
* [ ] 토큰 발급 기능 (로그인) 인수 테스트 만들기
  * `Feature` 로그인 기능
    * `Scenario` 로그인을 시도한다.
      * `Given` 회원 등록되어 있음
      * `When` 로그인 요청
      * `Then` 로그인 됨
      * `When` 등록되어 있지 않은 이메일 로그인 요청
      * `Then` 로그인 실패됨 (`AuthorizationException` 발생)
      * `When` 잘못된 비밀번호로 로그인 요청
      * `Then` 로그인 실패됨 (`AuthorizationException` 발생)
      * `When` 잘못된 토큰으로 로그인 요청
      * `Then` 로그인 실패됨 (`AuthorizationException` 발생)
* [ ] 인증 - 내 정보 조회 기능 완성하기
* [ ] 인증 - 즐겨 찾기 기능 완성하기

## 2단계 요구사항
* [X] 최단 경로 조회 인수 테스트 만들기
  * 최단거리 조회 
  * 출발역과 도착역이 같은 경우
  * 출발역과 도착역이 연결이 되어 있지 않은 경우
  * 존재하지 않은 출발역이나 도착역을 조회 할 경우
* [X] 최단 경로 조회 기능 구현하기
  * 경로 찾기 예외 사항
    * 출발역과 도착역이 같을 경우 예외 발생
    * 노선에 출발역 또는 도착역이 없을 경우 예외 발생
    * 출발역과 도착역이 연결되어 있지 않은 경우 예외 발생
  

## 1단계 요구사항
* [X] `LineSectionAcceptanceTest` 리팩터링
* [X] `LineService` 리팩터링
### 요구사항 분리 
* [X] 노선 저장 `saveLine`
  * 지하철역 조회 쿼리 분리
  * 지하철 역 목록 기능 분리
* [X] 노선 목록 `findLines`
  * 노선 목록 조회 쿼리 분리
  * 지하철 역 목록 기능 분리
* [X] 지하철 목록 수정 기능 `updateLine`
  * 노선 정보 쿼리 공통으로 분리
* [X] 노선에 지하철역 추가 `addLineStation`
  * 지하철역 조회 쿼리 분리 
  * 유효성 체크 쿼리 분리
  * 지하철역 추가 기능 분리
* [X] 노선에 지하철역 삭제 `removeLineStation`
  * 지하철역 조회 쿼리 분리
  * 지하철역 삭제 기능 분리
* [X] 노선 구간 정보 기능 분리
* [X] 상행선 찾기 기능 분리


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




