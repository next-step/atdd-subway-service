<br>

## 🚉 지하철 노선도 애플리케이션
<br>

<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>

### 💻 나의 프로젝트의 목표와 성과
#### 목표
- 도메인 주도 개발을 위하여 Service의 비즈니스 로직을 Domain으로 옮기는 리팩터링을 진행
- 인수 테스트 기반으로 API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링
- 외부 라이브러리를 사용하는 직접 구현하는 로직을 검증해야 하므로 실제 객체를 활용하여 테스트 코드 작성
- 토큰 발급(로그인)을 검증하는 인수 테스트 구현

#### 성과
- 역 추가,제거 로직을 Service Layer에서 Line 도메인으로 이동하며 도메인 주도 개발 역량을 향상시킬 수 있었다. <br>
  [↳ 도메인 주도 설계에 관한 블로그 포스팅 (@yyy96)](https://velog.io/@yyy96/비즈니스로직)
  
- mock 서버와 dto를 정의하여 인수 테스트 외부 라이브러리에 대한 테스트 코드 작성 <br>
  [↳ 테스트 더블에 관한 블로그 포스팅 (@yyy96)](https://velog.io/@yyy96/Mock)

- 토큰 발급 인수 테스트를 진행하며 인증에 관하여 다시 한번 학습 <br>
  [↳ 인증과 테스트에 관한 블로그 포스팅 (@yyy96)](https://velog.io/@yyy96/인증)

- `디미터의 법칙(Law of Demeter)`을 준수하며 객체 지향적 설계 리팩터링 <br>
  ↳ Sections에서 매번 section의 정보를 가져올 것이 아니라 section 에서 따로 stationId 가 일치하는지 메서드를 생성


<br>
<br>


### [🚆 도메인 주도 개발 및 인수 테스트 통합 (branch)](https://github.com/yyy96/ddd-subway-service/tree/atdd1)
### [🚆 경로 조회 기능 추가 (branch)](https://github.com/yyy96/ddd-subway-service/tree/atdd2)
![image](https://user-images.githubusercontent.com/65826145/196177442-bec42307-05a6-4688-91d2-08fd33dafc82.png)
### [🚆 나의 즐겨찾기 기능과 토큰 발급 인수 테스트 (branch)](https://github.com/yyy96/ddd-subway-service/tree/atdd3)
![image](https://user-images.githubusercontent.com/65826145/196177583-22f13ff6-dfc5-49ed-963a-78cbb86f10d0.png)

<br>
<br>

### [📝 코드 리뷰 및 리팩터링 과정](https://github.com/next-step/atdd-subway-service/pulls?q=is%3Apr+is%3Aclosed+author%3Ayyy96)
![image](https://user-images.githubusercontent.com/65826145/196175541-7892eefb-98d5-416e-8394-e2a3ad4122e3.png)
![image](https://user-images.githubusercontent.com/65826145/196175403-9826b4bc-1285-4603-8246-211c226b2c21.png)


