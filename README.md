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

## 요구사항 정리

### 1단계 - 인수 테스트 기반 리팩터링

1. 요구사항
    - [ ] LineService 리팩터링
    - [ ] (선택사항) LineSectionAcceptanceTest 리팩터링
2. 목록정리
    - 인수 테스트 기반 리팩터링
        - 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD 리팩터링
        - Domain 옮길 로직을 찾기
            -[ ] 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮기기
        - Domain 단위 테스트를 작성하기
            - [ ] 지하철 구간 등록 요청(상행, 하행)
            - [ ] 역 사이에 새로운 역 등록하는 경우
            - [ ] 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
            - [ ] 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음
            - [ ] 역과 역사이에 중간역 삭제
            - [ ] 지하철 노선 상행선 하행선 제거
            - [ ] 지하철 구간이 1개인 경우 삭제 불가
        - 로직 옮기기
            - 기존 로직 지우지 않고 새로운 로직을 만들어 수행
                - [ ] 기존 로직 찾기 
            - 정상 동작 확인 후 기존 로직 제거
