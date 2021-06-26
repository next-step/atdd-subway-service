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


## 1단계 요구사항

* 시나리오 테스트 분석하고 새로운 시나리오를 작성하여 요구사항에 정리한다.
    * Feature: 지하철 구간 관련 기능

    * Background
        * Given 지하철역 등록되어 있음
        * And 지하철 노선 등록되어 있음
        * And 지하철 노선에 지하철역 등록되어 있음

    *  Scenario: 지하철 구간을 순서 없이 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2.  지하철 노선을 등록한다.
            1. When 지하철 양재역 - 정자역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간을 제일 상행을 제외하고 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2. 지하철 노선을 제외한다.
            1. When 지하철 강남역 제외 요청
            2. Then 지하철 구간 제외됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간을 제일 하행을 제외하고 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2. 지하철 노선을 제외한다.
            1. When 지하철 광교역 구간 제외 요청
            2. Then 지하철 구간 제외됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간 중 중간을 제외하고 등록하고 조회한다.
        1. 지하철 노선을 등록한다.
            1. When 지하철 강남역 - 양재역 구간 등록 요청
               And 지하철 광교역 - 정자역 구간 등록 요청
            2. Then 지하철 구간 등록됨
        2. 지하철 노선을 제외한다.
            1. When 지하철 광교역 구간 제외 요청
            2. Then 지하철 구간 제외됨
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회

    *  Scenario: 지하철 구간 등록 실패를 한다.
        1. 지하철 노선에 이미 등록되어 있는 역을 등록한다.
            1.  When 지하철 강남역 - 광교역 구간 등록 요청
            2. Then 지하철 노선에 지하철역 등록 실패
        2. 지하철 노선에 등록되지 않은 역을 기준으로 등록한다.
            1. When 지하철 노선에 연결되지 않는 구간 양재역 - 정자역 새로 등록
            2. Then 지하철 노선에 지하철역 등록 실패
        3. 지하철 노선을 조회한다.
            1. When 지하철 노선 조회 요청
            2. Then 지하철 노선 순서대로 조회
        4. 지하철 구간 등록시 거리가 큰 값으로 등록한다.
            1. When 지하철 노선에 강남역 - 정자역 새로 등록 요청. 요청시 거리를 100으로 한다.
            2. Then 지하철 노선에 지하철역 등록 실패

    *  Scenario: 지하철 구간 제외 실패를 한다.
        1. 지하철 노선에 없는 구간을 제외한다.
            1. When 지하철 노선에 연결되지 않는 구간 정자역 제외 요청
            2. Then 지하철 노선에 지하철역 제외 실패
        2. 지하철 노선 구간이 하나만 있는 경우에 구간을 제외한다..
            1. When 지하철 노선에 연결된 구간 강남역 제외 요청
            2. Then 지하철 노선에 지하철역 제외 실패

* 계획한 시나리오를 LineSectionAcceptanceTest when과 then으로 작성한다.
* LineService를 분석하고 어떤 도메인으로 옮길 수 있을지 요구사항에 정리한다.
    * saveLine의 메서드를 sections 일급 컬렉션으로 생성하여 처리
    * findLines 메서드에서 응답을 Line 도메인에서 생성되도록 수정
    * findLineResponseById 메서드 Line으로 이동
    * addLines 메서드에서 station 유효성 검사를 station 메서드에서 진행하도록
    * 구간에 대한 검사는 sections 에서
* LineService의 로직을 단계별로 분리한다. 분리 시 도메인으로 새로운 코드를 작성하고 테스트 완료 후 기존 로직을 제거한다. 이 후 테스트를 다시 실행하여 모두 통과 되는지 확인한다.
* LineService 로직 분기를 단계별로 진행하고, 완료 후 리팩토링을 진행한다.
    * 각 도메인의 역할에 맞는 행동을 하고 있는지 체크
    * 행동에 대한 메시지를 정확히 전달하고 있는지 체크
    * import 순서나 사용하지 않는 메서드가 있는지 체크
    

## 1단계 질문

Q. 설계한대로 리팩토링을 하고 나서 보니 Line에서는 Sections에 관련된 파라메터만 넘겨주며, 자신이 가지고 있는 객체인 Sections 안에서 Section을 생성하기 인자로 자기 자신을(this) 넘겨주는 행위들이 반복되고 있었습니다.
(95604ca 에서 커밋 내용을 확인하실 수 있으십니다)

