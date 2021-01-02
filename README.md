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

## Step1. 인수 테스트 기반 리팩터링
### 인수 테스트 정리
- Feature: 지하철 구간 관련 기능
    - Background
        - given
            - 지하철역 등록되어 있음
            - and 지하철 노선 등록되어 있음
            - and 지하철 노선에 지하철역 등록되어 있음
    - [X] Scenario1: 지하철 구간을 관리한다.
        - when
            - 지하철 구간 등록 요청
        - then
            - 지하철 구간 등록 성공
        - when
            - 지하철 노선에 등록된 역 목록 조회 요청
        - then
            - 등록한 지하철 구간이 반영된 역 목록이 조회됨
        - when
            - 지하철 구간 삭제 요청
        - then
            - 지하철 구간 삭제됨
        - when
            - 지하철 노선에 등록된 역 목록 조회 요청
        - then
            - 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        - when
            - 삭제한 지하철 구간에 포함된 지하철 역 조회 요청
        - then
            - 구간이 삭제되도 속한 지하철 역은 조회됨
    - [X] Scenario2: 기존 지하철 노선의 종점간 거리보다 긴 종점 구간을 추가한다.
        - when
            - 기존 지하철 구간의 종점 사이의 종점감 거리보다 긴 거리를 가진 종점 구간을 추가 요청
        - then
            - 지하철 구간 등록 성공
    - [X] Scenario3: 기존 지하철 노선의 종점간 거리보다 긴 종점이 아닌 지하철 구간을 등록한다.
        - when
            - 기존 지하철 구간의 종점 사이의 종점감 거리보다 긴 거리를 가진 종점이 아닌 구간을 추가 요청
        - then
            - 지하철 구간 등록 실패
    - [X] Scenario4: 실수로 등록되지 않은 지하철 역이 포함된 지하철 구간을 등록한다.
        - when
            - 등록되지 않은 역이 포함된 지하철 구간 등록 요청
        - then
            - 지하철 구간 등록 실패
    - [X] Scenario5: 실수로 기존 지하철 노선과 접점이 없는 지하철 구간을 등록한다.
        - when
            - 기존 지하철 노선과 접점이 없는 지하철 구간 등록 요청
        - then
            - 지하철 구간 등록 실패
    - [X] Scenario6: 실수로 똑같은 지하철 구간을 두번 등록한다.
        - when
            - 지하철 구간 등록 요청
        - then
            - 지하철 구간 등록 성공
        - when
            - 똑같은 지하철 구간 등록 요청
        - then
            - 지하철 구간 등록 실패
    - [X] Scenario7: 하나밖에 안남은 지하철 구간의 역을 삭제한다.  
        - when
            - 지하철 구간 삭제 요청
        - then
            - 지하철 구간 삭제 실패

- Feature: 지하철 노선 관련 기능
    - Background
        - given
            - 지하철역 등록되어 있음
    - [X] Scenario1: 지하철 노선을 관리한다. 
        - when
            - 새로운 지하철 노선 등록 요청
        - then
            - 새로운 지하철 노선 등록 성공
        - when
            - 지하철 노선 조회 요청
        - then
            - 지하철 노선 조회 성공
        - when
            - 지하철 노선 이름 변경 요청
        - then
            - 지하철 노선 이름 변경 성공
        - when
            - 등록한 지하철 노선 삭제 요청
        - then
            - 등록한 지하철 노선 삭제 성공역
    - [X] Scenario2: 서로 겹치는 환승역이 있는 지하철 노선을 등록한다.
        - given
            - 지하철 노선 등록됨
        - when
            - 등록한 지하철 노선과 겹치는 역이 있는 지하철 노선 등록 요청
        - then
            - 지하철 노선 등록 성공
        - when
            - 새로운 지하철 노선에 등록된 역 목록 조회 요청
        - then
            - 지하철 노선에 등록된 역 목록 보임
        - when
            - 기존 지하철 노선에 등록된 역 목록 조회 요청
        - then
            - 새로 생성된 지하철 노선에 등록된 역과 겸치는 역 보임
    - [X] Scenario3: 실수로 같은 지하철 노선을 두번 등록한다.
        - when
            - 지하철 노선 등록 요청
        - then
            - 지하철 노선 등록 성공
        - when
            - 실수로 한번더 같은 지하철 노선 등록 요청
        - then
            - 지하철 노선 등록 실패
    - [X] Scenario4: 실수로 종점역을 빠뜨린 채로 지하철 노선을 등록 요청한다.
        - when
            - 종점역이 빠진 채로 지하철 노선 등록 요청
        - then
            - 지하철 노선 등록 실패
    - [X] Scenario5: 실수로 존재하지 않는 지하철역으로 지하철 노선 등록 요청한다.
        - when
            - 존재하지 않는 지하철역을 종점으로 지하철 노선 등록 요청
        - then
            - 지하철 노선 등록 실패
    - [X] Scenario6: 실수로 등록한 적 없는 지하철 노선을 수정하거나 삭제한다.
        - when
            - 등록한 적 없는 지하철 노선 삭제 요청
        - then
            - 지하철 노선 삭제 실패
        - when
            - 등록한 적 없는 지하철 노선 수정 요청
        - then
            - 지하철 노선 수정 실패

