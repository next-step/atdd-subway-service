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

## 🚀 1단계 - 인수 테스트 기반 리팩터링
### 요구사항
- [x] LineService 리팩터링
- [x] LineSectionAcceptanceTest 리팩터링
### 요구사항 설명
#### 인수 테스트 기반 리팩터링
- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

1. Domain으로 옮길 로직을 찾기
   - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   - 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   - SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거
   - 인수 테스트 통합
   - API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
   - 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

#### 인수 조건 예시
```text
Feature: 지하철 구간 관련 기능

Background
Given 지하철역 등록되어 있음
And 지하철 노선 등록되어 있음
And 지하철 노선에 지하철역 등록되어 있음

Scenario: 지하철 구간을 관리
When 지하철 구간 등록 요청
Then 지하철 구간 등록됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
When 지하철 구간 삭제 요청
Then 지하철 구간 삭제됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```

## 🚀 2단계 - 경로 조회 기능
### 요구사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기
#### 요청 / 응답 포맷
- Request
   ```text
   HTTP/1.1 200 
   Request method:	GET
   Request URI:	http://localhost:55494/paths?source=1&target=6
   Headers: 	Accept=application/json
           Content-Type=application/json; charset=UTF-8
   ```
- Response
   ```text
   HTTP/1.1 200 
   Content-Type: application/json
   Transfer-Encoding: chunked
   Date: Sat, 09 May 2020 14:54:11 GMT
   Keep-Alive: timeout=60
   Connection: keep-alive
   
   {
       "stations": [
           {
               "id": 5,
               "name": "양재시민의숲역",
               "createdAt": "2020-05-09T23:54:12.007"
           },
           {
               "id": 4,
               "name": "양재역",
               "createdAt": "2020-05-09T23:54:11.995"
           },
           {
               "id": 1,
               "name": "강남역",
               "createdAt": "2020-05-09T23:54:11.855"
           },
           {
               "id": 2,
               "name": "역삼역",
               "createdAt": "2020-05-09T23:54:11.876"
           },
           {
               "id": 3,
               "name": "선릉역",
               "createdAt": "2020-05-09T23:54:11.893"
           }
       ],
       "distance": 40
   }
   ```

### 🚀 3단계 - 인증을 통한 기능 구현
#### 요구사항
- [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [x] 인증 - 내 정보 조회 기능 완성하기
- [x] 인증 - 즐겨 찾기 기능 완성하기

#### 요구사항 설명
- 토큰 발급 인수 테스트
  - 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
  - AuthAcceptanceTest 인수 테스트 만들기
   ```text
   Feature: 로그인 기능
   
     Scenario: 로그인을 시도한다.
       Given 회원 등록되어 있음
       When 로그인 요청
       Then 로그인 됨
   ```
- 내 정보 조회 기능
  - MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
  - 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
  - 로그인 후 발급 받은 토큰을 포함해서 요청 하기
- 토큰을 통한 인증
  - /members/me 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
  - @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
  - 아래의 기능이 제대로 동작하도록 구현하기
- 즐겨 찾기 기능 구현하기
  - 즐겨찾기 기능을 완성하기
  - 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

<br>

### 🚀 4단계 - 요금 조회
#### 요구사항
- [x] 경로 조회 시 거리 기준 요금 정보 포함하기
- [x] 노선별 추가 요금 정책 추가
- [x] 연령별 할인 정책 추가

#### 요구사항 설명
- 거리별 요금 정책
- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
- 10km초과∼50km까지(5km마다 100원)
- 50km초과 시 (8km마다 100원)
- 지하철 운임은 거리비례제로 책정됩니다. (실제 이동한 경로가 아닌 최단거리 기준으로 계산)
  - 참고 링크: http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354
- 수정된 인수 조건
    ```text
    Feature: 지하철 경로 검색
    
      Scenario: 두 역의 최단 거리 경로를 조회
        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
        Then 최단 거리 경로를 응답
        And 총 거리도 함께 응답함
        And ** 지하철 이용 요금도 함께 응답함 **
    ```
- 노선별 추가 요금 정책
  - 노선에 추가 요금 필드를 추가
  - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
  - 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
- 로그인 사용자의 경우 연령별 요금 할인 적용
  - 청소년: 운임에서 350원을 공제한 금액의 20%할인
  - 어린이: 운임에서 350원을 공제한 금액의 50%할인 
    ```text
    - 청소년: 13세 이상~19세 미만
    - 어린이: 6세 이상~ 13세 미만
    ```

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
