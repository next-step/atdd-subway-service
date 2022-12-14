### Step1 요구사항
- LineService 리팩터링
- (선택) LineSectionAcceptanceTest 리팩터링


### Step1 LineService 리팩터링
> - [x] LineService의 비즈니스 로직을 도메인으로 옮기기
>   - [x] saveLine 리펙토링
>   - [x] getStations 리펙토링
>   - [x] sections 일급컬렉션 추가
>   - [x] findLine 리펙터링
>   - [x] findUpStation 리펙터링
>   - [x] removeLineStation 리펙터링
>   - [x] addLineStation 리펙터링
>   - [x] 예외 enum 리펙터링
> - [x] 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
> - [x] 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기


### Step1 피드백
> - [x] Vo에 Setter제거
> - [x] 도메인 단위 인수테스트 작성
> - [x] getter 방어적 복사 