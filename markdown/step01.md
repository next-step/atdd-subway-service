# 1단계 - 인수 테스트 기반 리팩터링

## 1. 요구사항 정의

### 1.1. 명시된 요구사항

#### 1.1.1. 요구사항

- LineSectionAcceptanceTest 리팩터링
- LineService 리팩터링

#### 1.1.2. 요구사항 설명

##### 1.1.2.1. 인수 테스트 통합

- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
    - 인수 조건 예시
        ```
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

###### 1.1.2.2 인수 테스트 기반 리팩터링

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

### 1.2. 기능 요구사항 정리

|구분 | 상세 |구현방법     |
|:----:  |:------  |:---------|
|노선 관리 리팩터링|• 지하철 노선 생성|• `LineAcceptanceTest` Refactor<br>• `LineServiceTest` 작성<br>• `LineTest` 작성|
|노선 관리 리팩터링|• 지하철 노선 생성 실패|• `LineAcceptanceTest` Refactor<br>• `LineServiceTest` 작성<br>• `LineTest` 작성|
|노선 관리 리팩터링|• 지하철 노선 목록 조회|• `LineAcceptanceTest` Refactor<br>• `LineServiceTest` 작성<br>• `LineTest` 작성<br>• (필요시) `LineGroupTest` 작성|
|노선 관리 리팩터링|• 지하철 노선 조회|• `LineAcceptanceTest` Refactor<br>• `LineServiceTest` 작성<br>• `LineTest` 작성|
|노선 관리 리팩터링|• 지하철 노선 수정|• `LineAcceptanceTest` Refactor<br>• `LineServiceTest` 작성<br>• `LineTest` 작성|
|노선 관리 리팩터링|• 지하철 노선 삭제|• `LineAcceptanceTest` Refactor<br>• `LineServiceTest` 작성<br>• `LineTest` 작성|
|구간 관리 리팩터링|• 지하철 구간 등록|• `LineSectionAcceptanceTest` Refactor<br>• `LineServiceTest` 작성|
|구간 관리 리팩터링|• 지하철 구간 등록 실패|• `LineSectionAcceptanceTest` Refactor<br>• `LineServiceTest` 작성|
|구간 관리 리팩터링|• 지하철 구간 삭제|• `LineSectionAcceptanceTest` Refactor<br>• `LineServiceTest` 작성|

### 1.3. 프로그래밍 요구사항

|구분|상세|구현 방법|
|:---:|:---|---|
|Convention|• 자바 코드 컨벤션을 지키면서 프로그래밍한다.<br>&nbsp;&nbsp;• https://naver.github.io/hackday-conventions-java/ <br>&nbsp;&nbsp;• https://google.github.io/styleguide/javaguide.html <br>&nbsp;&nbsp;•  https://myeonguni.tistory.com/1596 |- gradle-editorconfig 적용<br>- gradle-checkstyle 적용<br>- IntelliJ 적용<br>- Github 적용|
|테스트|• 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다. 단, UI(System.out, System.in) 로직은 제외<br>&nbsp;&nbsp;• 핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 구분한다.<br>&nbsp;&nbsp;•UI 로직을 InputView, ResultView와 같은 클래스를 추가해 분리한다.|- 핵심 로직 단위테스트|

### 1.4. 비기능 요구사항

|구분 |상세 |구현방법     |
|:----:  |:------  |:---------|
|요구사항|• 기능을 구현하기 전에 README.md 파일에 구현할 기능 목록을 정리해 추가한다.|- 요구사항 정의 정리|
|Convention|• git의 commit 단위는 앞 단계에서 README.md 파일에 정리한 기능 목록 단위로 추가한다.<br>&nbsp;&nbsp;• 참고문서 : [AngularJS Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)|- git commit 시 해당 convention 적용|

#### 1.4.1. AngularJS Commit Message Conventions 중

- commit message 종류를 다음과 같이 구분

```
feat (feature)
 fix (bug fix)
 docs (documentation)
 style (formatting, missing semi colons, …)
 refactor
 test (when adding missing tests)
 chore (maintain)
 ```

## 2. 분석 및 설계

### 2.1. 이번 Step 핵심 목표

#### 2.1.1. ATDD

- 테스트 하기 쉬운 코드 -> 테스트 하기 어려운 코드 순으로 작성해본다.
- 인수테스트, 통합 테스트, 테스트 하기 어려운 코드 : 실제 객체를 우선으로 적용 -> (가짜 객체) Test Double 중 Stubbing을 적용하여 본다 (Mockito, MockitoExtension, Spring-Stubbing)
- 단위테스트, 테스트 하기 쉬운 코드 : 도메인으로 분류하여 순수 자바로 실행 가능한 것들을 묶도록 한다.또한 도메인 역할에 맞는 메소드를 작성 및 테스트 한다.

### 2.2. Todo List

- [x] 0.기본 세팅
    - [x] 0-1.git fork/clone
        - [x] 0-1-1.NEXTSTEP 내 과제로 이동 및 '미션시작'
        - [x] 0-1-2.실습 github으로 이동
        - [x] 0-1-3.branch 'gregolee'로 변경
        - [x] 0-1-4.fork
        - [x] 0-1-5.clone : `git clone -b gregolee --single-branch https://github.com/gregolee/atdd-subway-service.git`
        - [x] 0-1-6.branch : `git checkout -b step1`
    - [x] 0-2.요구사항 정리
    - [x] 0-3.[AngularJS Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153#generating-changelogmd) 참고
    - [x] 0-4.Slack을 통해 merge가 되는지 확인한 후에 코드 리뷰 2단계 과정으로 다음 단계 미션을 진행
        - [x] 0-4-1.gregolee(master) branch로 체크아웃 : `git checkout gregolee`
        - [x] 0-4-2.step1 branch 삭제 : `git branch -D step1`
        - [x] 0-4-3.step1 branch 삭제 확인 : `git branch -a`
        - [x] 0-4-4.원본(next-step) git repository를 remote로 연결 (미션 당 1회) : `git remote add -t gregolee upstream https://github.com/next-step/atdd-subway-service`
        - [x] 0-4-5.원본(next-step) git repository를 remote로 연결 확인 : `git remote -v`
        - [x] 0-4-6.원본(next-step) git repository에서 merge된 나의 branch(gregolee)를 fetch : `git fetch upstream gregolee`
        - [x] 0-4-7.remote에서 가져온 나의 branch로 rebase : `git rebase upstream/gregolee`
        - [x] 0-4-7.gregolee -> step2로 체크아웃 : `git checkout -b step2`
    - [x] 0-5.리뷰어님의 리뷰를 반영한 코드로 수정
        - [x] 0-5-1.적용사항 없음
- [x] 1.자바 코드 컨벤션을 위한 세팅
    - [x] 1-1.[gradle-editorconfig](https://naver.github.io/hackday-conventions-java/#editorconfig) 적용
    - [x] 1-2.[gradle-checkstyle](https://naver.github.io/hackday-conventions-java/#checkstyle) 적용
    - [x] 1-3.[IntelliJ](https://naver.github.io/hackday-conventions-java/#_intellij) 적용
    - [x] 1-4.[Github](https://naver.github.io/hackday-conventions-java/#_github) 적용
- [x] 2.학습
    - [x] 2-1.Mockito, MockitoExtension, Spring-Stubbing 학습 
    - [x] 2-2.테스트를 분리하는 패러다임 학습
- [x] 3.분석 및 설계
    - [x] 3-1.step01.md 초안 작성
    - [x] 3-2.ATDD 작성
- [x] 4.구현
    - [x] 4-1.인수테스트 리팩터링
        - [x] 4-1-1.`LineAcceptanceTest`
        - [x] 4-1-2.`LineSectionAcceptanceTest`
    - [x] 4-2.지하철 노선 생성
    - [x] 4-3.지하철 노선 생성 실패
    - [x] 4-4.지하철 노선 목록 조회
    - [x] 4-5.지하철 노선 조회
    - [x] 4-6.지하철 노선 수정
    - [x] 4-7.지하철 노선 삭제 
    - [x] 4-8.지하철 구간 등록 
    - [x] 4-9.지하철 구간 등록 실패
    - [x] 4-10.지하철 구간 삭제 
    - [ ] 4-11.리뷰어님 코멘트 반영
        - [x] 4-11-1.`Distance.minus()` : `Distance` 리턴
        - [x] 4-11-2.`Sections` : `==` ====> `equals()`
        - [ ] 4-11-3.`LineAcceptanceTest.java` : 특정 상황에 대한 인수테스트로 변경
        - [ ] 4-11-4.`LineSectionAcceptanceTest.java` : 특정 상황에 대한 인수테스트로 변경
- [ ] 5.테스트
    - [ ] 5-1.Gradle build Success 확인
    - [ ] 5-2.checkstyle 문제없는지 확인 (Java Convention)
    - [ ] 5-3.요구사항 조건들 충족했는지 확인
        - [ ] 5-3-1.핵심 단위 로직 테스트
    - [ ] 5-4.인수 테스트 확인
    - [ ] 5-5.UI 테스트 확인
- [ ] 6.인수인계
    - [ ] 6-1.소감 및 피드백 정리
        - [ ] 6-1-1.느낀점 & 배운점 작성
        - [ ] 6-1-2.피드백 요청 정리
    - [ ] 6-2.코드리뷰 요청 및 피드백
        - [ ] 6-1-1.step1를 gregolee/atdd-subway-service로 push : `git push origin step1`
        - [ ] 6-1-2.pull request(PR) 작성
    - [ ] 6-3.Slack을 통해 merge가 되는지 확인한 후에 미션 종료

### 2.3. ATDD 작성

ATDD 작성 [Markdown 보기](./atdd.md)

## 3. 인수인계

### 3.1. 느낀점 & 배운점

#### 3.1.1. 느낀점

- 기존 프로덕션 코드를 교체하는 과정
    - 과거의 로직을 변경하지 않고 리팩터링하는 것이 매우 어렵게 느껴졌습니다.
        - 수정하는 과정에서 과거 로직을 건드려서 복구하는 과정이 쉽지 않았습니다.
        - 수정하는 동안 어디를 수정하고 있는지 해맸기 때문에 고통의 연속이었습니다.
    - 스트랭글러 패턴을 활용
        - 다소 쉽게 교체할 수 있어서 놀랬습니다.
        - 기존의 로직을 살려두었다가 완료가 되면 교체할 수 있어서 어디쯤 진행하고 있는지 확인할 수 있다는 장점이 있었습니다.
    - 다른 패턴을 학습하고 싶은 관심이 생겼습니다.
- 테스트
    - 우테캠 프로 과정을 하기 전에는 테스트코드는 '필요하면' 작성한다는 생각이 지배적이었습니다.
    - 그러나 인수테스트, 단위테스트(애플리케이션(서비스), 레포지토리, 도메인)를 진행하면서 '필수적으로' 작성해야 된다는 생각을 하게 되었습니다.
    - 테스트를 하는 것은 프로덕션 코드를 지키면서, 새로운 코드로 변경하는데 안전한 방어막으로 작용한다는 것을 새삼 깨달았습니다.

#### 3.1.2. 배운점

- 안전한 리팩토링
    - 기존의 메서드를 두고 Strangler Pattern 을 활용하여 리팩토링
- 테스트
    - 단위 테스트에도 구분이 있다.
        - 실체객체와 가짜객체를 구분하여 테스트를 진행할 수 있다.
        - 외부 라이브러리, 데이터베이스 등에 의존하는 경우 테스트가 어려워질 수 있다.
            - 실체객체로 테스트를 하도록 노력하고
            - 만약 그게 어렵다면 가짜객체(mock)를 통하여 테스트를 진행
    - 인수 테스트는 개념적인 구분이 있다.
        - 지난 미션에서는 인수테스트를 API 단위로 끊어서 진행
        - 이번 미션에서는 인수테스트를 시나리오 기반으로 API의 조합으로 진행

### 3.2. 피드백 요청

- 이번 단계에서는 피드백 요청이 없습니다.
