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
- [x] LineService에 존재하는 스프링 빈을 사용하거나 의존하는 로직을 제외한 비즈니스 로직을 도메인으로 옮기기
  - [x] 노선에 할당된 지하철 역 목록을 조회
  - [x] 노선 수정 세분화
  - [x] 노선에 지하철 역 추가
    - [x] 구간 재조정
    - [x] 예외 케이스
      - [x] 상행, 하행역이 이미 모두 등록되어 있는 경우
      - [x] 상행, 하행역 둘 중 하나도 노선에 포함되어 있지 않은 경우
  - [x] 노선에 할당된 지하철 역 제거
    - [x] 구간 병합
    - [x] 노선에 포함되지 않은 역을 제거하려는 경우
    - [x] 구간이 하나인 노선에서 역을 제거하려는 경우
  - [x] Sections 일급 컬렉션 생성
  - [x] Distance 값 객체 생성
  - [x] Line 클래스 내 빌더 패턴을 이용한 객체 생성 구현
- [x] 도메인 테스트
  - [x] Line
  - [x] Section
  - [x] Sections
  - [x] Distance

## 2단계 - 경로 조회 기능
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기
  - [x] 예외
    - [x] 출발역과 도착역이 같은 경우
    - [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
    - [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우
#### 요청 / 응답 포맷
Request
```http request
HTTP/1.1 200
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
Content-Type=application/json; charset=UTF-8
```
Response
```http request
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 May 2020 14:54:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "stations": [
        {
            "id": 5,
            "name": "양재시민의숲역",
            "createdAt": "2020-05-09T23:54:12.007"
        },
        {
            "id": 4,
            "name": "양재역",
            "createdAt": "2020-05-09T23:54:11.995"
        },
        {
            "id": 1,
            "name": "강남역",
            "createdAt": "2020-05-09T23:54:11.855"
        },
        {
            "id": 2,
            "name": "역삼역",
            "createdAt": "2020-05-09T23:54:11.876"
        },
        {
            "id": 3,
            "name": "선릉역",
            "createdAt": "2020-05-09T23:54:11.893"
        }
    ],
    "distance": 40
}

```

## 3단계 - 인증을 통한 기능 구현
- [x] 토큰 발급 기능(로그인) 인수 테스트 만들기
  - [x] 로그인 성공
  - [x] 로그인 실패
  - [x] 유효하지 않은 토큰
- [x] 인증 - 내 정보 조회 기능 완성하기
  - [x] 내 정보 조회, 수정, 삭제 기능에 대한 인수 테스트 작성(로그인 후 발급 받은 토큰을 포함해서 요청하기)
  - [x] 토큰을 확인하여 로그인 정보를 받아올 수 있도록 구현하기(`@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`를 활용)
- [x] 인증 - 즐겨 찾기 기능 완성하기
  - [x] 인증을 포함하여 ATDD 사이클을 통해 기능 구현하기
  - [x] 즐겨 찾기 생성
  - [x] 즐겨 찾기 목록 조회
  - [x] 즐겨 찾기 삭제

## 4단계 - 요금 조회
- [x] 경로 조회 시 거리 기준 요금 정보 포함하기
  - 기본 운임(10km 이내): 1,250원
  - 이용 거리 초과 시 추가운임비 부과
    - 10km 초과 ~ 50km 까지(5km 마다 100원)
    - 50km 초과 시(8km 마다 100원) 
- [x] 노선별 추가 요금 정책 추가
  - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - 경로 중 추가요금이 있는 노선을 환승하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
- [x] 연령별 할인 정책 추가
  - 청소년: 13세 이상 ~ 19세 미만
    - 운임에서 350원을 공제한 금액의 20% 할인
  - 어린이: 6세 이상 ~ 13세 미만
    - 운임에서 350원을 공제한 금액의 50% 할인
- [x] 지하철 경로 검색 인수 조건 수정
  ```shell
  Feature: 지하철 경로 검색
  
    Scenario: 두 역의 최단 거리 경로를 조회
      Given 지하철역이 등록되어있음
      And 지하철 노선이 등록되어있음
      And 지하철 노선에 지하철역이 등록되어있음
      When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
      Then 최단 거리 경로를 응답
      And 총 거리도 함께 응답함
      And ** 지하철 이용 요금도 함께 응답함 **
  ```
