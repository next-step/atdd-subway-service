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

## 1단계 - 인수 테스트 기반 리팩터링 요구사항
### 요구 사항
- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링

## 2단계 - 경로 조회 기능
### 요구 사항
- [X] 최단 경로 조회 인수 테스트 만들기
- [X] 최단 경로 조회 기능 구현하기

## 3단계 - 인증을 통한 기능 구현
### 요구 사항
- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
  - [X] 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리
- [X] 인증 - 내 정보 조회 기능 완성하기
  - [X] 내 정보 조회 기능 인수 테스트 작성
- [X] 인증 - 즐겨 찾기 기능 완성하기
  - [X] 즐겨 찾기 기능 인수 테스트 작성
  - [X] 즐겨 찾기 도메인 추가 및 API 구현
  
## 4단계 - 요금 조회
### 요구 사항
- [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
  - 거리별 요금 정책
    - 기본운임(10km이내) : 1250원
    - 이용 거리초과 시 추가운임
      - 10km초과 ~ 50km까지 (5km마다 100원)
      - 50km초과 시 (8km마다 100원)
- [ ] 노선별 추가 요금 정책 추가
  - 노선에 추가 요금 필드 추가
  - 추가요금 노선이용시 측정 요금에 추가요금 부과
  - 경로 중 추가요금이 있는 노선 환승시 가장 높은 금액의 추가요금만 적용
- [ ] 연령별 할인 정책 추가
  - 청소년 : 운임에서 350원을 공제한 금액의 20% 할인 / 13세 이상 ~ 19세 미만
  - 어린이 : 운임에서 350원을 공제한 금액의 50% 할인 / 6세 이상 ~ 13세 미만


