### 1단계 - 인수 테스트 기반 리팩터링
* [ ] LineService의 비즈니스 로직을 도메인으로 옮기기
* [ ] 한 번에 많은 부분을 고치지 않고 부분부분 리팩터링
* [ ] 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링

1. Domain으로 옮길 로직을 찾기
    * 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
    * 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
    * 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
    * SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
    * 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
    * 정상 동작 확인 후 기존 로직 제거

----------------------------------------------------
### 2단계 - 경로 조회 기능
* [ ] 최단 경로 조회 인수 테스트 만들기
* [ ] 최단 경로 조회 기능 구현하기


* 예외상황
  * 출발역과 도착역이 같은 경우
  * 출발역과 도착역이 연결되지 않은 경우
  * 존재하지 않는 출발역이나 도착역을 조회할 경우


* 힌트
  * jgrapht 라이브러리를 활용하면 간편하게 최단거리를 조회가능
  * 정점(vertext)과 간선(edge), 그리고 가중치 개념을 이용
    * 정점: 지하철역(Station)
    * 간선: 지하철역 연결정보(Section)
    * 가중치: 거리
  * 최단 거리 기준 조회 시 가중치를 거리로 설정 

----------------------------------------------------
### 3단계 - 인증을 통한 기능 구현
* [ ] 토큰 발급 기능 (로그인) 인수 테스트 만들기
* [ ] 인증 - 내 정보 조회 기능 완성하기
* [ ] 인증 - 즐겨 찾기 기능 완성하기

* 토큰 발급 인수 테스트
  * 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
  * AuthAcceptanceTest 인수 테스트 만들기
  * 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
  * AuthAcceptanceTest을 만족하도록 구현하면 됨
  * AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현하기
  * Bearer Auth 유효하지 않은 토큰 인수 테스트
    * 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리

* 내 정보 조회 기능
  * 인수 테스트
    * MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
    * 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
    * 로그인 후 발급 받은 토큰을 포함해서 요청 하기
  * 토큰을 통한 인증
    * /members/me 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
    * @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
    * 아래의 기능이 제대로 동작하도록 구현하기 
  * 즐겨 찾기 기능 구현하기
    * 즐겨찾기 기능을 완성하기
    * 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

----------------------------------------------------

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