- Feature: 지하철  관련 기능 
    - [X] Scenario1: 지하철 역을 관리한다.
        - when
            - 새로운 지하철 역 등록 요청
        - then
            - 새로운 지하철 역 등록 성공
        - when
            - 지하철 역 조회 요청
        - then
            - 지하철 역 조회 성공
        - when
            - 등록한 역 노선 삭제 요청
        - then
            - 등록한 역 노선 삭제 성공역
    - [X] Scenario2: 실수로 같은 지하철 역을 두번 등록한다.
        - when
            - 새로운 지하철 역 등록 요청
        - then
            - 새로운 지하철 역 등록 성공
        - when
            - 새로운 지하철 역 등록 요청
        - then
            - 새로운 지하철 역 등록 실패
    - [X] Scenario3: 실수로 지하철 노선에 등록된 지하철 역을 삭제한다.
        - given
            - 지하철 역 등록됨
            - and 지하철 노선 등록됨
        - when
            - 지하철 노선에 등록된 지하철 역 삭제 요청
        - then
            - 지하철 역 삭제 실패
    - [X] Scenario4: 실수로 등록한 적 없는 지하철 역을 삭제한다.
        - when
            - 등록한 적 없는 지하철 역 삭제 요청
        - then
            - 지하철 역 삭제 실패

## Step1 피드백 반영 요구사항
- [X] Line 추가 메서드에서 하부 메서드들의 결과값을 활용
- [X] Java Collection의 내장 add 메서드 결과를 활용
- [X] LineService에서 Line 삭제 관련 기능에 강제로 추가된 해당 Line 존재 확인 여부 부분 개선
- [X] Line 내 Section 제거 메서드의 validation 의도를 코드상에 더 잘 드러나도록 개선
- [X] Line 내 Section 이용 시 getter 사용 자제
- [X] Section의 distance 래핑
- [X] 컨트롤러에서 Spring Validation을 사용해서 기본적인 null 체크 하도록 변경
- [X] LineAcceptanceTest의 중복 제거

## Step1 2차 피드백 반영 요구사항
- [X] 문서에 쓸모없는 오타 제거
- [X] Line 내의 불필요한 getter 제거
- [X] Line 내의 디미터 법칙 위반 부분 개선
  - 이전 피드백에서 이미 개선되어 있음
- [X] Distance의 경계값 조정
- [X] Distance 뺄셈의 0 계산 별도 처리
    - 최소 경계값이 1로 상향되면서 0 계산할 일이 사라짐

## Step2. 경로 조회 기능
### 인수테스트 시나리오
- 기능: 지하철 노선 내 두 역간 최단 거리를 조회할 수 있다.
    ```java
    /*
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    ```
    - given
        - 지하철역 등록되어 있음
        - and 지하철 노선 등록되어 있음
        - and 지하철 노선에 지하철역 등록되어 있음
    
- [X] 시나리오1: 정상적으로 최단 경로 조회
    - when
        - 사용자가 최단 경로 조회 요청
    - then
        - 최단 경로 조회 요청 성공
- [X] 시나리오2: 출발역과 도착역이 같은 최단 경로 조회
    - when
        - 사용자가 실수로 출발역과 도착역이 같은 최단 경로 조회 요청
    - then
        - 최단 경로 조회 요청 실패
- [X] 시나리오3: 경로에 없는 역의 최단 경로를 조회
    - given
        - 지하철 역 등록됨
    - when
        - 사용자가 경로에 없는 지하철역을 포함한 최단 경로 조회 요청
    -  then
        - 최단 경로 조회 요청 실패
- [X] 시나리오4: 갈 수 없는 경로의 최단 경로 조회
    - given
        - 지하철역 등록됨
        - and 기존 노선과 연결되지 않은 지하철 노선 등록됨
    - when
        - 사용자가 갈 수 없는 경로의 최단 경로 조회 요청
    - then
        - 최단 경로 조회 요청 실패

