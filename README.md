<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
## 1단계 - 인수 테스트 기반 리팩터링
- [ ] LineService에 존재하는 스프링 빈을 사용하거나 의존하는 로직을 제외한 비즈니스 로직을 도메인으로 옮기기
  - [ ] 노선에 할당된 지하철 역 목록을 조회
  - [ ] 노선 수정 시 Line 객체가 아닌 수정에 필요한 값을 전달하도록 변경
  - [ ] 노선에 지하철 역 추가
    - [ ] 구간 재조정
    - [ ] 예외 케이스
      - [ ] 상행, 하행역이 이미 모두 등록되어 있는 경우
      - [ ] 상행, 하행역 둘 중 하나도 노선에 포함되어 있지 않은 경우
  - [ ] 노선에 할당된 지하철 역 제거
    - [ ] 구간 병합
    - [ ] 노선에 포함되지 않은 역을 제거하려는 경우
    - [ ] 구간이 하나인 노선에서 역을 제거하려는 경우
  - [ ] Sections 일급 컬렉션 생성
  - [x] Distance 값 객체 생성
  - [x] Line 클래스 내 빌더 패턴을 이용한 객체 생성 구현
- [ ] 도메인 테스트
  - [ ] Line
  - [ ] Section
  - [ ] Sections
  - [x] Distance
