package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static String 로그인_요청_후_토큰_가져오기(String email, String password) {
        TokenResponse tokenResponse = 로그인_요청(email, password).as(TokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    /*
    Feature: 로그인 기능
        Scenario: 로그인을 시도한다.
            Given 회원 등록되어 있음
            When 로그인 요청
            Then 로그인 됨
     */
    @Test
    @DisplayName("로그인 기능")
    void scenario1() {
        //given
        회원_생성을_요청("test@email.com", "1234", 10);

        //when
        TokenResponse tokenResponse = 로그인_요청("test@email.com", "1234")
            .as(TokenResponse.class);

        //then
        Assertions.assertThat(tokenResponse.getAccessToken()).isNotNull();

    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

}
