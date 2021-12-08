<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-service">
</p>

<br>

# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 3단계 - 인증을 통한 기능 구현

- [ ] **토큰 발급 기능 (로그인) 인수 테스트 만들기**
  - [x] 이메일과 패스워드를 이용하여 요청 시 `accessToken` 을 응답하는 기능을 구현하기
  - [ ] `AuthAcceptanceTest` 을 만족하도록 구현하면 됨
  - [ ] `AuthAcceptanceTest` 에서 제시하는 예외 케이스도 함께 고려하여 구현하기
  - [ ] 유효하지 않은 토큰으로 `/members/me` 요청을 보낼 경우에 대한 예외 처리
- [ ] **인증 - 내 정보 조회 기능 완성하기**
  - [ ] `MemberAcceptanceTest 클래스의 `manageMyInfo` 메서드에 인수 테스트를 추가하기
  - [ ] 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
  - [ ] 로그인 후 발급 받은 토큰을 포함해서 요청 하기
  - [ ] `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
  - [ ] `@AuthenticationPrincipal` 과 `AuthenticationPrincipalArgumentResolver`을 활용하기
  - [ ] 아래의 기능이 제대로 동작하도록 구현하기
  ```@GetMapping("/members/me")
  public ResponseEntity<MemberResponse> findMemberOfMine(LoginMember loginMember) {
  MemberResponse member = memberService.findMember(loginMember.getId());
  return ResponseEntity.ok().body(member);
  }
  
  @PutMapping("/members/me")
  public ResponseEntity<MemberResponse> updateMemberOfMine(LoginMember loginMember, @RequestBody MemberRequest param) {
  memberService.updateMember(loginMember.getId(), param);
  return ResponseEntity.ok().build();
  }
  
  @DeleteMapping("/members/me")
  public ResponseEntity<MemberResponse> deleteMemberOfMine(LoginMember loginMember) {
  memberService.deleteMember(loginMember.getId());
  return ResponseEntity.noContent().build();
  }
  ```

- [ ] 인증 - **즐겨 찾기 기능 완성하기**
  - 즐겨찾기 기능을 완성하기
  - 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

  ```
  Feature: 즐겨찾기를 관리한다.

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음

  Scenario: 즐겨찾기를 관리
    When 즐겨찾기 생성을 요청
    Then 즐겨찾기 생성됨
    When 즐겨찾기 목록 조회 요청
    Then 즐겨찾기 목록 조회됨
    When 즐겨찾기 삭제 요청
    Then 즐겨찾기 삭제됨
  ```
<br>

## 🚀 Getting Started

### Install

#### npm 설치

```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
