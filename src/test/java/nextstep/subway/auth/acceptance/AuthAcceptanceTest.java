package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {


    @BeforeEach
    public void setUp() {
        super.setUp();

        //given
        회원_등록되어_있음("test@email.com", "password", 29);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when
        ExtractableResponse<Response> 로그인_토큰_요청 = 로그인_토큰_요청(new TokenRequest("test@email.com", "password"));

        //then
        assertThat(로그인_토큰_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(로그인_토큰_요청.as(TokenResponse.class).getAccessToken()).isNotEmpty();
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> 로그인_실패_요청 = 로그인_토큰_요청(new TokenRequest("test@email.com", "passwordfail"));

        //then
        assertThat(로그인_실패_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    @Disabled
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_토큰_요청(new TokenRequest("test@email.com", "password"));
        String token = response.as(TokenResponse.class).getAccessToken() + "fail";
        ExtractableResponse<Response> 유효하지_않은_토큰_요청 = MemberAcceptanceTest.회원_토근_조회_요청(response, token);

        //then
        assertThat(유효하지_않은_토큰_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 로그인_토큰_요청(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, int age) {
        return MemberAcceptanceTest.회원_생성을_요청(email, password, age);
    }

}