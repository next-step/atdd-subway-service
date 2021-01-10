package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;


    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> tokenResponse = 로그인_요청(EMAIL, PASSWORD);

        로그인_성공됨(tokenResponse);
        로그인_토큰_받음(tokenResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //패스워드 틀림
        ExtractableResponse<Response> tokenResponse = 로그인_요청(EMAIL, "idontknow");

        로그인_실패됨(tokenResponse);

        //이메일 틀림
        ExtractableResponse<Response> tokenResponse2 = 로그인_요청("wef@ma.com", PASSWORD);

        로그인_실패됨(tokenResponse2);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        로그인_요청(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_정보_조회_요청("sdfsdfwefw");

        내_정보_조회_실패됨(findResponse);
    }

    private void 내_정보_조회_실패됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private void 로그인_토큰_받음(ExtractableResponse<Response> tokenResponse) {
        TokenResponse token = tokenResponse.as(TokenResponse.class);
        assertThat(token.getAccessToken()).isNotNull();
    }

    private void 로그인_실패됨(ExtractableResponse<Response> tokenResponse) {
        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_성공됨(ExtractableResponse<Response> tokenResponse) {
        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
         TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {

        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}
