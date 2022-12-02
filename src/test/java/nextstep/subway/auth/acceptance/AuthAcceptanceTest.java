package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.common.exception.ErrorEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Feature: 로그인 기능
 *
 *   Background
 *     Given 회원 등록되어 있음
 *
 *   Scenario: 로그인을 시도한다.
 *     When 로그인 요청
 *     Then 로그인 됨
 *
 *   Scenario: 등록되지 않은 이메일로 로그인을 시도한다.
 *     When 로그인 요청
 *     Then 로그인 실패
 *
 *   Scenario: 등록되지 않은 비밀번호로 로그인을 시도한다.
 *     When 로그인 요청
 *     Then 로그인 실패
 *
 *   Scenario: 유효하지 않은 토큰으로 로그인을 시도한다.
 *     When 로그인 요청
 *     Then 로그인 실패
 *
 *   Scenario: 유효하지 않은 토큰으로 `/members/me` URL 을 요청한다.
 *     When URL 요청
 *     Then 페이지 접근 거부
 *
 */
public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @Test
    void Bearer_Auth_로그인_성공() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));

        로그인_성공(response);
    }

    @Test
    void Bearer_Auth_등록되지_않은_이메일로_로그인_요청시_실페() {
        final String UN_REGISTERED_EMAIL = "no@gmail.com";
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(UN_REGISTERED_EMAIL, PASSWORD));

        로그인_실패(response, ErrorEnum.NOT_EXISTS_EMAIL.message());
    }

    @Test
    void Bearer_Auth_등록되지_않은_비밀번호로_로그인_요청시_실페() {
        final String NOT_MATCH_PASSWORD = "0000";
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, NOT_MATCH_PASSWORD));

        로그인_실패(response, ErrorEnum.NOT_MATCH_PASSWORD.message());
    }

    @Test
    void Bearer_Auth_유효하지_않은_토큰으로_로그인_요청시_실패() {
    }

    @Test
    void Bearer_Auth_유효하지_않은_토큰으로_회원정보_페이지_요청() {
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank()
        );
    }

    private void 로그인_실패(ExtractableResponse<Response> response, String expectedErrorMessage) {
        String errorMessage = response.body().path("errorMessage").toString();
        assertAll(
                () -> assertThat(errorMessage).isEqualTo(expectedErrorMessage),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }
}
