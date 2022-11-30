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

## 🚀 2단계 - 인수 테스트 기반 리팩터링
#### 미션 수행 순서
- mock 서버와 dto를 정의하여 인수 테스트 성공 시키기
- TDD의 방향보다 테스트를 통해 구현할 기능을 명세하는것과 리팩터링이 더 중요함
- Outside In 경우
  - 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
  - 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
  - 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
  - Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)
- - Inside Out 경우
- 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
- 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
- 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작

#### 요구사항
- [ ] 최단 경로 조회 인수 테스트 만들기
  - [ ] 인수 테스트 픽스쳐 정의
  - [ ] 인수 테스트 코드 작성(Outside In 방식으로 시작)
  - [ ] controller -> service 순서로 시작
- [ ] 최단 경로 조회 기능 구현하기
  - [ ] 최단 경로 조회 기능 구현하기

---
## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
