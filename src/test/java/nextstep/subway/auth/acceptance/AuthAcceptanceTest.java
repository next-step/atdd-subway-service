package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
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
import nextstep.subway.member.MemberAcceptanceTest;

public class AuthAcceptanceTest extends AcceptanceTest {

	private final String EMAIL = MemberAcceptanceTest.EMAIL;
	private final String PASSWORD = MemberAcceptanceTest.PASSWORD;

	@BeforeEach
	void initUser() {
		MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, MemberAcceptanceTest.AGE);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		// when
		ExtractableResponse<Response> response = 로그인_토큰_요청(EMAIL, PASSWORD);

		// then
		로그인_토큰_조회됨(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		// when
		ExtractableResponse<Response> passwordFailResponse = 로그인_토큰_요청(EMAIL, "123456");
		ExtractableResponse<Response> emailFailResponse = 로그인_토큰_요청("email@gmail.com", PASSWORD);

		//then
		로그인_토큰_실패됨(passwordFailResponse);
		로그인_토큰_실패됨(emailFailResponse);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		// given
		TokenResponse tokenResponse = 로그인_토큰_요청(EMAIL, PASSWORD).as(TokenResponse.class);

		// then
	}

	public static ExtractableResponse<Response> 로그인_토큰_요청(String email, String password) {
		return RestAssured
			.given().log().all()
			.auth().preemptive().basic(email, password)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(new TokenRequest(email, password))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/login/token")
			.then().log().all().extract();
	}

	private void 로그인_토큰_조회됨(ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull()
		);
	}

	private void 로그인_토큰_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
