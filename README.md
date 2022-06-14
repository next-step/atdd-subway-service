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
- step1
  - LineSectionAcceptanceTest Refactoring
    - given, when, then 정의
    - helper method 분리
    - helper method static import
  - LineServce Refactoring
    - 리팩토링할 기능 및 방법
      - line 저장하기 / line 정보들 LineResponse로 찾기 / 순서대로 line에 포함된 station list받아오기 
        - 순서대로 line에 포함된 station list 받아오는 로직을 Line Domain에서 처리후 반환
      - 구간추가하기
        - Section : DownStation / UpStation 각 기준으로 섹션 분리 기능 구현 
        - Line 
          - 해당하는 Section 추출 및 Section.update 호출 
      - 구간제거하기
        - Line
          - 삭제할 Station 앞 / 뒤 Section 합치는 기능 구현 
        - Section
          - 각 Section 합치는 기능 구현 

- step2
  - 다익스트라 알고리즘을 사용하여 정거장 간 최단 거리, 정거장 추출 
    - 다익스트라 외부 라이브러리 사용 테스트 코드 작성
    - 다익스트라를 통한 최단거리 추출 인수테스트 작성 
      - 이때 Controller, Service껍데기만 작성 후 Mock, dto 정의하여 인수테스트 성공시키기 부터 시작
      - Outside in방식으로 테스트코드 작성 
        - 서비스 로직 테스트시, 사용되는 도메인은 Mock객체를 활용하여 테스트
        - Happy case에 대한 부분만 구현
  - 각 RunTimeException을 알맞은 Exception으로 변경 및 Advice생성


- step4
  - 정거장 간 경로 조회시, 해당 경로에 대한 운임을 추출/확인할수 있게 개발하여야 한다. 
  - 인수테스트 작성
    - PathAcceptanceTest에 "지하철 이용요금 확인" 인수테스트 추가 
  - 이용요금 조건
    - 나이 별 할인 
      - 13 <= age < 19 : (totalPrice - 350) * 0.8
      - 6 <= age < 13 : (totalPrice - 350) * 0.5
      - else : none 
    - 거리 별 운임료
      - distance <= 10 : 1250 
      - distance < 10 <= 50 : 1250 + Math.ceil((distance - 10) / 5) * 100    
        - 10km 초과시, 5km당 100원
      - distance > 50 : 1250 + 40 / 5 * 100 + Math.ceil((distance - 50)/8) * 100
    - 노선 별 추가요금 
      - 추가요금이 있는 노선을 이용할 경우, 측정된 요금에 추가된다 
  - 요금정책 개발 예정 내역 
    - 거리 별 금액을 계산하는 interface / 구현체 생성. 
    - 나이 별 할인률을 제공하는 interface / 구현체 생성 
    - Dijkstra Edge에 Line정보를 추가 
      - GraphPath.getEdgeList() 를 통해, 지나온 Line들을 추출한뒤, 추가 운임료 계산 
