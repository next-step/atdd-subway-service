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

## 경로 조회 구현 목록

- [ ] 최단 경로 조회 인수 테스트 만들기 `ATDD`
    ```
    Feature: 지하철 경로 조회
    Given:
    지하철 역 등록되어 있다.
    노선 등록되어 있다.
    노선에 지하철 역 등록되어 있다.
    
    Scenario: 지하철 최단 경로 조회
    When: 최단 경로 조회 요청
    Then: 최단 경로의 지하철역 목록 조회됨
    ```

- [ ] 경로 조회 도메인 (PassFinder) Happy 케이스 테스트 및 기능 구현 `TDD`
- [ ] 경로 조회 도메인 (PassFinder) 예외 케이스 테스트
    - [ ] 출발역과 도착역이 같은 경우
    - [ ] 출발역과 도착역이 연결이 되어 있지 않은 경우
    - [ ] 존재하지 않은 출발역이나 도착역을 조회 할 경우
    - [ ] 구간에 지하철역이 존재하지 않을 경우

### Outside In 경우

- 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
- 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
- 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
- Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)

### Inside Out 경우

- 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
- 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
- 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작

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
