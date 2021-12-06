package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String LOGIN_TOKEN_URI = "/login/token";
    private static final String MEMBERS_ME_URI = "/members/me";
    private static final String NOT_EXIST_EMAIL = "NOT_EXIST@EMAIL.COM";
    private static final String NOT_CORRECT_PASSWORD = "NOT_CORRECT_PASSWORD";
    private static final String INVALID_TOKEN_NOT_FOUND = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJub3Rmb3VuZEBub3Rmb3VuZC5jb20iLCJpYXQiOjE2Mzg3NjQyMDIsImV4cCI6MTYzODc2NzgwMn0.JjTAu_iv-19kUHAnffR-v6Gmy0_sC1OtIB-PWD3pPfI";
    private static final String INVALID_TOKEN_EXPIRED = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDAwMDAwMDAsImV4cCI6MTYwMDAwMDAwMH0.MxezzXBO7gnocwzvzN522EutLv9t2mMnsot4XKt8fO0";

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        TokenRequest request = TokenRequest.of(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(LOGIN_TOKEN_URI)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        TokenResponse tokenResponse = response.jsonPath().getObject("", TokenResponse.class);
        String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());

        // then
        assertThat(payload).isEqualTo(EMAIL);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        TokenRequest 비밀번호불일치 = TokenRequest.of(EMAIL, NOT_CORRECT_PASSWORD);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(비밀번호불일치)
                .when().post(LOGIN_TOKEN_URI)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // given
        TokenRequest 존재하지않은_ID = TokenRequest.of(NOT_EXIST_EMAIL, NOT_CORRECT_PASSWORD);

        // when
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(존재하지않은_ID)
                .when().post(LOGIN_TOKEN_URI)
                .then().log().all().extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> 회원_미존재 = RestAssured.given().log().all()
                .auth().oauth2(INVALID_TOKEN_NOT_FOUND)
                .when().get(MEMBERS_ME_URI)
                .then().log().all().extract();

        // then
        assertThat(회원_미존재.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // when
        ExtractableResponse<Response> 토큰_만료됨 = RestAssured.given().log().all()
                .auth().oauth2(INVALID_TOKEN_EXPIRED)
                .when().get(MEMBERS_ME_URI)
                .then().log().all().extract();

        // then
        assertThat(토큰_만료됨.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
