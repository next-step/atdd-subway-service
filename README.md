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

***

# 기능 요구 사항
## Step1
* LineSectionAcceptanceTest 리팩터링
    * 시나리오, 흐름을 검증하는 테스트로 리팩터링 [O]
* LineService 리팩터링
    * LineService의 비즈니스 로직을 도메인으로 이동 [O]
    * Domain의 단위 테스트를 작성 [O]
## Step2
* 최단 경로 조회 인수 테스트 작성 [O]
* 최단 경로 조회 기능 구현
    * outside-in : service test mokito 이용하여 구현 [O]
    * inside-out : 외부 라이브러리 이용하여 domain 구현 [O]
## Step3
* 토큰 발급 기능 (로그인) 인수 테스트 만들기
* 인증 - 내 정보 조회 기능 완성하기
* 인증 - 즐겨 찾기 기능 완성하기    
