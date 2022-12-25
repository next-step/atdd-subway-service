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

## Step1 - 인수 테스트 기반 리팩터링

#### 요구사항 1
- LineService 리팩터링
#### 요구사항 2
- (선택) LineSectionAcceptanceTest 리팩터링
#### 구현 리스트
- [x] 기존 서비스 복사하기 ( strangler pattern )
- [x] Domain으로 옮길 로직을 찾기
    - [x] [1]saveLine메소드의 getStations.
    - [x] [2]findLines메소드의 persistLines.stream().
    - [x] [3]findLineResponseById메소드의 getStations.
    - [x] [4]addLineStation메소드의 line.getSections().stream(), line.getSections().add
    - [x] [5]removeLineStation메소드의 line.getSections().stream(), line.getSections().add, ifPresent
    - [x] [6]getStations메소드의 line.getSections().stream()
    - [x] [7]findUpStation메소드의 line.getSections().stream()
- [x] Domain의 단위 테스트를 작성하기
    - [x] [1] ~ [7] 단위 테스트 작성
- [x] 로직을 옮기기
    - [x] 새로운 로직 만들어서 수행
    - [x] 정상 동작 확인 후 기존 로직 제거 
- [x] 인수 테스트 통합 리팩토링

## Step2 - 경로 조회 기능
#### Step - 리뷰어 리뷰사항
- [x] 파라미터 전달 : 다중 파라미터 -> 객체로 개선 
- [x] Optional활용 : 파라미터 확실한 객체만 넘기게 개선
- [x] indent : depth 2단계를 1단계로 개선
- [x] 메소드 참조가 가능하다면, 메소드 참조로 개선 ( 가독성 증가 및 코드량 감소 )
- [x] LineTest : 도메인 내부 함수로만 테스트가 구성되도록 개선
- [x] 객체지향 : 비즈니스 로직을 도메인 내부에서 해결하도록 개선 ( 메시지 전달 )
- [x] 일급 콜렉션 : List<>같은 Collection의 경우, 일급 컬렉션을 사용하도록 개선

#### 요구사항 1
- 최단 경로 조회 인수 테스트 만들기
#### 요구사항 2
- 최단 경로 조회 기능 구현하기
#### 구현 리스트
1. 최단 경로 조회 인수 테스트 만들기
- [x] 다익스트라 알고리즘 인수 테스트 만들기
2. 최단 경로 조회 기능 구현하기 ( Outside In으로 구현 )
- [x] 컨트롤러 레이어 구현
- [x] 서비스 레이어 구현 - 서비스 테스트 우선작성으로 기능 보호 후 구현 ( Mock객체 활용 )
- [x] 로직 검증 - 외부 라이브러리를 검증하므로 가급적 실제 객체 사용
- [x] Happy 케이스에 대한 부분 구현
#### Step2 - 리뷰어 1차 리뷰사항
- [x] 삭제 - 주석처리 제거
- [x] 경고표시되는 소스 수정
- [x] 스트림 알고 사용하기. List일 경우, 제공해주는 forEach함수 존재
- [x] TestStation 제거
- [x] 외부 라이브러리의 종속성을 캡슐화

## Step3 - 인증을 통한 기능구현
#### Step2 - 리뷰어 리뷰사항
- [x] 외부 라이브러리를 상속받은 만큼 메시지 던져보기
- [x] 예외 테스트도 추가적으로 작성
#### 요구사항 1
- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기
#### 구현 리스트
1. 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [x] 이메일, 패스워드로 Access Tocken을 응답하는 인수테스트
- [x] 잘못된 토큰으로 요청을 보낼경우에 대한 예외 케이스 작성 [ 틀린 토큰, 유효하지 않은 토큰 ]
2. 인증 - 내 정보 조회 기능 완성하기
- [x] 로그인 요청시, 토큰을 확인하여 유저 정보를 받아올수 있게 처리하기
- [x] 로그인 요청시, 토큰을 확인하여 유저 정보를 수정할수 있게 처리하기
- [x] 로그인 요청시, 토큰을 확인하여 유저 정보를 삭제할수 있게 처리하기
3. 즐겨 찾기 기능 완성하기
- [x] 즐겨찾기 기능을 완성하기
- [x] 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기
- [x] 즐겨찾기 생성 기능 추가하기
- [x] 즐겨찾기 목록 조회 기능 추가하기
- [x] 즐겨찾기 삭제 기능 추가하기

## Step4 - 요금 조회
#### Step3 - 리뷰어 리뷰사항
- [x] 자신만의 좋아요 삭제하기 
- [x] 반복되는 코드 메소드로 분리하기
- [x] repository - 배열을 return 할 경우, 어차피 빈배열을 리턴하므로 Optional 삭제하기
#### 요구사항 1
- 경로 조회 시 거리 기준 요금 정보 포함하기
- 노선별 추가 요금 정책 추가
- 연령별 할인 정책 추가
#### 거리별 요금 정책
- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
  - 10km초과∼50km까지(5km마다 100원)
  - 50km초과 시 (8km마다 100원)
#### 구현 리스트
- [x] 경로 조회 시 거리 기준 요금 정보 포함하기
- [x] 노선별 추가 요금 정책 추가
- [x] 연령별 할인 정책 추가




