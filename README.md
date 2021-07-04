# 인수테스트 기반 TDD 
## STEP2 - 경로 조회 기능
### 요구사항
- [ ] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

### 기능 구현 목록
- [ ] 작성 예정

## STEP1 - ATDD 기반 리팩토링
### 요구사항
- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링

### 기능 구현 목록
- [x] LineSectionAcceptanceTest 리팩터링
  - [x] TestFactory 사용
  
- [x] LineService 리팩토링
  - [x] Service 내에 orElseThrow 메소드가 던지는 예외가 RuntimeException인 경우 EntityNotFound로 대체
  - [x] getStations 도메인 추출 및 리팩토링
    - [x] Line 도메인 내 sections를 1급 콜렉션으로 대체
    - [x] Line 도메인 내 getStations 메소드 구현
    - [x] getStations 메소드가 반환하는 List<Station>을 1급 콜렉션인 Stations 구현 및 대체
  - [x] 도메인 to DTO 메소드 구현
    - [x] LineResponse에 List<Line> to List<LineResponse> 메소드 구현
    - [x] LineResponse에 Line to LineResponse 메소드 구현
    - [x] StationResponse에 Stations to List<StationResponse> 메소드 구현
    - [x] StationResponse에 Line to StationResponse 메소드 구현
  - [x] findUpStation 도메인 추출 및 리팩토링
  - [x] SaveLine 리팩토링
    - [x] ***도메인 추출*** LineRequest의 도메인 프로세스 전환 메소드 구현
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [x] fineLines 리팩토링
    - [x] ***도메인 추출*** LineResponse 내 팩토리 메소드로 Line을 변환 후 반환
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [x] findLineResponseById 리팩토링
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환링
  - [x] updateLine 리팩토링
    - [x] Line 도메인 내에 update하는 메소드 구현 후 적용
  - [x] ***도메인 추출*** addLineStation 리팩토링
  - [x] ***도메인 추출*** removeLineStation 리팩토링
  
### 피드백 반영 필요 목록
- [ ] 전체적인 시나리오 검증 테스트 구현
- [x] getConnectedStationCount() 메소드의 반환 값을 int타입으로 대체 
- [ ] 읽기 전용 조회 서비스 로직에는 @Trasactional(readOnly = true) 옵션 적용
- [ ] Sections 내에 Optional과 isPresent()를 사용하는 부분을 ifPresent로 대체
- [ ] e.printStackTrace 대신 logback으로 처리
- [ ] SectionsTest의 역 조회 검증 로직을 size() 뿐만 아니라 세부 요소까지 확인하도록 개선