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

# TODO List

## 1단계 - 인수 테스트 기반 리팩터링
-[x] 인수테스트 리팩터링
    -[x] 노선 구간 관리의 시나리오를 만들고, 시나리오 대로 동작하는지 테스트
-[x] LineService 리팩터링
    -[x] 노선 역 정렬 로직 Line 객체 안으로
    -[x] 노선 저장 리팩터링
    -[x] 노선 조회 리팩터링
    -[x] 노선 목록 조회 리팩터링
    -[x] 노선 구간 추가 리팩터링
    -[x] 노선 구간 삭제 리팩터링
## 2단계 - 경로 조회 기능
-[x] 최단 경로 조회 인수 테스트 만들기
-[x] 최단 경로 조회 기능 구현하기
  -[x] 최단 경로를 찾아내는 역할을 하는 도메인 클래스 구현
  -[x] 도메인 클래스를 사용해서 최단 경로 조회 API 구현
## 3단계 - 인증을 통한 기능 구현
-[x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
  -[x] AuthAcceptanceTest 완성시키기
-[x] 인증 - 내 정보 관리 기능 완성하기
  -[x] MemberAcceptanceTest 완성시키기
  -[x] /members/me 요청 시 토큰 처리 해주기
-[ ] 인증 - 즐겨 찾기 기능 완성하기
  -[x] 즐겨찾기 시나리오 만들고 인수테스트 작성하기
  -[x] 도메인 설계 후 inside out 방식으로 TDD 하기
    -[x] 즐겨찾기 생성
    -[x] 즐겨찾기 목록 조회
    -[x] 즐겨찾기 삭제
## 4단계 - 요금 조회
-[x] 경로 거리 기준 요금을 함께 응답하도록 최단 경로 인수 테스트 수정
-[ ] 최단 경로 조회 도메인 코드 수정 로직 TDD 하기
  -[x] 경로 거리에 따른 요금 계산 로직 추가
  -[x] 노선의 추가 요금에 따라 발생되는 요금 계산하기
  -[x] 로그인 멤버에 따라 할인 정책 추가하기

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
