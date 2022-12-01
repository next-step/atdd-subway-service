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

## 강의내용 간략 정리
### 단위 테스트와 TDD
단위 테스트를 딱 잘라 정의하긴 어렵지만,  
`협력 객체`에 따라서 구분할 수 있음

> 단위 테스트
> - 작은 코드 조각(단위)을 검증
> - 빠르게 수행 가능
> - **격리된 방식**으로 처리

'격리해야 하는 대상을 무엇으로 보느냐?' 에서 차이가 발생

#### Classist(테스트 자체를 격리) ; 통합
- 검증해야 하는 `테스트(기능)`과 다른 `테스트(기능)`을 격리
- 따라서 테스트간 공유하는 의존성이 아니라면 `실제 객체`를 사용

#### Mockist(테스트 해야하는 객체를 격리) ; 고립
- 검증해야 하는 `테스트 대상(객체)`와 다른 `협력 객체`를 격리
- 따라서 테스트 대상(객체)이 의존하는 것을 `가짜 객체`로 대체

#### TDD 흐름의 종류
위에서 정의한 Classist 와 Mockist 에 따라 각각  
Inside Out vs Outside In 으로 TDD의 흐름을 정의할 수 있다.
##### Inside Out (Classist - Chicago style)
- 실제 객체를 다뤄야 하기 때문에 도메인 모델부터 시작
##### Outside In (Mockist - London style)
- 상위 레벨부터 시작
- 테스트 더블을 적극 활용하여 협력 객체의 예상 결과를 정의
- 다음 사이클로 테스트 더블로 미리 정의한 협력 객체를 테스트 대상

#### 정리
- **_정답은 없다_** : 상향식과 하향식을 적절히 섞어서 아는것에서 모르는 방향으로 전개  
- Top-Down 으로 방향을 잡고, Bottom-up 구현
- 내부 구현에 대한 설계 흐름을 구상
- 설계가 끝나면 도메인부터 차근차근 TDD로 기능 구현
- **길 잃으면 인수테스트로 돌아와서 다시 길을 볼 것**
- TDD를 연습할때는 실제 객체를 활용하는 것을 우선
- 영 안되겠다 싶으면 테스트 더블(Mocking 등등..) 활용

### 인수 테스트 기반 리팩토링
비즈니스 로직을 리팩터링 할 때 무엇부터 해야하는지? 에서 부터 시작  
- **테스트 코드** 먼저 수정
- 생각해보면, TDD 라는 것 자체가 테스트 코드부터 시작하니 리팩터링도 테스트 코드부터 하는게 당연

#### 구체적인 리펙터링 프로세스(스트랭글러 패턴 ; Strangle pattern)
new 로 교체하고, legacy 는 일정 기간 후 삭제하는 작업 패턴(**인수 테스트 보우하사 리펙터링 만세**)
1. 인수 테스트를 작성하여 리펙터링의 명확한 길을 잡는다
2. 새로운 코드를 한벌 더 만든다(가능한 수준 만큼 제한은 없음)
3. 파악이 가능한 부분부터 단위 테스트를 만들어 기능 검증하기
4. 기능 구현 후 **기존 코드 제거**하여 새로운 인수 테스트와 **기존 코드를 잘 대체하는 지** 확인

## 이번 미션에서의 목표
- 인수 테스트에서 TDD 사이클로 넘어가는 흐름 경험

### 개인적 목표
- 테스트 더블 사용법에 대한 숙지(Mock 등등..)

## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항
- [x] LineSectionAcceptanceTest 리팩터링 ; 인수 테스트 통합 작성 완료
- [x] LineService 리팩터링
    - LineService의 비즈니스 로직을 도메인으로 옮기기
    - 부분 부분 리팩터링
    - 인수테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링

#### 요구사항 설명
1. Domain 으로 옮길 로직을 찾기
   - **스프링 빈을 사용하는 객체**는 제외
   - **스프링 빈을 사용하는 객체에 의존하는 로직**은 제외
   - 나머지 로직을 도메인으로!!
   - 객체지향 생활체조 원칙 참고
2. Domain 단위 테스트를 작성하기
   - 1 단계에서 옮겨 올 로직의 기능을 테스트
   - SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거

### 1단계 피드백 기능 목록
- [x] 기능 목록 작성 시 인수 조건, 도메인 기능 목록 등 상세화
- [x] Service에서 Service를 의존하면 순환참조 발생 가능 --> 방향은 아래로 흐르는 것이 좋음
- [x] 조회 메서드에 `@Transactional(readOnly=true)` 선언하여, `Lazy Loading` 대비
- [x] 매직넘버에 이름 부여

## 2단계 - 경로 조회 기능
```text
     교대역    --- 2호선 : 10 ---   강남역
     |                             |
    3호선 : 3                     신분당선 : 10
     |                             |
     남부터미널역  --- 3호선 : 2 ---   양재
```
### 인수 테스트
- [x] 최단 경로 조회 인수 테스트 만들기
  - Given : 경로 조회 가능한 노선들이 등록되어 있다
  - When : 출발역과 도착역의 경로 조회 요청하면
  - Then : 최단 경로와 거리를 응답한다.

### Domain
- [ ] Paths 구현
  - [ ] 최단 경로 찾기
  - [ ] 예외 케이스 구현
    - [ ] 출발역과 도착역이 같은 경우
    - [ ] 출발역과 도착역이 견결되어 있지 않은 경우
    - [ ] 존재하지 않은 출발역이나 도착역을 조회 할 경우

### Repository
- [ ] SectionRepository 구현

### Service
- [ ] PathService 구현