그 이후로 Line의 역할을 모두 Sections에 위임하고 있다고 판단하여 Line과 Sections의 역할을 분담하게 하였는데요...!
제가 결정한 부분이 어떤지 피드백을 들어보고싶습니다. (Line에서 Sections로 Section을 생성해서 넘기려고 하니 중복 코드들도 보이는거 같네요ㅜ)

A. section을 add하거나 remove할 때 validate하는� 로직, add/remove함으로써 기존에 존재하던 section들을 조정하는 로직을 sections로 옮기는 건 어떨까요~? line 외부에서 line에게 section을 추가해달라고 요청할 수 있지만 line은 sections에 section이 추가될 때 벌어지는 내용에 대해서 알 필요가 없다고 생각하기 때문에 의견드립니다:) 다른 의견이 있으시면 말씀 부탁드립니다!
그리고 section을 어디에서 생성할까에 대해서는 line의 내부, sections의 내부 모두 괜찮다고 생각합니다:)

Q. Mock 객체를 이용하여 Service layer의 테스트를 작성하였는데요. 가짜 객체를 사용한 테스트 작성이 조금 어색하게 느껴지네요. 테스트 작성 시 인수테스트나 유닛테스트에서 체크 가능한 부분은 제외 시키며 개발을 하였는데요. 혹시 부자연 스러운 부분이 있다면 피드백 꼭 부탁드리겠습니다.

A1. 인수테스트, 유닛테스트에서 확인이 가능해도 작성하면 좋다고 생각하고 있어요:) 왜냐하면 현재는 도메인으로 로직을 옮기는 중이고 각 레이어의 테스트들로 부분적인 확인을 하며 로직을 옮기고 마지막에 인수테스트로 확인하는 절차를 가지게끔 미션이 구성되어 있기 때문입니다. 하지만 작성해주신 부분에서 어색한 부분은 없어 이대로 괜찮을거 같습니다:)


Q. 말씀해주신데로 Line에서 하는 역할들을 Sections로 옮겼는데요. 일전에 질문드렸다 시피 Sections로 역할을 위임하게 되니 Line에서 Sections에게 메시지를 던질 때 자기 자신을(파라메터에 this가 꼭 들어가게 되었음) 넘기는 경우가 생기게 되었습니다.

말씀해주신대로 Line 보다는 Sections에서 많은 책임을 부여해서 처음 개발을 하였다가 Line에서 하도록 리팩토링을 진행하여 커밋한것이 이전의 풀리퀘스트 내용이었는데요. 이 부분에 대해서 조금 조언을 얻고자 합니다.

조금 정리하자면 Line에서 Sections으로 메시지를 던질 때 파라메터에 자기 자신을 넘기게 된다면 Sections보다 lIne에서 처리해야지 않나 라는 질문이 되겠네요. (설계에 대해 궁금한 정도니 리뷰어님의 생각을 편하게 말씀해주셔도 좋습니다! 정답은... 딱히 없는거 같지만 Sections에서 처리는게 맞다고 생각은 들지만 Line을 넘기는게 자꾸 찝찝하네요 ^^;;)

*A2. 다르게 한번 생각해 보면 어떨까요~? sections 입장에서 addSection라는 인터페이스를 외부에 제공할 때 어떤 모습으로 제공하는게 좋을까요? sections 입장에서 인터페이스를 생각해보고 line을 파라미터로 받는 인터페이스가 아닌 더 나은 인터페이스가 있는지 생각해보면 어떨까요~?*

## 2단계 요구사항

* 최단 경로 인수 테스트를 작성하기

* Outside In으로 컨트롤러 레이어 개발 후 Mock을 이용하여 서비스 테스트 작성 후 개발
    * 요청을 받는 컨트롤러 개발
    * 길 찾기 경로에 대한 TDD (실제객체 이용), 서비스 레이어, 레퍼지토리는 Mock 이용하여 흐름을 테스트 -> 도메인을 테스트 함
        * 요청에 대하여 길 찾기 경로가 정상적으로 작동하는지
    * 정상 확인
* Inside out 으로 도메인 테스트 시작
    * 각 도메인을 TDD로 구현
    * 도메인의 역할을 지정
    * 도메인과 관계를 맺는 객체 기능 구현 -> 리팩터링 싸이클 돌기
        * 찾으려는 역이 있는지 체크
        * 출발역과 도착역이 같은지 체크
        * 출발역과 도착역이 연결되어 있지 않은 경우
        * 역 출발과 도착의 구간 합이 예상한것과 같은지
        * 경로 순서가 올바른지
		