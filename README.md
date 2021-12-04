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


## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항

- LineSectionAcceptanceTest 리팩터링
- LineService 리팩터링

### 기능목록

- [X] LineSectionAcceptanceTest 리팩터링
  - [X] 지하철 구간 추가 테스트 새로 작성
  - [X] 기존 구간 등록 테스트를 삭제한다
  - [X] 지하철 구간 삭제 테스트 새로 작성
  - [X] 기존 구간 삭제 테스트를 삭제한다
  - [X] INTERNAL_SERVER_ERROR 에러 반환을 BAD_REQUEST 반환으로 변경
- [X] LineService 리팩터링
  - [X] Sections에 getStations() 메소드 추가
  - [X] RuntimeException -> CustomException 변경
  - [X] 구간 추가 기능을 Sections로 이전
  - [X] 구간 삭제 기능을 Sections로 이전
  - [X] 2depth 코드 리팩토링
  - [X] Distance 도메인 생성

## 2단계 - 경로 조회 기능

### 요구사항

- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현하기

#### Request

```http request
HTTP/1.1 200 
Request method: GET
Request URI: http://localhost:55494/paths?source=1&target=6
Headers: Accept=application/json Content-Type=application/json; charset=UTF-8
```

#### Response

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

### 기능목록

- [ ] 경로를 검색하면 최단거리와 역 순서를 반환한다
  - [X] 인수테스트 작성
  - [ ] 기능 구현
