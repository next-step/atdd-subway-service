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


---

# 요구사항 분석

## 1단계 - 인수 테스트 기반 리팩터링
* LineSectionAcceptanceTest 리팩터링
* LineService 리팩터링
* API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기

### 인수 테스트 기반 리팩터링
* LineService의 비즈니스 로직을 도메인으로 옮기기
* 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
* 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

### 1. Domain으로 옮길 로직을 찾기
* 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
* 객체지향 생활체조를 참고

### 2. Domain의 단위 테스트를 작성하기
* 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
* SectionsTest나 LineTest 클래스가 생성될 수 있음

### 3. 로직을 옮기기
* 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
* 정상 동작 확인 후 기존 로직 제거

### Todo
* 지하철 구간 관리 통합 시나리오 인수 테스트 작성
  * When 지하철 구간 등록 요청
  * Then 지하철 구간 등록됨
  * When 지하철 구간 사이에 구간 등록 요청
  * Then 지하철 구간 등록됨
  * Then 기존 구간의 길이와 새로 생긴 구간으로 나누어진 구간들의 길이의 합이 같음 
  * When 지하철 노선에 등록된 역 목록 조회 요청
  * Then 등록한 지하철 구간이 반영된 역 목록이 순서대로 정렬되어 조회됨
  * When 종점역이 포함되지 않은 지하철 구간 삭제 요청
  * Then 지하철 구간 삭제됨
  * When 종점역이 포함된 지하철 구간 삭제 요청
  * Then 지하철 구간 삭제됨  
  * When 지하철 노선에 등록된 역 목록 조회 요청
  * Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
* 각 인수테스트 요청의 비즈니스 로직을 수행하는 서비스의 책임을 가져갈 도메인 테스트 작성
  * 노선에 새로운 구간 등록
    * 기존 구간이 있을 경우 구간이 나눠짐
    * 기존 구간이 있을 경우 기존 구간의 길이에서 새로운 구간의 길이 차감
  * 노선에 지하철역 삭제
    * 노선의 구간 목록에서 지하철역이 포함된 구간들이 합쳐지며 길이 합쳐짐
    * 삭제 대상 지하철역이 종점일 경우 옆의 역이 새로운 종점이 됨
  * 노선의 구간을 정렬하여 지하철역 목록 응답
* 도메인 구현
  * 노선(Line) 도메인
    * 구간 등록
      * 구간 사이에 등록시 기존 구간 길이 차감
    * 구간 삭제
    * 노선의 구간을 정렬하여 지하철역 목록 응답
  * 구간(Section)
    * 구간 길이 차감
    * 구간 길이 합쳐짐

## 2단계 - 경로 조회 기능

### 요구사항
* 최단 경로 조회 인수 테스트 만들기
* 최단 경로 조회 기능 구현하기

### TODO
* 최단 경로 조회 인수 테스트
  * 최단 경로 조회
  * 예외 테스트 - 출발역과 도착역이 같은 경우
  * 예외 테스트 - 출발역과 도착역이 연결되어 있지 않은 경우
  * 예외 테스트 - 존재하지 않은 출발역이나 도착역을 조회할 경우
* 최단 경로 조회 구현
  * 최단 경로 조회
  * 예외 처리 - 출발역과 도착역이 같은 경우
  * 예외 처리 - 출발역과 도착역이 연결되어 있지 않은 경우
  * 예외 처리 - 존재하지 않은 출발역이나 도착역을 조회할 경우
  
## 3단계 - 인증을 통한 기능 구현

### 요구사항
* 토큰 발급 기능 (로그인) 인수 테스트 만들기
* 인증 - 내 정보 조회 기능 완성하기
* 인증 - 즐겨 찾기 기능 완성하기

### TODO  
* 토큰 발급 (로그인) 인수 테스트
* 토큰 발급 구현
* 내 정보 조회 인수 테스트
* 정보 조회 도메인 테스트
* 정보 조회 구현
* 즐겨 찾기 인수 테스트
* 즐겨 찾기 조회 도메인 테스트
* 즐겨 찾기 구현
