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

## 1단계 요구사항
* [X] `LineSectionAcceptanceTest` 리팩터링
* [X] `LineService` 리팩터링
### 요구사항 분리 
* [X] 노선 저장 `saveLine`
  * 지하철역 조회 쿼리 분리
  * 지하철 역 목록 기능 분리
* [X] 노선 목록 `findLines`
  * 노선 목록 조회 쿼리 분리
  * 지하철 역 목록 기능 분리
* [X] 지하철 목록 수정 기능 `updateLine`
  * 노선 정보 쿼리 공통으로 분리
* [X] 노선에 지하철역 추가 `addLineStation`
  * 지하철역 조회 쿼리 분리 
  * 유효성 체크 쿼리 분리
  * 지하철역 추가 기능 분리
* [X] 노선에 지하철역 삭제 `removeLineStation`
  * 지하철역 조회 쿼리 분리
  * 지하철역 삭제 기능 분리
* [X] 노선 구간 정보 기능 분리
* [X] 상행선 찾기 기능 분리


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




