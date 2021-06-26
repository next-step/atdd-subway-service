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


## 요구사항 
*[x] LineSectionAcceptanceTest 리팩터링
*[x] LineService 리팩터링
*[ ] 최단 경로 조회 인수 테스트 만들기
*[ ] 최단 경로 조회 기능 구현하기


## 작업 목록
1. Domain으로 옮길 로직을 찾기<br>
   스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정<br>
   객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기<br>
   서비스 레이어에서 옮겨 올 로직의 기능을 테스트<br>
   SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기<br>
   기존 로직을 지우지 말고 새로운 로직을 만들어 수행<br>
   정상 동작 확인 후 기존 로직 제거

## API 명세

* GET /paths 
  * Request
    1. int "source"
    2. int "target"
    <br>
  * Response
    ```json
    {
      "stations": 
      [
        {
          "id": 1,
          "name": "강남역",
          "createdAt": "2020-05-09T23:54:12.007"
        },
        {
          "id": 6,
          "name": "양재역",
          "createdAt": "2020-05-10T23:54:12.007"
        }
      ],
      "distance": 10
    }
    ```
