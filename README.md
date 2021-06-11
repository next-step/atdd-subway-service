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
      When 역 제거 요청
      Then 역 제거 성공

   Scenario: 지하철 노선에 존재하지 않는 역을 제외한다
      When 역 제거 요청
      Then 역 제거 실패