# 🚀 3단계 - 인증을 통한 기능 구현

## 요구사항

### 토큰 발급

- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
  - [X] 검증하는 인수 테스트 만들기
  - [X] AuthAcceptanceTest 인수 테스트 만들기
- [X] 로그인 기능 구현
  - [X] 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
  - [X] AuthAcceptanceTest을 만족하도록 구현하면 됨
  - [X] AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현하기
- [X] Bearer Auth 유효하지 않은 토큰 인수 테스트
  - [X] 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리

### 내정보 조회

- [ ] 내정보 조회 인수 테스트 만들기
  - [ ] MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
  - [ ] 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
  - [ ] 로그인 후 발급 받은 토큰을 포함해서 요청 하기
- [ ] 토큰을 통한 인증 - 내 정보 조회 기능 완성하기
  - [ ] /members/me 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
  - [ ] @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
  - [ ] 아래의 기능이 제대로 동작하도록 구현하기

### 즐겨찾기

- [ ] 즐겨찾기 인수 테스트 만들기
  - [ ] 검증하는 인수 테스트 만들기
- [ ] 인증 - 즐겨 찾기 기능 완성하기
