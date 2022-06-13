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

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String WRONG_PASSWORD = "wrong_password";
    public static final int AGE = 20;

    public static final String NEW_EMAIL = "nextstep@email.com";
    public static final String NEW_PASSWORD = "nextstep";
    public static final int NEW_AGE = 25;

    @DisplayName("로그인 성공한다.")
    @Test
    void loginSuccess() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));

        // then
        로그인_성공(response);
    }

    @DisplayName("로그인 실패한다.")
    @Test
    void loginFail() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, WRONG_PASSWORD));

        // then
        로그인_실패(response);
    }

    @DisplayName("유효 토큰으로 회원(본인) 정보를 조회한다.")
    @Test
    void findMemberWithValidToken() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        TokenResponse tokenResponse = 로그인_성공(response);

        // when
        ExtractableResponse<Response> response2 = 본인_정보_조회(tokenResponse);

        // then
        본인_정보_조회_성공(response2);
    }

    @DisplayName("유효하지 않는 토큰으로 회원(본인) 정보를 조회한다.")
    @Test
    void findMemberWithInvalidToken() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_성공(response);

        // when
        ExtractableResponse<Response> response2 = 본인_정보_조회(new TokenResponse("TEST"));

        // then
        본인_정보_조회_실패(response2);
    }

    @DisplayName("유효 토큰으로 회원(본인) 정보를 수정한다.")
    @Test
    void updateMemberOfMineWithValidToken() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        TokenResponse tokenResponse = 로그인_성공(response);

        // when
        ExtractableResponse<Response> response2 = 본인_정보_수정(tokenResponse, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));

        // then
        본인_정보_수정_성공(response2);
    }

    @DisplayName("유효 토큰으로 회원(본인) 정보를 삭제한다.")
    @Test
    void deleteMemberOfMineWithValidToken() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        TokenResponse tokenResponse = 로그인_성공(response);

        // when
        ExtractableResponse<Response> response2 = 본인_정보_삭제(tokenResponse);

        // then
        본인_정보_삭제_성공(response2);
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all().extract();
    }

    public static TokenResponse 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(TokenResponse.class);
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 본인_정보_조회(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 본인_정보_수정(TokenResponse tokenResponse, MemberRequest memberRequest) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all().extract();
    }

    public static void 본인_정보_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 본인_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 본인_정보_수정_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 본인_정보_삭제(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/members/me")
                .then().log().all().extract();
    }

    public static void 본인_정보_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
