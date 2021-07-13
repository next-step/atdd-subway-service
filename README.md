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


## 4단계 요금조회 기능 명세서

- [ ] 노선별 추가 요금 정책 추가
    - [ ] 노선 추가 시 추가 요금 정보를 입력할 수 있다.
    - [ ] 노선 조회 시 추가 요금이 있는 경우 조회된다.
    - [ ] 노선의 추가 요금 정보를 수정할 수 있다.
- [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
    - [ ] 경로 조회 시 요금 정보를 전달한다.
        - [ ] 기본운임을 초과한 경우에는 추가 요금이 발생한다.
            - [ ] 경로에 노선이 한 개인 경우
                - [ ] 해당 노선의 추가 요금만큼 요금에 더해진다.
            - [ ] 경로에 노선이 한 개 이상인 경우
                - [ ] 추가 요금이 가장 큰 노선의 금액을 더한다.
        - [ ] 기본운임 이내인 경우에는 1,250원을 전달한다.
- [ ] 연령별 할인 정책 추가
    - [ ] 로그인한 사용자의 age가 13세 이상 ~ 19세미만인 경우 요금이 20%할인된 금액으로 적용된다.
    - [ ] 로그인한 사용자의 age가 6세 이상 ~ 13세 미만인 경우 요금이 50%할인된 금액으로 적용된다.
