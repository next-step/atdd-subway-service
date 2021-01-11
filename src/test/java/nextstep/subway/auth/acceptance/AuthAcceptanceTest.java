package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        MemberAcceptanceTest.회원_등록_되어있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_됨(response);
        로그인_토큰_발급_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 존재하지 않는 회원 이메일")
    @Test
    void myInfoWithNotExistAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청("notexist@email.com", PASSWORD);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 틀린 비밀번호")
    @Test
    void myInfoWithWrongPasswordAuth() {
        //given
        MemberAcceptanceTest.회원_등록_되어있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL,  "Wrong" + PASSWORD);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> response = MemberAcceptanceTest.내_정보_조회_요청(new TokenResponse("WrongToken"));

        //then
        토큰_인증_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청 (String email, String password) {
        TokenRequest request = new TokenRequest(email, password);

        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/login/token")
            .then().log().all().extract();
    }

    public static void 로그인_됨 (ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_토큰_발급_됨 (ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인_되어있음 (String email, String password) {
        return 로그인_요청(email, password);
    }
}
