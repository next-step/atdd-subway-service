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

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md)
licensed.

## 인수 테스트 기반 TDD 미션

### 1단계 - 인수 테스트 기반 리팩터링

- [X] LineSectionAcceptanceTest 리팩터링
- [X] LineSectionAddAcceptanceTest 라인 추가관련 분리
- [X] LineSectionRemoveAcceptanceTest 라인 제거관련 분리
- [X] LineService 리팩터링
- [X] `@ControllerAdvice` 적용
- [X] 상황별 예외처리
- 테스트
    - [X] SectionTest
    - [X] SectionsTest
    - [X] LineTest
    - [X] LineRepositoryTest
    - [X] LineServiceTest

### 2단계 - 경로 조회 기능

- `PathAcceptanceTest` 구현
    - [ ] 정상 경로 조회
    - [ ] 같은 역 경로 조회 실패
    - [ ] 이어지지 않는 경로 조회 실패
- `PathController` 구현
    - [ ] 경로조회 라우터 추가
- [ ] `PathService` 경로 조회 서비스 구현
- [ ] `PathFactory` 인터페이스 활용하여 외부 모듈 의존역전시키기
- infrastructure 패키지 분리
    - [ ] `PathFactory` 구현체 구현