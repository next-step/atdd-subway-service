package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.나의_회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

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

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String TEST_EMAIL = "email@email.com";
    private static final String TEST_PWD = "password";
    private static final int TEST_AGE = 15;

    @DisplayName("Bearer Auth 로그인을 시도한다.")
    @Test
    void myInfoWithBearerAuth() {

        // given
        회원_등록되어_있음();

        // when
        ExtractableResponse<Response> response = 로그인_요청();

        // then
        String accessToken = 로그인_됨(response);
        나의_회원_정보_조회_요청(accessToken);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청();

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        회원_등록되어_있음();

        // when
        ExtractableResponse<Response> response = 나의_회원_정보_조회_요청("invalid-token");

        // then
        토큰_검증_실패(response);
    }

    private void 회원_등록되어_있음() {
        ExtractableResponse<Response> response = 회원_생성을_요청(TEST_EMAIL, TEST_PWD, TEST_AGE);
        회원_생성됨(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String pwd) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new TokenRequest(email, pwd))
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청() {
        return 로그인_요청(TEST_EMAIL, TEST_PWD);
    }

    public static String 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
        return response.as(TokenResponse.class).getAccessToken();
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_검증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
    }
}
