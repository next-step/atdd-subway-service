package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("인증 관련 인수테스트")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 15;

    private TokenRequest 로그인정보;
    private TokenRequest 미등록정보;

    @BeforeEach
    public void setUp(){
        super.setUp();

        //given
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        로그인정보 = new TokenRequest(EMAIL, PASSWORD);
        미등록정보 = new TokenRequest("test@email.com", "1234");
    }

    /**
     * Feature: 로그인 기능
     *   Background
     *      Given 회원 등록되어 있음
     *
     *   Scenario: 로그인을 시도한다.
     *     When 로그인 요청
     *     Then 로그인 됨
     * */
    @Test
    @DisplayName("로그인을 시도한다.")
    void login(){

        //when
        TokenResponse tokenResponse = 로그인_요청(로그인정보).as(TokenResponse.class);

        //then
        토큰_검증(tokenResponse);

    }


    /**
     * Feature: 엑세스토큰 인증 기능
     * 
     *   Background
     *     Given 회원 등록되어 있음
     *
     *   Scenario: 나의정보를 확인한다.
     *     Given 로그인 되어있음 (엑세스토큰 발급)
     *     When  나의정보를 요청하면 (with 엑세스토큰)
     *     Then  나의정보가 응답됨
     * */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {

        //given
        TokenResponse tokenResponse = 로그인_요청(로그인정보).as(TokenResponse.class);

        //when
        MemberResponse response = 나의정보_요청(tokenResponse.getAccessToken()).as(MemberResponse.class);

        //then
        나의정보_조회됨(response);
    }


    /**
     * Feature: 인증토큰
     **
     *   Scenario: 로그인 실패
     *     When  회원 등록되지 않는 로그인 요청하면
     *     Then  로그인 실패됨
     * */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {

        //when
        ExtractableResponse<Response> response = 로그인_요청(미등록정보);

        //then
        로그인_실패됨(response);
    }



    /**
     * Feature: 나의 정보 조회기능 (인증불가 토근)
     *
     *   Scenario: 나의 정보를 조회한다.(인증불가 토큰)
     *     Given 인증불가 토근으로
     *     When  나의정보를 요청하면
     *     Then  나의정보 조회 실패됨
     * */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2NTQ2NzQ0NzcsImV4cCI6MTY1NDY3ODA3N30.JbtFjzck0lLRGTKNR7JvD71k_YJhPwBriHHrB2";

        //when
        ExtractableResponse<Response> response = 나의정보_요청(invalidToken);

        //then
        나의정보_조회_실패됨(response);
    }

    private void 토큰_검증(TokenResponse tokenResponse) {
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    private void 나의정보_조회됨(MemberResponse response) {
        assertThat(response.getId()).isNotNull();
        assertThat(response.getEmail()).isEqualTo(EMAIL);
        assertThat(response.getAge()).isEqualTo(AGE);
    }

    private void 나의정보_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest){

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post(" /login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 나의정보_요청(String accessToken){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

}
