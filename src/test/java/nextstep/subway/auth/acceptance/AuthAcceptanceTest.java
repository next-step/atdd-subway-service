package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // Given
        ExtractableResponse<Response> tokenResponse = 로그인을_요청_한다(EMAIL, PASSWORD);

        // When
        ExtractableResponse<Response> memberInfoResponse = 토큰값으로_회원_정보_요청(tokenResponse);

        // Then
        토큰값으로_회원_정보_조회_성공(memberInfoResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // When
        ExtractableResponse<Response> tokenResponse = 로그인을_요청_한다(EMAIL, "wrongPassword");

        // Then
        로그인_요청_실패(tokenResponse);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // When
        ExtractableResponse<Response> memberInfoResponse = 토큰값으로_회원_정보_요청("wrongToken");

        // Then
        토큰값으로_회원_정보_조회_실패(memberInfoResponse);
    }

    @Test
    void 로그인을_시도한다() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // When
        ExtractableResponse<Response> response = 로그인을_요청_한다(EMAIL, PASSWORD);

        // Then
        로그인_요청_성공(response);
    }

    public static ExtractableResponse<Response> 로그인을_요청_한다(String email, String password) {
        TokenRequest params = new TokenRequest(email, password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    public static void 로그인_요청_실패(ExtractableResponse<Response> tokenResponse) {
        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 토큰값으로_회원_정보_요청(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        return 토큰값으로_회원_정보_요청(tokenResponse.getAccessToken());
    }

    public static ExtractableResponse<Response> 토큰값으로_회원_정보_요청(String token) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 토큰값으로_회원_정보_조회_성공(ExtractableResponse<Response> memberInfoResponse) {
        assertThat(memberInfoResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 토큰값으로_회원_정보_조회_실패(ExtractableResponse<Response> memberInfoResponse) {
        assertThat(memberInfoResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
