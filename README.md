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

## Step1
* 1.LinSectionAcceptanceTest 리팩토링
* 2.LineService 리팩토링(비즈니스로직 도메인으로 옮기기)
* 3.LineTest 추가

## Step2
* 1.step1 refactoring 일급콜렌션 사용 위임
* 2.PathAcceptanceTest 작성
* 3.경로 조회 수행하는 도메인 추가 및 테스트 작성

## Step3
* 1.토큰 발급(로그인)을 검증하는 인수 테스트(AuthAcceptanceTest) 작성
* 2.내 정보 조회 인수 테스트(MemberAcceptanceTest) 작성
* 3.즐겨찾기 인수 테스트(FavoriteAcceptanceTest) 작성

## Step4
* 1.1 거리별 요금 정책파악
*     기본운임(10km 이내) : 기본운임 1,250원
*     이용 거리초과 시 추가운임 부과
*     10km초과 ~ 50km까지(5km마다 100원)
*     50km초과 시(8km마다 100원)
* 1.2 노선별 추가 요금 정책(추가요금이 있는 노선을 환승하여 이용할 경우, 가장 높은 금액의 추가요금만 적용)
*     신분당선 900원 , 3호선 500원
* 1.3 로그인 사용자 연령별 요금 할인 적용(청소년 13세이상 ~19세 미만, 어린이 6세이상 ~13세 미만)
* 2.최단경로라이브러리 확인(Long 제네릭타입)
* 3.인수테스트(PathAcceptanceTest)