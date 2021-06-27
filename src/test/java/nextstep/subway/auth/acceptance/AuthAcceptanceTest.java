package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;

@DisplayName("Auth 관련 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        ExtractableResponse<Response> joinResponse
            = 회원_생성_되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> loginResponse
            = 회원_로그인_요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> memberInfoResponse
            = 회원_정보_조회(loginResponse);

        // then
        회원_정보_조회됨(memberInfoResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response
            = 회원_로그인_요청(NEW_EMAIL, NEW_PASSWORD);

        // then
        회원_로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response
            = 회원_정보_조회_잘못된_토큰();

        // then
        회원_정보_조회_실패(response);
    }

    private static ExtractableResponse<Response> 회원_정보_조회(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        return RestAssured.given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all().extract();
    }

    private static void 회원_정보_조회됨(ExtractableResponse<Response> memberInfoResponse) {
        assertThat(memberInfoResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        MemberResponse member = memberInfoResponse.as(MemberResponse.class);
        assertThat(member.getAge()).isEqualTo(AGE);
        assertThat(member.getEmail()).isEqualTo(EMAIL);
    }

    private static ExtractableResponse<Response> 회원_정보_조회_잘못된_토큰() {
        TokenResponse tokenResponse = new TokenResponse("INVALID_TOKEN");
        return RestAssured.given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all().extract();
    }

    public static TokenResponse 회원_로그인_되어_있음(String email, String password) {
        return 회원_로그인_요청(email, password).as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 회원_로그인_요청(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/login/token")
            .then().log().all().extract();
    }

    private static void 회원_로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static void 회원_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
