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

## 🚀 3단계 인증을 통한 기능 구현
### 구현사항 정리
* [ ] 토큰발급기능 인수테스트 작성
    ```markdown
    ✅ Feature: 토큰 발급 기능
        🔙 Background
            Given 회원으로 등록되어 있음 
          
        1️⃣ Scenario : 로그인을 시도한다 
            When 로그인 요청
            Then 로그인 성공함
      
        2️⃣ Scenario : 등록되지 않은 이메일로 로그인을 시도한다
            When 로그인 요청
            Then 로그인 실패함  
      
        3️⃣ Scenario : 잘못된 비밀번호로 로그인을 시도한다
            When 로그인 요청
            Then 로그인 실패함
      
        4️⃣ Scenario : 로그인 이후 15분(토큰 유효기간)이 경과하고 내 정보를 조회한다
            When 내정보 조회 요청
            Then 조회 실패함
  
        5️⃣ Scenario : 로그인 이후 15분이 경과하고 즐겨찾기를 조회한다
            When 즐겨찾기 조회 요청
            Then 조회 실패함  
    ```    
* [X] 인증-내 정보 조회 기능 완성
* [X] 인증-즐겨찾기 기능 완성

---
## 🚀 2단계 경로 조회 기능
### 구현사항 정리
* [X] 최단 경로 조회 인수 테스트 만들기
    ```markdown
    ✅ Feature: 최단 경로 조회 기능 
        🔙 Background
            Given 지하철역 등록되어 있음 (2호선, 3호선, 4호선)
              And 지하철 노선 등록되어 있음
              And 지하철 노선에 지하철역 등록되어 있음 
          
        1️⃣ Scenario : 최단 경로를 조회한다 
            When A역에서 B역까지 최단경로 조회 요청
            Then 최단 경로인 지하철 목록 반환됨
      
        2️⃣ Scenario : 출발역과 도착역이 같다
            When A역에서 A역까지 최단경로 조회 요청
            Then 최단 경로 조회 실패함      
      
        3️⃣ Scenario : 출발역과 도착역이 연결되어 있지 않다
            When A역에서 B역까지 최단경로 조회 요청
            Then 최단 경로 조회 실패함
      
        4️⃣ Scenario : 출발역이나 도착역이 존재하지 않는다
            When A역에서 B역까지 최단경로 조회 요청
            Then 최단 경로 조회 실패함
    ```     

* [x] 최단 경로 조회 기능 구현하기
    * 미션 수행 순서
        * [x] 인수테스트 성공 시키기 : Mock 서버와 DTO를 정의하여 성공시키기
        * [x] 기능 구현
            * `PathController`에서부터 `PathService`,`PathResponse` 도출 → `PathServiceTest` 작성   
            * `PathService`에서 시작하여 `PathFinder`, `Path`, `SectionEdge` 도출 → `PathFinderTest` 작성
        * [x] 예외상황 처리 구현
            * 출발역과 도착역이 동일한 경우
            * 출발역과 도착역이 연결되어 있지 않은 경우
            * 출발역이나 도착역이 존재하지 않는 경우

## 🚀 1단계 인수 테스트 기반 리팩터링
### 구현사항 정리

* [X] LineSectionAcceptanceTest 리팩터링
    * 목표 : 인수테스트 통합 → 시나리오, 흐름 위주의 테스트로 리팩토링
    * **As-is** LineSectionAcceptanceTest
  
    ```markdown
    ✅ Feature: 지하철 구간 관련 기능 
        🔙 Background
            Given 지하철역 등록되어 있음
              And 지하철 노선 등록되어 있음
              And 지하철 노선에 지하철역 등록되어 있음 
          
        1️⃣ Scenario : 지하철 구간을 등록한다
            When 지하철 구간 등록 요청
            Then 지하철 구간 등록됨
            Then 지하철 구간 순서대로 정렬되어 조회됨
      
        2️⃣ Scenario : 지하철 노선에 이미 등록되어 있는 역을 등록한다
            When 지하철 구간 등록 요청
            Then 지하철 구간 등록 실패됨       
      
        3️⃣ Scenario : 지하철 노선에 등록되지 않은 역을 기준으로 등록한다
            When 지하철 구간 등록 요청
            Then 지하철 구간 등록 실패됨 
      
        4️⃣ Scenario : 지하철 노선에 등록된 지하철역을 제외한다
            Given 지하철 구간 등록됨 
            When 지하철 구간 삭제 요청
            Then 지하철 구간 삭제됨
            Then (삭제한 지하철 구간 반영되어) 지하철 구간 순서대로 정렬되어 조회됨
           
        5️⃣ Scenario : 지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다
            When 지하철 구간 삭제 요청
            Then 지하철 구간 삭제 실패됨
    ```     

  * **To-be** LineSectionAcceptanceTest
  
  ```markdown
    ✅ Feature: 지하철 구간 관련 기능 
        🔙 Background
            Given 지하철역 등록되어 있음
            And 지하철 노선 등록되어 있음
            And 지하철 노선에 지하철역 등록되어 있음        

        1️⃣ Scenario : 지하철 구간 등록 관련
            When 지하철 구간 등록 요청
            Then 지하철 구간 등록됨
            Then 지하철 구간 순서대로 정렬되어 조회됨
            
            When (기존에 있는) 지하철 구간 등록 요청
            Then  지하철 구간 등록 실패됨
            
            When (노선에 등록되지 않는 지하철 역을 이용하여) 지하철 구간 등록 요청
            Then 지하철 구간 등록 실패됨 
    
        2️⃣ Scenario : 지하철 구간 제외 관련
            Given 지하철 구간 등록 요청 
            
            When 지하철 구간 삭제 요청
            Then 지하철 구간 삭제됨
            Then (삭제한 지하철 구간 반영되어) 지하철 구간 순서대로 정렬되어 조회됨
            
            When (노선에 구간이 하나뿐일 때) 지하철 구간 삭제 요청
            Then 지하철 구간 삭제 실패됨
  ```
    
* [X] LineService 리팩터링
    * [X] Domain으로 옮길 로직 찾기
        * `getStations()` → `Line`, `Section` `Sections`에 위임
            * 노선에 등록되어 있는 구간을 찾음(`Line`)
            * 상행 종점 찾음 (`Sections`)
            * 상행 종점을 시작으로 해서, 현재 지하철역을 upStation으로 가지는 구간이 있다면(`Section`) 
            해당 구간의 downStation을 List<Station>에 add(`Sections`) 
            
        * `addLineStation()` → `Line`, `Section`, `Sections`에 위임
            * 신규 구간의 `Station` 점검 (`Sections`)
              * 양쪽 지하철역 모두 동일한 기존 구간이 있는지 등의 점검은 `Section`에서 담당  
            * 점검 통과했을 경우 거리 고려하여 add (`Sections`)
          
        * `removeLineStation` → `Line`, `Section`, `Sections`에 위임
            * 삭제 가능여부 점검 (`Sections`)
            * 삭제하려는 구간 탐색 (`Sections`)
            * 거리 계산하여 구간 제거한 신규 구간 추가 (`Section`, `Sections`)
        
    * [X] Service 리팩토링 + Domain의 단위테스트 작성
    