###1단계 - 인수 테스트 기반 리팩터링
##### 요구사항
* LineService 비즈니스 로직을 도메인으로 옮기기
  * 노선 생성
    1. 노선생성 후 지하철 역 목록조회 로직 Sections로 이동
  * 노선 전체 조회
    1. 노선 내 지하철 역 목록조회 로직 Sections로 이동
  * 노선 1건 조회
    1. 노선 내 지하철 역 목록조회 로직 Sections로 이동
    2. RuntimeException -> IllegalArgumentException
    3. LineExceptionHandler 생성
    4. 실패하는 인수테스트코드 작성
  * 노선 수정
    1. 노선 조회부분 기존 메서드 재사용
  * 노선에 신규 구간 등록
    1. 기존 구간별 제약조건에 따른 검증로직 Sections로 이동
    2. 구간추가 후 기존역 갱신 로직 Sectinos로 이동
    3. RuntimeException -> IllegalArgumentException
  * 

