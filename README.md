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

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md)
licensed.

## 요구사항 정리

### 1단계

1. Domain으로 옮길 로직을 찾기

- LineService 구간 추가 로직 적절한 도메인으로 이동
- LineService 구간 제거 로직 적절한 도메인으로 이동
- LineService 정류장 조회 로직 적절한 도메인을

2. Domain의 단위 테스트를 작성하기

- 구간 추가 단위테스트 작성
- 구간 추가시 해당 구간이 이미 있을떄에 대한 오류 단위테스트 작성
- 구간 추가시 연결된 구간이 없으면 오류 단위테스트 작성
- 구간 제거 단위테스트 작성.
- 구간 제거시 모든 구간이 제거되는경우 오류 단위테스트 작성.

### 피드백 요구사항 정리.

- 요구사항을 정리한다.
- 서비스와 DTO 생성을 분리.
- sections멤버 변수를 바로 초기화 해주어 null 체크
- Distance 원시값 포장
- Distance 단위테스트 작성
- entity 생성자 protected 처리
- Arrays.asList() 보단 Collection.emptyList() 사용
- 메소드 길이 10줄 미만으로 줄이기 - 함수추출
- 단위 테스트 BDD패턴으로 변경
