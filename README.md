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

## 요구사항 정리

### 1단계 - 인수 테스트 기반 리팩터링

1. 요구사항
    - [x] LineService 리팩터링
    - [x] (선택사항) LineSectionAcceptanceTest 리팩터링
2. 목록정리
    - 인수 테스트 기반 리팩터링
        - 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD 리팩터링
        - Domain 옮길 로직을 찾기
            -[x] 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮기기
        - Domain 단위 테스트를 작성하기
            - [x] 지하철 구간 등록 요청(상행, 하행)
            - [x] 역 사이에 새로운 역 등록하는 경우
            - [x] 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
            - [x] 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음
            - [x] 역과 역사이에 중간역 삭제
            - [x] 지하철 노선 상행선 하행선 제거
            - [x] 지하철 구간이 1개인 경우 삭제 불가
            - [x] 지하철 노선보다 거리가 큰 경우 추가 불가
        - 로직 옮기기
            - 기존 로직 지우지 않고 새로운 로직을 만들어 수행
                - [x] 기존 로직 찾기
            - 정상 동작 확인 후 기존 로직 제거

### 2단계 - 경로 조회 기능

1. 요구사항
    - [x] 최단 경로 조회 인수 테스트 만들기
    - [x] 최단 경로 조회 기능 구현하기
2. 목록정리
    - [x] jgrapht 라이브러리 이용
    - [x] 외부 라이브러리의 인수 테스트로 테스트

### 3단계 - 인증을 통한 기능 구현

1. 요구사항
    - [x] 토큰 발급 기능(로그인) 인수 테스트 만들기
    - [x] 인증 - 내 정보 조회 기능 완성하기
    - [ ] 인증 - 즐겨 찾기 기능 완성하기
2. 목록정리
    - [x] 토큰 발급 인수 테스트
        - [x] 이메일과 패스워드를 이용하여 access token 응답하는 기능 구현
        - [x] AuthAcceptanceTest를 만족하도록 구현
        - [x] AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현
        - [x] 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리
    - [x] 내 정보 조회 기능
        - [x] MemberAccptanceTest 클래스의 manageMyInfo 메서드에 인수 테스트 추가
        - [x] 내 정보 조회, 수정, 삭제 기능 /members/me URI 요청으로 기능 구현
            - [x] 조회
            - [x] 수정
            - [x] 삭제
        - [x] 로그인 후 발급 받은 토큰을 포함해서 요청
    - [ ] 토큰을 통한 인증
        - [x] /members/me 요청 시 토큰을 확인하여 로그인 정보 받아오게 하기
        - [x] @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
    - [ ] 즐겨 찾기 기능 구현하기
        - [ ] 즐겨찾기 기능 완성
        - [ ] 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능 구현하기
