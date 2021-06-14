# 요구사항
## LineSectionAcceptanceTest 리팩터링
Feature: 지하철 구간 관련 기능  
   Background:  
      Given 강남역이 등록  
      AND 양재역이 등록  
      AND 정자역이 등록  
      AND 광교역이 등록  
      And 신분당선이 등록  

   Scenario: 지하철 구간을 등록한다  
      When 노선에 역 등록 요청  
      Then 노선 조회하여 역이 정상 등록 됨  
      When 지하철역 목록 조회  
      Then 역이 순서 정렬이 됨  

   Scenario: 지하철 노선에 여러개의 역을 순서 상관 없이 등록한다  
      When 노선에 역 등록 요청  
      Then 노선 조회하여 역이 정상 등록 됨  
      When 노선에 역 등록 요청  
      Then 노선 조회하여 역이 정상 등록 됨  
      When 지하철역 목록 조회  
      Then 역이 순서 정렬이 됨  

   Scenario: 지하철 노선에 이미 등록되어있는 역을 등록한다  
      When 이미 있는 역을 추가  
      Then 추가 실패  
   
   Scenario: 지하철 노선에 등록되지 않은 역을 기준으로 등록한다  
      When 노선에 없는 역 끼리 연결 요청  
      Then 추가 실패  
   
   Scenario: 지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다  
      When 역 제거 요청  
      Then 역 제거 실패 

   Scenario: 지하철 노선에 등록된 지하철역을 제외한다  
      When 노선에 역 등록 요청  
      Then 역 추가 성공  
      When 노선에 역 등록 요청  
      Then 역 추가 성공  
      When 지하철역 목록 조회  
      Then 역이 순서 정렬이 됨  
      When 역 제거 요청  
      Then 역 제거 성공  
      When 지하철역 목록 조회  
      Then 역이 순서 정렬이 됨  
## 최단거리
            
Feature: 최단거리를 검색한다.
   Background:
      Given 강남역이 등록  
      AND 양재역이 등록  
      AND 정자역이 등록  
      AND 광교역이 등록  
      AND 신분당선이 등록 (강남역 - 양재역)  
      AND 2호선이 등록 (강남역 - 정자역)  
      AND 3호선이 등록 (강남역 - 광교역)  
   Scenario: 최단 거리 검색을 한다.  
      When 신분당선에 역 추가 (강남역 - 양재역 - 정자역) (SUM : 5)  
      Then 역 추가 성공  
      When 2호선에 역 추가 (강남역 - 양재역 - 정자역) (SUM : 7)  
      Then 역 추가 성공  
      When 3호선에 역 추가 (강남역 - 광교역 - 정자역) (SUM : 3)  
      Then 역 추가 성공  
      When 강남역 - 정자역 최단거리 요청  
      Then 강남역 - 광교역 결과 출력  
      Then 거리 3 출력  
   Explain: 거리
      신분당선 : 강남역 -3- 양재역 -2- 정자역
      2호선 : 강남역 -3- 양재역 -4- 정자역
      3호선 : 강남역 -2- 광교역 -1- 정자역
     
       