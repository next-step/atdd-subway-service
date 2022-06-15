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


## 1단계 - 인수 테스트 기반 리팩터링

- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링

1. [x] Domain으로 옮길 로직을 찾기
- 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 이동
- 객체 지향 생활 체조를 참고

2. [x] Domain의 단위 테스트를 작성하기
- 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
- SectionsTest, LineTest 클래스 생성

3. [x] 로직을 옮기기
- 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
- 정상 동작 확인 후 기존 로직 제거

4. [x] 인수 테스트 통합
- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

## 2단계 - 경로 조회 기능

1. [x] 최단 경로 조회 인수 테스트 만들기
- PathAcceptanceTest.java

2. [x] 최단 경로 조회 기능 구현하기
- PathFinder.java

3. [x] 최단 경로 라이브러리 사용하기
- jgrapht 라이브러리를 활용하면 간편하게 최단 거리를 조회할 수 있음
- 정점(vertext)과 간선(edge), 그리고 가중치 개념을 이용
- 최단 거리 기준 조회 시 가중치를 거리로 설정

4. [x] 최단 경로 조회 예외 처리하기
- 출발역과 도착역이 같은 경우
- 출발역과 도착역이 연결이 되어 있지 않은 경우
- 존재하지 않은 출발역이나 도착역을 조회 할 경우

## 3단계 - 인증을 통한 기능 구현

1. [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 토큰 발급(로그인)을 검증하는 인수 테스트 생성
- `AuthAcceptanceTest`에 인수 테스트 추가

2. [x] 인증 - 내 정보 조회 기능 완성하기
- 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
- 로그인 후 발급 받은 토큰을 포함하여 요청
- `MemberAcceptanceTest`에 인수 테스트 추가
- `/members/me` 요청 시 토큰을 확인하여 로그인 정보 받기
- `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver` 활용

3. [x] 인증 - 즐겨 찾기 기능 완성하기
- 즐겨 찾기 추가, 조회, 삭제 기능을 `/favorites` 라는 URI 요청으로 동작하도록 검증
- 즐겨 찾기 기능 구현
- 로그인 후 발급 받은 토큰을 포함하여 요청
- `FavoriteAcceptanceTest` 인수 테스트 추가
- `/favorites` 요청 시 토큰을 확인하여 로그인 정보 받기
- `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver` 활용

