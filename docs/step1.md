#step 1

## 요구사항

---

- [X] Line 인수테스트 공통 메소드 추출 및 정리
- [X] Line의 sections 일급컬렉션으로 변경
- [X] Section의 distance 원시값 포장 및 단위테스트 작성
- [X] LineService에서 Domain으로 옮길 로직 확인하여 정리
  - getStations -> Sections
  - addLineStation(stationService 아래 부분) -> Sections
  - removeLineStation(stationService 아래 부분) -> Sections
  - findUpStation -> Sections
  - findLines(return 부분) -> LineResponse
- [ ] 옮길 로직을 바탕으로 Domain 단위 테스트 작성
  - [X] getStations -> Sections
  - [X] addLineStation(stationService 아래 부분) -> Sections
  - [ ] removeLineStation(stationService 아래 부분) -> Sections
  - [ ] findUpStation -> Sections
  - [ ] findLines(return 부분) -> LineResponse
- [ ] Service의 로직 Domain으로 이동
  - [X] getStations -> Sections
  - [X] addLineStation(stationService 아래 부분) -> Sections
  - [ ] removeLineStation(stationService 아래 부분) -> Sections
  - [ ] findUpStation -> Sections
  - [ ] findLines(return 부분) -> LineResponse
