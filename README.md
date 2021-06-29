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


## Todo

- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineSerivce 리팩토링

- [x] line.syncSequence() 코드 수정
- [x] 매직넘버 수정
- [x] sections.stream().anyMatch(section -> section.getDownStation().equals(upStation));
- [x] addSection() 리팩토링
- [x] 구간 앞뒤로 구간 추가시 오류 확인

2단계 - 경로 조회 기능

- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

3단계 - 인증을 통한 기능 구현

- [x] 토큰 발급
  - [x] 토큰 발급 인수 테스트 작성
  - [x] 토근 발급 기능 구현
- [x] 내 정보 조회 기능
  - [x] 내 정보 조회 기능 인수 테스트 작성
  - [x] 내 정보 조회 기능 기능 구현
- [x] 즐겨 찾기 기능 구현하기
  - [x] 즐겨 찾기 인수 테스트 작성
  - [x] 즐겨 찾기 기능 구현
  
---

4단계 - 요금 조회

 - [x] 경로 조회 시 거리 기준 요금 정보 포함하기
   - 기본 운임(10Km 이내) 1,250원
   - 이용 거리 초과 시 추가운임 부과
     - 10Km 초과 ~ 50Km까지 (5km마다 100원)
     - 50Km 초과 시 (8km마다 100원)
 - [x] 노선별 추가 요금 정책 추가
 - [x] 연령별 할인 정책 추가


