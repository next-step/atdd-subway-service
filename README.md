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


## 기능목록정리 1

- [x] 인수테스트 리팩토링 : 하나의 테스트로 통합
- 도메인 로직 테스트 생성 : 도메인 테스트 생성, 비즈니스 로직을 도메인으로 옮기기
  - [x] 지하철 역 조회 로직 분리
  - [x] 지하철 구간 추가 로직 분리
  - [x] 지하철 구간 삭제 로직 분리

## 기능목록정리 - 경로조회기능

- [x] 인수테스트 만들기
- [x] 컨트롤러 레이어 생성
- [x] 서비스 레이어 생성
- [x] 최단경로찾기 도메인 레이어 구현
- [x] 최단경로찾기 도메인 레이어 예외상황 구현

## 기능목록정리 - 인증을 통한 기능 구현

- [x] 로그인 인수 테스트
- [x] 내 정보 조회 기능 인수 테스트 작성
- [ ] 즐겨찾기 기능 인수 테스트 구현
- [ ] 즐겨찾기 기능 구현
