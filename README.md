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

## 🚀 1단계 - 인수 테스트 기반 리팩터링
- [x] LineService 리팩터링
  - [x] LineService의 비즈니스 로직을 도메인으로 옮기기
    - [x] 노선 지하철 역 조회
    - [x] 노선 구가 등록
    - [x] 노선 수정
    - [x] 역 제거
    - [x] 노선 지하철 역 추가
  - [x] 인수 테스트 작성
  - [x] 단위 테스트 작성
    - [x] Line
    - [x] Section
    - [x] Distance
    - [x] Sections 일급 컬렌션
- [x] (선택) LineSectionAcceptanceTest 리팩터링
- [x] 리펙터링
  - [x] 스타일 포멧
  - [x] 상수 추출

---

## 🚀 2단계 - 경로 조회 기능
- [ ] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

---
## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
