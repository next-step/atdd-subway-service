package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
		// Scenario : 로그인을 시도 시나리오
		// Given : 회원 등록되어 있음
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When : 로그인 요청
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		ExtractableResponse<Response> tokenResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		// Then : 로그인 됨
		assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
		// Scenario : 로그인 실패 시나리오
		// Given : 회원 등록되어있지 않음
		// When : 로그인 요청
		TokenRequest tokenRequest1 = new TokenRequest(EMAIL, PASSWORD);
		ExtractableResponse<Response> tokenResponse1 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest1)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		// Then : 로그인 실패
		assertThat(tokenResponse1.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		// Given : 회원가입
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When : 로그인 요청(ID 틀림)
		TokenRequest tokenRequest2 = new TokenRequest("abc@email.com", PASSWORD);
		ExtractableResponse<Response> tokenResponse2 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest2)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		// Then : 로그인 실패
		assertThat(tokenResponse2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		// When : 회원 가입 후 로그인 요청(비밀번호 틀림)
		TokenRequest tokenRequest3 = new TokenRequest(EMAIL, "12345");
		ExtractableResponse<Response> tokenResponse3 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest3)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		// Then : 로그인 실패
		assertThat(tokenResponse3.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
		// Scenario : 유효하지 않은 토큰으로 /members/me 요청할 경우 에러 시나리오
		// Given : 회원등록 됨
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When : 로그인 요청
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		// Then : 로그인 됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		// Given : 토큰 수령
		TokenResponse tokenResponse = response.as(TokenResponse.class);
		String token = tokenResponse.getAccessToken();
		String worngToken = "abc";
		// When : 유효하지 않은 토큰으로 /members/me 요청
		ExtractableResponse<Response> errorResponse = RestAssured
			.given().log().all()
			.auth().oauth2(worngToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
		// Then : 에러 발생
		assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
