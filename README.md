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

## 1단계  - 인수 테스트 기반 리팩터링 요구사항
* [x] LineSectionAcceptanceTest 리팩터링
~~~
Feature: 지하철 구간 관련 기능

Background
Given 지하철역 등록되어 있음
And 지하철 노선 등록되어 있음
And 지하철 노선에 지하철역 등록되어 있음

Scenario: 지하철 구간 관리 성공 검증
When 지하철 구간 등록 요청
Then 지하철 구간 등록됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
When 지하철 구간 삭제 요청
Then 지하철 구간 삭제됨
When 지하철 노선에 등록된 역 목록 조회 요청
Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨

Scenario: 지하철 구간 관리 실패 검증
When 지하철 중복된 구간 등록 요청
Then 지하철 구간 등록 실패됨
When 지하철 노선에 등록되지 않은 역을 등록요청
Then 지하철 구간 등록 실패됨
When 지하철 노선에 등록된 지하철역이 두개일 때 한 역을 삭제 요청
Then 지하철 구간 삭제 실패됨
~~~
* [x] LineService 리팩터링
  * [x] getStations() -> Line 도메인으로 이동
  * [x] Sections 일급콜렉션 생성
  * [x] 구역 추가 기능 적절한 도메인으로 이동
  * [x] 구역 제거 기능 적절한 도메인으로 이동
  * [x] RestControllerAdvice, ExceptionHandler 를 사용하여 에러 처리

### 피드백 및 공부 정리
[피드백링크](https://github.com/next-step/atdd-subway-service/pull/377)
1. 시스템 예외처리 메시지를 클라이언트에 그대로 전달한다면 어떠한 문제가 있을지 고민해보아요.
~~~
 두가지 정도의 문제가 있을 거같습니다.
 1. 메시지에 보안에 취약한 내용을 노출 할 수 있다.
 2. 메시지를 통해 의도를 주지 않으면 어떤 부분에서 에러가 났는지 파악하기 어렵다.
~~~
2.  Optional을 매개변수로 넘기는 것이 왜 안티패턴에 해당되는지 고민해보아요.
~~~
참고: https://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/
Optional을 매개변수로 넘기면 그 메소드 안에서도 다시 ifPresent() 체크를 해야되서 안티패턴인거 같습니다.
다른 미션에서도 Optional 사용법에 대해 알려주신 내용이 있었는데 제가 체크가 덜 된 거 같습니다. 감사합니다. :) 
~~~
3. 예외를 발생시킬 때 어떠한 문제인지 메시지를 작성해보면 어떨까요? 메시지를 통해 의도를 나타낼 수 있다면 어떠한 장점을 가지는지도 고민해보세요!
~~~
1번의 내용과 조금 겹치는 부분인거 같네요.:) 메시지를 잘 전달 해주면 프론트 쪽에서 어떻게 다시 호출하거나 에러처리를 어떻게 할 지 방향을 잡기 쉬울거 같습니다.
~~~
* 예외처리 관련 참고자료
  * https://cheese10yun.github.io/spring-guide-exception/#null
  * https://tecoble.techcourse.co.kr/post/2020-08-17-custom-exception/
  * https://jaehun2841.github.io/2019/03/10/effective-java-item72/#illegalargumentexception

