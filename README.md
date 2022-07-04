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
# 1단계 인수 테스트 기반 리팩터링

### 요구사항

* 인수 테스트 기반 리팩터링
    * LineService의 비즈니스 로직을 도메인으로 옮기기
    * 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
    * 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기
      1. Domain으로 옮길 로직을 찾기
         스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
         객체지향 생활체조를 참고
      2. Domain의 단위 테스트를 작성하기
         서비스 레이어에서 옮겨 올 로직의 기능을 테스트
         SectionsTest나 LineTest 클래스가 생성될 수 있음
      3. 로직을 옮기기
         기존 로직을 지우지 말고 새로운 로직을 만들어 수행
         정상 동작 확인 후 기존 로직 제거

### to-do list
- [ ] LineService 리팩터링
    - [X] Sections 추가
        - [X] getStation 이동
        - [X] SectionsTest getStations 추가
    - [X] Distance 값객체로 변경
        - [X] Section 에 distance 변수 타입을 Distance 로 변경
        - [X] DistanceConverter 추가
        - [X] DistanceTest 추가
    - [X] addLineStation 개선
        - [X] 기존로직 복사하여 개선
        - [X] Line 에 addSection 이동
        - [X] Sections 에 로직 이동
            - [X] Section 에 rearrange 추가
            - [X] SectionTest 추가
            - [X] Section 에 유효성 검사 추가
            - [X] Sections 에 유효성 검사 추가
        - [X] 기존로직 제거
    - [X] dto to domain 로직 수정
    - [X] removeLineStation 개선
        - [X] 기존로직 복사하여 개선
        - [X] Sections 에 로직 이동
            - [X] 구간 병합 추가
            - [X] 기존 구간 삭제
        - [X] 테스트 케이스 추가
            - [X] SectionsTest 케이스 추가
            - [X] SectionTest 케이스 추가
        - [X] 기존로직 제거
- [X] 전역 예외 처리 추가
    - [X] RuntimeException 으로 사용되던 예외 수정
- [X] 인수 테스트 코드 시나리오 기반으로 리팩토링


---

# 2단계 요구사항

* 최단 경로 조회 인수 테스트 만들기
* 최단 경로 조회 기능 구현하기

### to-do list
- [X] 최단 경로 조회 인수 테스트 만들기
    - [X] 최단거리 조회 성공 케이스 추가
    - [X] 최단거리 조회 에외 케이스 추가
- [ ] 최단 경로 조회 기능 구현하기
    - [X] PathFinder 추가
    - [X] PathService 추가
    - [X] PathController 추가


---

# 3단계 요구사항

* 토큰 발급 기능 (로그인) 인수 테스트 만들기
* 인증 - 내 정보 조회 기능 완성하기
* 인증 - 즐겨 찾기 기능 완성하기

### to-do list
- [X] 토큰 발급 인수 테스트 만들기
    - [X] AuthAcceptanceTest
        - [X] 로그인 케이스
        - [X] Bearer Auth 로그인 실패 케이스
        - [X] Bearer Auth 유효하지 않은 케이스
    - [X] 로그인한 사용자의 정보로 반환하도록 기능 추가
- [X] 내 정보 조회 기능
    - [X] MemberAcceptanceTest
        - [X] manageMyInfo 케이스 추가
    - [X] MemberController
        - [X] 나의 정보 조회
        - [X] 나의 정보 수정
        - [X] 나의 정보 삭제
- [X] 즐겨 찾기 기능 구현하기
    - [X] FavoritesAcceptanceTest
        - [X] manageFavorite 인수 테스트 케이스 추가
    - [X] Favorite 기능
      - [X] Favorite 등록
      - [X] Favorite 목록 조회
      - [X] Favorite 삭제

---

# 4단계 요구사항

* 경로 조회 시 거리 기준 요금 정보 포함하기
* 노선별 추가 요금 정책 추가
* 연령별 할인 정책 추가

### to-do list

- [X] 거리별 요금 정책 인수 테스트 추가
    - [X] Price 추가
        - [X] 거리별 추가 요금 계산 추가  
