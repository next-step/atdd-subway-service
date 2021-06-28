##요구사항
- [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [ ] 인증 - 내 정보 조회 기능 완성하기
- [ ] 인증 - 즐겨 찾기 기능 완성하기

##요구사항 설명
###토큰 발급 인수 테스트
- [x] 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
- [x] AuthAcceptanceTest 인수 테스트 만들기

###내 정보 조회 기능
- [x] 인수 테스트 
  - [x] MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
  - [x] 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
  - [x] 로그인 후 발급 받은 토큰을 포함해서 요청 하기
- [x] 토큰을 통한 인증
  - /members/me 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
  - @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
  - 아래의 기능이 제대로 동작하도록 구현하기

###즐겨 찾기 기능 구현하기
- [ ] 즐겨찾기 기능을 완성하기
- [ ] 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기