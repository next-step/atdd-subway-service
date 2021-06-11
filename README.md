# 요구사항
## LineSectionAcceptanceTest 리팩터링
Feature: 지하철 구간 관련 기능
   Background: 
      Given 강남역이 등록
      AND 양재역이 등록
      AND 정자역이 등록
      AND 광교역이 등록
      And 신분당선이 등록
   
   Scenario: 지하철 구간을 등록
      When 노선에 역 등록 요청
      Then 노선 조회하여 역이 정상 등록 됨
      Then 역이 순서 정렬이 됨

      