### Todo-list
- [X] Jgraph 학습 테스트 진행
    - [X] 노선 한개짜리 그래프 생성해보기
    - [X] 노선 한개짜리 그래프 최단경로 탐색해보기
    - [X] 겹치는 역이 있는 노선 두개짜리 그래프 생성해보기
    - [X] 겹치는 역이 있는 노선 두개짜리 그래프 최단경로 탐색해보기
        - 그래프를 노선 별로 나눴다가 나중에 다시 합치는 건 좋지 않은 방법인 것을 학습
        - JGraph 알고리즘이 간선 추가에 대한 방법을 제한적으로 공개했기 때문
    - [X] 겹치는 역이 없는 노선 두개짜리 그래프 생성해보기
    - [X] 겹치는 역이 없는 노선 두개짜리 그래프 최단경로 탐색해보기
        - 겹치는 역이 없으면 반환값이고 뭐고 없다. => 그냥 바로 NPE 던져버림
    
- [X] PathFinder 구현
    - [X] 전체 경로를 갖고 있는 graph를 인자로 갖는다.
    - [X] Station ID 컬렉션, SafeSectionInfo(upStationId, downStationId, distance) 컬렉션을 인자로 받아서 오브젝트를 생성한다.
        - [X] 두 컬렉션 중 하나라도 비어있거나 null일 경우 예외 발생
- [X] ShortestPath 구현
    - PathFinder에서 경로 탐색 후 거리값, 경로를 할 때마다 graph 탐색이 발생한다.
    - graph 탐색은 꾀나 비싼 연산일 수 있겠다는 생각이 들어서(전체 경로가 클수록) 한번만 계산할 수 있도록 별도의 오브젝트로 분리
    - [X] 최단 경로를 구할 수 있다.
        - [X] 출발지나 목적지가 경로 내에 존재하지 않는 경우 도메인 에외를 발생시킨다.
        - [X] 갈 수 없는 경로의 경우 도메인 예외를 발생시킨다.
    - [X] 최단 경로의 지하철 역 목록을 구할 수 있다.
    - [X] 최단 경로의 총 거리를 알 수 있다.
- [X] PathFactory 구현
    - Path를 생성하는 것 자체가 꾀나 큰 책임이 됐기 때문에 별도의 클래스 분리
- [X] SafeLineAdapter 구현
    - SafeLine 구현체로 Path에 필요한 Line 정보를 안전하게 가져오는 역할을 수행한다.
    - [X] 모든 Line의 모든 역 ID를 중복을 제거하고 가져올 수 있다.
    - [X] 모든 Line의 구간 정보를 SafeSectionInfo로 바꿔서 가져올 수 있다.
- [X] SafeStationAdapter 구현
    - Station ID에 맞는 역 정보들을 모두 불러올 수 있다.
- [X] SafeSectionInfo
    - Line의 구간 정보 중 필요한 부분만 추려낸 오브젝트
    - [X] upStationId, downStationId, distance를 인자로 갖는다.
- [X] PathService 구현
    - 도메인 서비스와 도메인 객체를 잘 조합해서 최단 경로를 검색할 수 있도록 한다.
- [X] Controller와 인수 테스트 구현

## Step3. 인증을 통한 기능 구현
### 인수테스트 시나리오
- 기능: 로그인(토큰 발급)
- [X] 시나리오1: 정상적으로 로그인
    - given
        - 회원 등록되어 있음
    - when
        - 로그인 요청
    - then
        - 로그인 됨
- [X] 시나리오2: 회원가입도 안하고 로그인부터 시도
    - when
        - 로그인 요청
    - then
        - 로그인 실패
- [X] 시나리오3: 실수로 패스워드를 잘못 입력하고 로그인
    - given
        - 회원 등록되어 있음
    - when
        - 틀린 패스워드로 로그인 요청
    - then
        - 로그인 실패
- [X] 시나리오4: 잘못된 토큰으로 인가 기능 사용
    - given
        - 회원 등록되어 있음
        - and 토큰 발급 받음
    - when
        - 잘못된 토큰으로 내 정보 조회 요청
    - then
        - 내 정보 조회 실패

- 기능: 로그인 된 상태에서 내 정보를 관리한다.
- [X] 시나리오1: 로그인한 상태에서 내 정보를 관리한다.
    - given
        - 회원가입 됨
        - and 로그인 됨
    - when
        - 내 정보 조회 요청
    - then
        - 내 정보 조회 성공
    - when
        - 내 정보 수정 요청(이름)
    - then
        - 내 정보 수정 성공
    - when
        - 회원 탈퇴
    - then
        - 회원 탈퇴 성공
- [X] 시나리오2: 로그인 안한 상태에서 내 정보를 조회한다.
    - given
        - 회원가입 됨
    - when
        - 내 정보 조회 요청
    - then
        - 내 정보 조회 실패

