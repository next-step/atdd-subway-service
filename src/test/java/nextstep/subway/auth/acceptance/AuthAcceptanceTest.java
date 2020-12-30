package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.BaseTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends BaseTest {
	public static final String EMAIL = "test@test.com";
	public static final String PASSWORD = "testpass";
	public static final String WRONG_PASSWORD = "testpass1";
	public static final int AGE = 20;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	public void setUp() {
		super.setUp();
		MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
	}

	@DisplayName("로그인을 시도")
	@Test
	void myInfoWithBearerAuth() {
		//when
		ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
		//then
		로그인_됨(response);
	}

	@DisplayName("잘못된 패스워드로 로그인 시도, 로그인 실패한다.")
	@Test
	void myInfoWithBadBearerAuth() {
		//when
		ExtractableResponse<Response> response = 로그인_요청(EMAIL, WRONG_PASSWORD);
		//then
		로그인_실패됨(response);
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

	private void 로그인_됨(ExtractableResponse<Response> response) {

		TokenResponse tokenResponse = response.as(TokenResponse.class);
		boolean isValid = jwtTokenProvider.validateToken(tokenResponse.getAccessToken());

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(isValid).isTrue()
		);
	}

	private void 로그인_실패됨(ExtractableResponse<Response> response) {

		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
