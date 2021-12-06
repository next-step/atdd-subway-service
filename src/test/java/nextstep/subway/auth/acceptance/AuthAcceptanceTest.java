package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

public class AuthAcceptanceTest extends AcceptanceTest {
    private TokenRequest 등록된_사용자_로그인_요청 = TokenRequest.of(EMAIL, PASSWORD);
    private TokenRequest 등록되지_않은_사용자_로그인_요청 = TokenRequest.of(anyString(), anyString());
    private TokenRequest 비밀번호_불일치_로그인_요청 = TokenRequest.of(EMAIL, anyString());
    private static final String 유효하지_않은_토큰_회원_미존재 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJub3Rmb3VuZEBub3Rmb3VuZC5jb20iLCJpYXQiOjE2Mzg3NjQyMDIsImV4cCI6MTYzODc2NzgwMn0.JjTAu_iv-19kUHAnffR-v6Gmy0_sC1OtIB-PWD3pPfI";
    private static final String 유효하지_않은_토큰_만료됨 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDAwMDAwMDAsImV4cCI6MTYwMDAwMDAwMH0.MxezzXBO7gnocwzvzN522EutLv9t2mMnsot4XKt8fO0";

    private static final String LOGIN_TOKEN_URI = "/login/token";
    private static final String MEMBERS_ME_URI = "/members/me";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("로그인 기능을 구현한다")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청함(등록된_사용자_로그인_요청);

        // then
        String 토큰 = 로그인을_성공하면_토큰을_발급받는다(response);
        발급한_로그인_토큰이_이메일과_일치함(토큰, 등록된_사용자_로그인_요청);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> 비밀번호불일치_응답 = 로그인_요청함(비밀번호_불일치_로그인_요청);

        // then
        로그인_인증_실패함(비밀번호불일치_응답);

        // when
        ExtractableResponse<Response> 존재하지않은_ID_응답 = 로그인_요청함(등록되지_않은_사용자_로그인_요청);

        로그인_인증_실패함(존재하지않은_ID_응답);
    }

    private AbstractIntegerAssert<?> 로그인_인증_실패함(ExtractableResponse<Response> 비밀번호불일치_응답) {
        return assertThat(비밀번호불일치_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> 유효하지_않은_토큰_회원_미존재_응답 = 토큰을_포함하여_마이페이지를_요청함(유효하지_않은_토큰_회원_미존재);

        // then
        로그인_인증_실패함(유효하지_않은_토큰_회원_미존재_응답);

        // when
        ExtractableResponse<Response> 유효하지_않은_토큰_만료됨_응답 = 토큰을_포함하여_마이페이지를_요청함(유효하지_않은_토큰_만료됨);

        // then
        로그인_인증_실패함(유효하지_않은_토큰_만료됨_응답);
    }

    private ExtractableResponse<Response> 토큰을_포함하여_마이페이지를_요청함(String invalidTokenNotFound) {
        return RestAssured.given().log().all()
                .auth().oauth2(invalidTokenNotFound)
                .when().get(MEMBERS_ME_URI)
                .then().log().all().extract();
    }

    private void 발급한_로그인_토큰이_이메일과_일치함(String token, TokenRequest request) {
        String payload = jwtTokenProvider.getPayload(token);
        assertThat(payload).isEqualTo(request.getEmail());
    }

    public static ExtractableResponse<Response> 로그인_요청함(TokenRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(LOGIN_TOKEN_URI)
                .then().log().all().extract();
    }

    public static String 로그인을_성공하면_토큰을_발급받는다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getObject("", TokenResponse.class).getAccessToken();
    }
}
