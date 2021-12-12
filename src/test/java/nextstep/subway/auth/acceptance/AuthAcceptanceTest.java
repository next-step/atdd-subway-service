package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.나의정보_조회;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL_TEST_INFO = "test@test.com";
    private static final String PASSWORD_TEST_INFO = "test1234";
    private static final int AGE_TEST_INFO = 12;
    private static final String INVALID_TOKEN = "wrong_token";

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = allowedMemberLogin(EMAIL_TEST_INFO, PASSWORD_TEST_INFO, AGE_TEST_INFO);
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        login_success(response, tokenResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = notAllowedMemberLogin(EMAIL_TEST_INFO, PASSWORD_TEST_INFO, AGE_TEST_INFO);
        login_fail(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        allowedMemberLogin(EMAIL_TEST_INFO, PASSWORD_TEST_INFO, AGE_TEST_INFO);
        TokenResponse invalidTokenResponse = new TokenResponse(INVALID_TOKEN);
        ExtractableResponse<Response> myInfoResponse = 나의정보_조회(invalidTokenResponse);
        login_fail(myInfoResponse);
    }

    private ExtractableResponse<Response> allowedMemberLogin(String email, String password, Integer age) {
        signUpMember(email, password, age);
        return login(email, password);
    }

    private ExtractableResponse<Response> notAllowedMemberLogin(String email, String password, Integer age) {
        signUpMember(email, password, age);
        String wrongPassword = password + "123";
        return login(email, wrongPassword);
    }

    private ExtractableResponse<Response> signUpMember(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> login(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all().
                 extract();
    }

    private void login_success(ExtractableResponse<Response> response, TokenResponse tokenResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    private void login_fail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
