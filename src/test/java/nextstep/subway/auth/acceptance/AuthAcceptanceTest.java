package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.NEW_EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.NEW_PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String INVALID_TOKEN = "invalid token";

    @BeforeEach
    void init() {
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * Scenario: 로그인을 시도한다.
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인되어_있음(loginResponse);
    }

    /**
     * Scenario: 로그인에 실패한다.
     * Given 회원 등록되어 있음
     * When 틀린 비밀번호로 로그인 요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth 로그인 실패 - 틀린 비밀번호")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, NEW_PASSWORD);

        // then
        로그인_실패(response);
    }

    /**
     * Scenario: 로그인에 실패한다.
     * Given 회원 등록되어 있음
     * When 없는 이메일로 로그인 요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth 로그인 실패 - 없는 이메일")
    @Test
    void myInfoWithBadBearerAuth_2() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(NEW_EMAIL, PASSWORD);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = MemberAcceptanceTest.내_정보_조회_요청(INVALID_TOKEN);

        유효하지_않은_토큰(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();

    }

    public static String 로그인되어_있음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.as(TokenResponse.class)
                .getAccessToken();
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 유효하지_않은_토큰(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
