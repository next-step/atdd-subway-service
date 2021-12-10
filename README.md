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

# 기능 목록 정리
## 1단계 - 인수 테스트 기반 리팩터링리
- 공통
  - [X] 공통 상수 생성
  - [X] 커스텀 익셉션 생성
  - [X] 공통 메시지 생성
  - [X] 공통 에러 응답 생성
  - [X] 공통 에러 핸들러 생성
- LineService 리팩터링
  - Line
    - [X] 접근제어자 수정
    - [X] 정적 메소드 활용
    - [X] sections 일급콜렉션 사용
    - [X] 구간 추가 기능 추가
    - [X] 구간 삭제 기능 추가
    - [X] Validation 추가
  - Section
    - [X] 접근제어자 수정
    - [X] 정적 메소드 활용
    - [X] distance 분리 추가
    - [X] 상행역, 하행역 동일 여부 체크 로직 추가
    - [X] merge 기능 추가
  - Sections 
    - [X] 접근제어자 수정
    - [X] 정적 메소드 활용
    - [X] 일급콜렉션 생성
    - [X] merge 기능 추가
    - [X] 노선의 속한 구간에 상행, 하행역 동일 체크 로직 추가
    - [X] validation 기능 추가
  - Distance
    - [X] validataion 기능 추가
    - [X] 거리 더하기, 빼기 기능 추가
- LineSectionAcceptanceTest 리팩터링
  - [X] 텍스트 픽스쳐 생성
  - [X] 중복 테스트 제거 
  - [X] 전체 시나리오 나열
  - [X] 도메인 테스트 생성
    - [X] LineTest
    - [X] SectionTest
    - [X] SectionsTest

## 2단계 - 경로 조회 기능
- 요구사항
  - 최단 경로 조회 인수 테스트 만들기
  - 최단 경로 조회 기능 구현하기
- 미션 수행 순서
  - 인수 테스트 성공 시키기
  - 기능 구현
- 진행 순서
  - 지난 미션 피드백
    - [X] RestAdvice 커스텀 익센션별로 나눔
  - PathFinder
    - [X] 예외처리 로직
    - [X] jgraph 활용 최적 경로 찾는 로직
    - [X] 전 역 조회하는 메소드
  - PathFinderTest
    - [X] PathFinder 도메인 단위테스트 진행
  - PathServiceTest 
    - 가짜객체 활용하여 서비스 테스트 진행
  - PathAcceptanceTest 
    - 인수테스트 시나리오 작성
      - Feature : 최단 경로 조회
        - [X] Scenario: 출발역과 도착역의 최단경로 조회한다.
          - Given 지하철_역_생성_요청
          - Given 지하철_노선_생성_요청
          - Given 지하철_노선에_구간_등록_요청
          - When 지하철_최적_경로_요청
          - Then 지하철_최적_경로_목록_응답됨
          - Then 지하철_최적_경로_목록_포함됨
            - Feature : 최단 경로 조회
        - [X] Scenario: 출발역과 도착역이 같을 경우 예외처리한다.
          - Given 지하철_역_생성_요청
          - Given 지하철_노선_생성_요청
          - Given 지하철_노선에_구간_등록_요청
          - When 지하철_최적_경로_요청
          - Then 지하철_최적_경로_목록_실패됨
        - [X] Scenario: 출발역과 도착역이 연결이 되어 있지 않은 경우 예외처리한다.
          - Given 지하철_역_생성_요청
          - Given 지하철_노선_생성_요청
          - Given 지하철_노선에_구간_등록_요청
          - Given 다른_지하철_역_노선_구간_요청
          - When 지하철_최적_경로_요청
          - Then 지하철_최적_경로_목록_실패됨
        - [X] Scenario: 존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리한다.
          - Given 지하철_역_생성_요청
          - Given 지하철_노선_생성_요청
          - Given 지하철_노선에_구간_등록_요청
          - When 등록되지_않는_도착역_지하철_최적_경로_요청
          - Then 지하철_최적_경로_목록_실패됨
