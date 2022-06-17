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

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    void setup() {
        MemberAcceptanceTest.회원_생성을_요청("minho@minho.com", "미노", 1);
    }

    /**
     * Given 생성된 회원정보가 있고
     * When 생성된 회원정보로 토큰을 조회 요청하면
     * Then 토큰을 가져온다
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 회원_토큰_조회("minho@minho.com", "미노");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getMap(".")).hasSize(1);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 회원_토큰_조회("minho@minho.com", "마이노");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        TokenResponse unauthorized_token = new TokenResponse("unauthorized token");

        // when
        ExtractableResponse<Response> findResponse = MemberAcceptanceTest.내_정보_조회(unauthorized_token.getAccessToken(), "a@a.com", "wow");

        // then
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 회원_토큰_조회(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().log().all()
                .post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 토큰_가져오기(String email, String password) {
        return (String) 회원_토큰_조회(email, password).jsonPath().getMap(".").get("accessToken");
    }

}
