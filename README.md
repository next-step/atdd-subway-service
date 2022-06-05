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

---

### 1단계 - 인수테스트 기반 리팩터링
 1. LineService 리팩터링
    - [] 도메인으로 옮길 비즈니스 로직을 확인하고 옮기기
      - [X] Line 도메인 리팩토링
      - [X] Section 도메인 리팩토링
      - [X] Station 도메인 리팩토링
      - [] LineService에서 도메인으로 옮겨올 비즈니스 로직 리팩토링
    - [] 옮겨진 비즈니스 로직을 확인하는 도메인 테스트 코드 작성

 2. LineSectionAcceptanceTest 리팩터링
    - [X] AcceptanceTest 리팩토링 : 공통적으로 사용될 수 있는 부분 추출
      - [X] LineAcceptanceTest 기준 리팩토링 진행
      - [X] LineSectionAcceptTest 기준 리팩토링 진행
    - [X] 테스트코드와 테스트에서 사용되는 메소드 분리
    - [X] 통합 인수테스트 작성 및 패스
      - 지하철 구간을 관리
        * When. 지하철 구간 등록 요청
        * Then. 지하철 구간 등록됨.
        * When. 지하철 노선에 등록된 역 목록 조회 요청
        * Then. 등록된 지하철 구간이 반영된 역 목록이 조회됨
        * When. 지하철 구간 삭제 요청
        * Then. 지하철 구간이 삭제됨.
        * When. 지하철 노선에 등록된 역 목록 조회 요청
        * Then. 삭제한 지하철 구간이 반영된 역 목록이 조회됨