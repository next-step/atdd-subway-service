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


## Step1 - 인수 테스트 기반 리팩터링

### 요구사항
- [X] LineSectionAcceptanceTest 리팩터링
- LineService 리팩터링

### 인수 조건 예시
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

## Step2 - 경로 조회 기능

### 요구사항
- [X] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

### 외부 라이브러리 테스트
- [X] 노선 하나의 지하철역에서 최단거리를 구한다.
- [X] 노선 여러개의 지하철역에서 최단거리를 구한다.
- [X] 출발역과 도착역이 같은 경우를 확인한다.
- [X] 출발역과 도착역이 연결이 되어 있지 않은 경우를 확인한다.
- [X] 존재하지 않은 출발역이나 도착역을 조회 할 경우를 확인한다.

### 도메인 to-do 리스트

- `경로 저장소(PathRepository)`
    - [X] 모든 경로 구간들을 조회할 수 있다.
    - [X] 경로 지하철역 id로 경로 지하철역을 조회할 수 있다.
    
- `경로 탐색기(PathFinder)`
    - [X] 노선 하나의 지하철역에서 최단거리를 구한다.
    - [X] 노선 여러개의 지하철역에서 최단거리를 구한다.
    - [X] 출발역과 도착역이 같은 경우를 예외처리 한다.
    - [X] 출발역과 도착역이 연결이 되어 있지 않은 경우 예외처리 한다.
    - [X] 존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리 한다.
    
- `경로(Path)`
    - 경로 지하철역 리스트와 총 거리로 구성.
    
- `경로 지하철역(PathStation)`
    - 지하철역의 id, name, createAt로 구성.

- `경로 구간들(PathSections)`
    - [X] 경로 구간들의 지하철역의 목록을 조화할 수 있다.
    
- `경로 구간(PathSection)`
    - [X] 경로 구간의 지하철역을 조회할 수 있다.

## Step3 - 인증을 통한 기능 구현

### 요구사항
- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기

### 토큰 발급 기능
- 인수테스트
- [X] 토큰 발급 성공
    - given
        - 회원 등록되어 있음
    - when
        - 로그인 요청
    - then
        - 로그인 성공됨
- [X] 토큰 발급 실패: 아이디 또는 비밀번호가 틀린 경우
    - given
        - 회원 등록되어 있음
    - when
        - 잘못된 로그인 요청 (아이디 또는 비밀번호가 틀림)
    - then
        - 로그인 실패됨

### 내 정보 조회 기능 
- 인수테스트
- [X] 나의 정보를 관리한다.
    - given
        - 회원 등록되어 있음
        - and 회원 로그인되어 있음
    - when
        나의 정보 조회 요청 (유효하지 않은 토큰)
    - then
        - 나의 정보 조회 실패됨
    - when
        - 나의 정보 조회 요청
    - then
        - 나의 정보가 조회됨
    - when
        - 나의 정보 수정 요청
    - then
        - 나의 정보 수정됨
    - when
        - 나의 정보 삭제 요청
    - then
        - 나의 정보 삭제됨

### 즐겨찾기 기능
- 인수테스트
- [X] 즐겨찾기를 관리한다.
    - given
        - 지하철역 등록되어 있음
        - and 지하철 노선 등록되어 있음
        - and 지하철 노선에 지하철역 등록되어 있음
        - and 회원 등록되어 있음
        - and 로그인 되어있음
    - when 
        - 즐겨찾기 생성을 요청
    - then 
        - 즐겨찾기 생성됨
    - when 
        - 즐겨찾기 목록 조회 요청
    - then 
        - 즐겨찾기 목록 조회됨
    - when 
        - 즐겨찾기 삭제 요청
    - then 
        - 즐겨찾기 삭제됨
