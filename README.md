# STEP1 - ATDD 기반 리팩토링
## 요구사항

## 요구사항
- [x] LineSectionAcceptanceTest 리팩터링
- [ ] LineService 리팩터링

## 기능 구현 목록
- [x] LineSectionAcceptanceTest 리팩터링
  - [x] TestFactory 사용
  
- [ ] LineService 리팩토링
  - [ ] Service 내에 orElseThrow 메소드가 던지는 예외가 RuntimeException인 경우 EntityNotFound로 대체
  - [ ] getStations 도메인 추출 및 리팩토링
  - [ ] findUpStation 도메인 추출 및 리팩토링
  - [ ] SaveLine 리팩토링
    - [ ] ***도메인 추출*** LineRequest의 도메인 프로세스 전환 메소드 구현
    - [ ] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [ ] fineLines 리팩토링
    - [ ] ***도메인 추출*** LineResponse 내 팩토리 메소드로 Line을 변환 후 반환
    - [ ] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [ ] findLineResponseById 리팩토링
    - [ ] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환링
  - [ ] updateLine 리팩토링
    - [ ] Line 도메인 내에 update하는 메소드 구현 후 적용
  - [ ] addLineStation 리팩토링
  - [ ] removeLineStation 리팩토링
    
  