## 요구사항

- [ ] LineService 리팩터링
    - [ ] saveLine
    - [ ] findLines
    - [ ] findLineById
    - [ ] findLineResponseById
    - [ ] deleteLineById
    - [ ] updateLine
    - [ ] addLineStation
    - [ ] removeLineStation
    - [ ] getStations
- [ ] LineSectionAcceptanceTest 리팩터링

## 요구사항 설명

1. Domain으로 옮길 로직을 찾기
   스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   정상 동작 확인 후 기존 로직 제거
   (선택) 인수 테스트 통합
   API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
   반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
   인수 조건 예시
4. 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
5. 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기
