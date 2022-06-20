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

<br>

------
##기능 목록
###Step 1. 인수 테스트 기반 리팩터링
- [X] LineService 리팩터링
- [X] LineSectionAcceptanceTest 리팩터링

###Step 2. 경로 조회 기능
- [X] 최단 경로 조회 인수 테스트 만들기
  - [X] 정상 시나리오 : 조회 성공
  - [X] 예외 시나리오
    - [X] 출발역과 도착역이 같은 경우 조회 불가
    - [X] 출발역과 도착역이 연결이 되어 있지 않은 경우 조회 불가
    - [X] 존재하지 않은 출발역이나 도착역을 조회 할 경우 조회 불가
- [X] 최단 경로 조회 기능 구현하기
  - [X] jgrapht 라이브러리 사용하여 최단 경로 조회
  - [X] 최단 경로 지하철역 리스트 조회
  - [X] 최단 경로 거리 조회

###Step 3. 인증을 통한 기능
- [X] 토큰 발급 기능 (로그인)
  - [X] 인수 테스트
    - [X] 정상 시나리오 
      - [X] 로그인 성공
      - [X] 유효한 토큰으로 회원정보 조회
    - [X] 예외 시나리오
      - [X] 사용자 정보 맞지 않아 로그인 실패
      - [X] 유효하지 않은 토큰으로 회원정보 조회
  - [X] 기능 목록
    - [X] 이메일과 패스워드를 이용하여 요청 시 access token을 응답
- [X] 내 정보 관리 기능
  - [X] 인수 테스트
    - [X] 회원 정보 조회 시나리오 : 생성/조회/수정/삭제
    - [X] 내정보 조회 시나리오 : 조회/수정/삭제
  - [X] 기능 목록
    - [X] 정보 조회
    - [X] 정보 수정
    - [X] 정보 삭제
    - [X] /members/me 요청 시 토큰을 확인하여 정보 처리
- [X] 즐겨찾기 기능
  - [X] 인수 테스트
    - [X] 정상 시나리오 : 생성/조회/삭제
    - [X] 예외 시나리오
      - [X] 즐겨찾기 생성을 중복 요청시 실패
      - [X] 존재하지 않는 역으로 즐겨찾기 생성시 실패
      - [X] 동일한 역을 출발지와 목적지로 즐겨찾기 생성시 실패
      - [X] 잘못된 토큰으로 즐겨찾기 생성/삭제시 실패
  - [X] 기능 목록
    - [X] 즐겨찾기 생성
    - [X] 즐겨찾기 조회
    - [X] 즐겨찾기 삭제
    
###Step 4. 요금 조회
- 거리별 요금 정책 
  - 기본운임(10㎞ 이내) : 기본운임 1,250원 
  - 이용 거리초과 시 추가운임 부과 
    - 10km초과∼50km까지(5km마다 100원)
    - 50km초과 시 (8km마다 100원)
- 로그인 사용자의 경우 연령별 요금 할인 적용
  - 청소년:
    - 13세 이상~19세 미만
    - 운임에서 350원을 공제한 금액의 20%할인 
  - 어린이:
    - 6세 이상~ 13세 미만
    - 운임에서 350원을 공제한 금액의 50%할인

- [X] 요금 조회 기능
  - [X] 지하철 경로 검색 시 요금도 함께 응답
  - [X] 거리 기반 추가요금 적용
  - [X] 노선별 추가요금 적용
  - [X] 로그인 사용자의 경우 연령별 요금 할인 적용
- [X] 요금 조회 인수 테스트
  - [X] 비회원 조회
  - [X] 어른 회원 조회
  - [X] 청소년 회원 조회
  - [X] 어린이 회원 조회