- 기능: 즐겨찾기를 관리한다.
- 기본 조건
    - given
        - 지하철역 등록되어 있음
        - and 지하철 노선 등록되어 있음
        - and 지하철 노선에 지하철역 등록되어 있음
        - and 회원등록되어 있음
        - and 로그인되어 있음
- [X] 시나리오1: 사용자가 즐겨찾기를 관리한다.
    - when
        - 즐겨찾기 생성 요청
    - then
        - 즐겨찾기 생성 성공
    - when
        - 즐겨찾기 목록 조회 요청
    - then
        - 즐겨찾기 목록 조회 성공
    - when
        - 즐겨찾기 삭제 요청
    - then
        - 즐겨찾기 삭제 성공
    
- [X] 시나리오2: 사용자가 실수로 똑같은 즐겨찾기를 두번 등록한다.
    - given
        - 즐겨찾기 등록되어 있음
    - when
        - 같은 정보로 즐겨찾기 등록 요청
    - then
        - 즐겨찾기 등록 실패
    
### Todo-list
- [X] 인가 기능을 구현하기 위해 CustomResolver 등록
    - 이미 등록되어 있음
    
- [X] Favorite
    - [X] memberId, sourceId, targetId를 속성으로 갖는다.
- [X] SafeStationAdapter DIP
    - 이전 Path에서 사용한 DIP와 거의 유사한 기능 구현
    - modifiedDate가 추가되는 차이점이 있다.
- [X] FavoriteService
    - [X] Favorite에 대한 기본적인 CRUD 가능
    - [X] 기존에 등록된 favorite 정보와 같은 정보는 또 등록할 수 없다.

### 고민사항
- Favorite은 엔티티? VO?
    - Favorite 단일로 존재할 수 있는가? => X
    - Member에 종속적되어 존재한다. = Member가 없으면 의미가 없다.
    - Favorite은 별도의 식별자가 반드시 필요한가? => X
    - MemberId, SourceId, TargetId 중 하나라도 다르면 전혀 다른 오브젝트로 구분이 가능하다.
    => ~~Member Aggregate에 종속된 VO로 간주.~~
      
- 이번 과제에서는 Favorite을 Entity로 취급
    - 제시된 요구사항의 삭제가 ID를 기반으로 삭제가 진행됨
    - Member의 VO 컬렉션으로 Favorite 구현시 Favorite의 ID를 통한 조회, 삭제는 VO의 의미가 퇴색됨
    => Favorite을 엔티티로 간주한다.

### Step3 피드백 반영 요구사항
- [X] SafeStationInFavoriteAdapter의 예외 상황이 좀 더 의미가 잘 드러나도록 개선
- [X] 즐겨찾기 삭제 시도 시 해당 유저가 맞는지 확인하는 절차 필요

## Step4. 요금 조회
### Todo-list
- [ ] 거리에 따른 추가 요금 계산
    - Fee
        - 요금을 다루는 인터페이스
        - [X] 해당 거리의 요금을 계산하는 calculate 메서드를 추상화한다.
    - DefaultFee
        - Fee의 하부 타입 10km 이내 기본요금을 책임진다.
        - [X] 언제나 1250원을 반환한다.
    - LongFee
        - 10km 초과 ~ 50km 이하 거리의 요금계산을 책임진다.
        - [X] 기본료로 1250원을 갖는다.
        - [X] 5km 마다 추가요금을 100원을 붙여서 계산한다.
    - SuperLongFee
        - 50km 초과의 요금계산을 책임진다.
        - [X] 기본료로 2050원을 갖는다.
        - [X] 8km 마다 추가요금을 100원 붙여서 계산한다.
- [ ] 환승노선의 환승 비용에 따른 추가 요금 계산
    - TransferFinder
        - 경로 중 환승한 노선만 찾아내는 책임을 수행한다.
        - [ ] ShortestPath를 인자로 주입받아 LineInPaths를 반환한다.
        - [ ] LineInPaths를 통해 환승한 노선들을 찾아낼 수 있다.
    - LineInPath
        - [ ] 경로 상에서 어떤 노선에 속해 있는지를 속성으로 갖는다. (List<Long>)
    - LineInPaths
        - LineInPath 일급 컬렉션
        - [ ] 구성요소 중 두개 이상의 노선이 겹치는 부분을 찾아낼 수 있다.
        - [ ] 각 요소의 다음 요소가 어떤 노선인지 알 수 있다.
        - [ ] 노선이 겹치는 요소가 현재 경로에서 어떤 노선으로 환승했는지 알 수 있다.
- [ ] 이용자 나이에 따른 할인 계산
- FeeCalculator
  - 요금을 계산할 수 있다.
  - [ ] double 타입 거리를 인자로 받아 알맞는 요금을 계산할 수 있다.
