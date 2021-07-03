# STEP1 - ATDD 기반 리팩토링
## 요구사항

## 요구사항
- [x] LineSectionAcceptanceTest 리팩터링
- [ ] LineService 리팩터링

## 기능 구현 목록
- [x] LineSectionAcceptanceTest 리팩터링
  - [x] TestFactory 사용
  
- [ ] LineService 리팩토링
  - [x] Service 내에 orElseThrow 메소드가 던지는 예외가 RuntimeException인 경우 EntityNotFound로 대체
  - [x] getStations 도메인 추출 및 리팩토링
    - [x] Line 도메인 내 sections를 1급 콜렉션으로 대체
    - [x] Line 도메인 내 getStations 메소드 구현
    - [x] getStations 메소드가 반환하는 List<Station>을 1급 콜렉션인 Stations 구현 및 대체
  - [ ] 도메인 to DTO 메소드 구현
    - [x] LineResponse에 List<Line> to List<LineResponse> 메소드 구현
    - [x] LineResponse에 Line to LineResponse 메소드 구현
    - [x] StationResponse에 Stations to List<StationResponse> 메소드 구현
    - [x] StationResponse에 Line to StationResponse 메소드 구현
  - [ ] findUpStation 도메인 추출 및 리팩토링
  - [x] SaveLine 리팩토링
    - [x] ***도메인 추출*** LineRequest의 도메인 프로세스 전환 메소드 구현
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [x] fineLines 리팩토링
    - [x] ***도메인 추출*** LineResponse 내 팩토리 메소드로 Line을 변환 후 반환
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [ ] findLineResponseById 리팩토링
    - [ ] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환링
  - [ ] updateLine 리팩토링
    - [ ] Line 도메인 내에 update하는 메소드 구현 후 적용
  - [ ] addLineStation 리팩토링
  - [ ] removeLineStation 리팩토링
    
  