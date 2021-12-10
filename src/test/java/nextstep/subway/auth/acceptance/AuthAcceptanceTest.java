package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

	private static String EMAIL = "test@email.com";
	private static String PASSWORD = "testpassword1234";

	@BeforeEach
	public void setUp() {
		super.setUp();
		// given
		회원_생성을_요청(EMAIL, PASSWORD, 20);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		// when
		ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

		// then
		로그인_성공(response);
	}

	private void 로그인_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		// when
		ExtractableResponse<Response> response = 로그인_요청(EMAIL, "wrongPassword");

		// then
		로그인_실패(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		// when
		String invalidateToken = "invalidateToken";

		ExtractableResponse<Response> response = RestAssured
			.given()
			.log().all()
			.auth().oauth2(invalidateToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then()
			.log().all()
			.extract();

		// then
		인증_실패(response);
	}

	private void 로그인_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	private void 인증_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

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
}