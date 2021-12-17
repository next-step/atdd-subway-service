package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuthAcceptanceTest extends AcceptanceTest {
    private final String email = "abc@abc.co.kr";
    private final String password = "1234";
    private final int age = 20;
    private LoginMember loginMember;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(email, password, age);
        MemberAcceptanceTest.회원_생성됨(createResponse);
        ExtractableResponse<Response> response = MemberAcceptanceTest.회원_정보_조회_요청(createResponse);
        loginMember = response.jsonPath().getObject(".", LoginMember.class);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> tokenResponse = getToken(email, password, age);
        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse token = tokenResponse.jsonPath().getObject(".", TokenResponse.class);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .body(loginMember)
                .contentType("application/json")
                .when().get("/members/me")
                .then().log().all().extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = getToken(email, "123", age);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth()
                .oauth2("12345")
                .contentType("application/json")
                .body(loginMember)
                .when().get("/members/me")
                .then().log().all().extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> getToken(String email, String password, int age){
        TokenRequest tokenRequest = new TokenRequest(email, password);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all().extract();
        return response;
    }

}
