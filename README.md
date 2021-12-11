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


## 1단계
#### 기능 요구사항
- [X] LineSectionAcceptanceTest 리팩터링
    - [X] 지하철 구간을 관리하는 테스트로 통합
    - [X] static 메서드 분리
- [X] LineService 리팩터링
    - [X] 서비스 내의 비즈니스 로직 line 도메인으로 이동
    - [X] 거리 역할 클래스 추가
    - [X] 구간 클래스 도메인 로직 추가
    - [X] 구간 목록 역할 클래스 추가
    - [X] 역 목록 역할 클래스 추가
    - [X] 역 응답 목록 클래스 추가
- [X] 공통 에러처리

#### 코드리뷰
- [X] `NoSuchElementException` 대신 용도에 맞는 새로운 Exception 생성

## 2단계
#### 기능 요구사항
- [ ] 최단 경로 조회 인수 테스트 만들기
    - [ ] 정상적으로 경로 조회 인수 테스트 생성 ((Outside In)
    - [ ] 출발역과 도착역이 같은 경우 인수 테스트 생성 (Inside Out)
    - [ ] 출발역과 도착역이 연결이 되어 있지 않은 경우 인수 테스트 생성 (Inside Out)
    - [ ] 존재하지 않은 출발역이나 도착역을 조회 할 경우 인수 테스트 생성 (Inside Out)
- [ ] 최단 경로 조회 기능 구현하기
    - [ ] 정상적으로 경로 조회 (Outside In)
        - [ ] PathService 코드 구현
        - [ ] PathFinder 코드 구현
    - [ ] 출발역과 도착역이 같은 경우 (Inside Out)
    - [ ] 출발역과 도착역이 연결이 되어 있지 않은 경우 (Inside Out)
    - [ ] 존재하지 않은 출발역이나 도착역을 조회 할 경우 (Inside Out)


## 참고
- [JPA 사용시 @Embedded 주의 사항](https://jojoldu.tistory.com/559)