## 2단계 - 경로 조회 기능
* [x] 최단 경로 조회 인수 테스트 만들기
* [x] HappyPath 인수 테스트
* [x] 예외 상황 인수 테스트
  * [x] 출발역과 도착역이 같은 경우
  * [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
  * [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우
* [x] 최단 경로 조회 기능 구현하기
  * [x] 각 sourceId,targetId 로 sourceStation, targetStation 찾고 findLines로 Stations 찾기
  * [x] PathFinder 테스트코드 작성
  * [x] PathFinder 경로 조회 기능
  * [x] Mock 객체로 테스트 작성
* [x] 예외 상황 구현
  * [x] 출발역과 도착역이 같은 경우
  * [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
  * [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우

### 피드백 및 공부 정리
[피드백링크](https://github.com/next-step/atdd-subway-service/pull/401)
1. 모든 IllegalArgumentException는 InternalServerError에 해당할까요? IllegalArgumentException도 표준예외기 때문에 https://github.com/next-step/atdd-subway-service/pull/377#discussion_r763763503의 문제가 남아있을 것 같네요~
~~~
IllegalArgumentException은 메시지를 감추고 새로운 에러 클래스를 사용하여 처리했습니다.
~~~
2. PathService와 PathFinder가 강하게 결합이 되어있네요! 경로를 구하는 알고리즘의 경우 언제든 변경될 수 있는 부분이기 때문에 언제든 확장할 수 있도록 수정해보면 어떨까요?
~~~
PathFinder 의존성 주입으로 생성하게 하여 확장에 유연하게 만들었습니다. 
~~~
3. PathService에 의존성을 제거하고 확장성을 고려해보라는 코멘트를 남겼는데, 해당 코멘트를 반영하면 mockito 없이 테스트를 작성하실 수 있으실거예요 ㅎㅎ
~~~
해당 부분은 mockito없이 PathFinderTest.java 로 테스트 해보았습니다. 감사합니다. :)
~~~

* 답변 정리: Mock 테스트 활용 방법은 어떤게 있나요?
~~~
외부에 영향을 받지 않고 싶을 때 Mock을 사용합니다!
말씀하신 것 처럼 통제가 불가능한 의존성과의 의존성을 끊어줄 떄 사용하는게 일반적이고, 테스트 대상을 슬라이스 테스트 할 때도 사용됩니다!
~~~

## 3단계 - 인증을 통한 기능 구현
* [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
* [x] 인증 - 내 정보 관리 인수 테스트 만들기
  * [x] 내 정보 조회
  * [x] 내 정보 수정
  * [x] 내 정보 삭제
* [x] 인증 - 즐겨 찾기 관리 인수 테스트
* [x] 즐겨찾기 기능 구현
  * [x] 즐겨찾기 생성
  * [x] 즐겨찾기 목록 조회
  * [x] 즐겨찾기 삭제

### 공부정리
* RestAssured jwt auth 테스트 방법
  * 참고자료1 : https://stackoverflow.com/questions/42790021/rest-assured-bearer-authentication
  * 참고자료2 : https://www.baeldung.com/rest-assured-authentication <- oauth2 검색
  * given().auth().oauth2(token) 사용하면 된다.

### 피드백 정리
1. 모든 커스텀 예외처리가 INTERNAL_SERVER_ERROR이 맞을까요?
~~~
커스텀 예외처리를 두개로 나누어 INTERNAL_SERVER_ERROR, BAD_REQUEST 로 나눴습니다. :)
~~~
2. PathFinder를 의존성 주입으로 생성하게 하여 확장에 유연하게 만들었지만, 이제 PathFinder가 강하게 결합이 됐네요. 적절한 확장포인트를 생각하여 인터페이스를 통해 의존성을 끊어보면 좋겠어요!
~~~
PathFinder인터페이스를 도출하고 기존 파일은 JgraphtPathFinder로 명명하여 의존성을 줄이려고 했습니다.
~~~
3. 커스텀 예외처리를 분리하신 만큼 해당 서비스 내에 의도한 예외상황은 커스텀 예외처리를 활용해보면 좋겠습니다~
~~~
의도된 예외상황은 커스텀예외처리 했습니다~
~~~

### 4단계 - 요금 조회
* [ ] 인수테스트 추가 및 수정
  * [x] 경로 조회 시 거리 기준 요금
  * [ ] 노선별 추가 요금 정책
  * [ ] 연령별 할인 정책
  * [ ] 노선, 연령별, cnrkdns 할인 정책 인수 테스트
* [ ] 기능 구현
  * [x] 경로 조회 시 거리 기준 요금 정보 포함하기
  * [x] 이용 거리초과 시 추가운임 부과
    * 10km초과∼50km까지(5km마다 100원)
    * 50km초과 시 (8km마다 100원)
  * [ ] 노선에 추가 요금 필드를 추가
  * [ ] 노선별 추가 요금 정책 추가
  * [ ] 연령별 할인 정책 추가
