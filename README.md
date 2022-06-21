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
1단계 - 인수 테스트 기반 리팩터링

- [X] LineService 리팩터링
  - [X] LineService의 비즈니스 로직을 도메인으로 옮기기 
  - [X] domain의 단위 테스트를 작성하기
  
- [X] (선택) LineSectionAcceptanceTest 리팩터링
  - [X] API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
  
    ```text
      Feature: 지하철 구간 관련 기능
      Background:
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


* 참고 사항 
1. Domain으로 옮길 로직을 찾기
   스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   정상 동작 확인 후 기존 로직 제거


2단계 - 경로 조회 기능 

- [ ] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기
    - 최단경로 가중치 : 거리 
      
    

- 요청 포멧 
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
  * 예외 사항
    * 출발역과 도착역이 같은 경우
    * 출발역과 도착역이 연결이 되어 있지 않은 경우
    * 의존재하지 않은 출발역이나 도착역을 조회 할 경우
