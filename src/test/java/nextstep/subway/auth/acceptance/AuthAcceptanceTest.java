package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

	private static final String EMAIL = "wooahan@naver.com";
	private static final String OTHER_EMAIL = "other@naver.com";
	private static final String PASSWORD = "password";
	private static final String OTHER_PASSWORD = "otherPassword";
	private static final Integer AGE = 33;

	@BeforeEach
	@Override
	public void setUp() {
		super.setUp();
		MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		//when 로그인요청
		ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));

		//then 로그인 성공
		로그인_성공(response);
	}

	@DisplayName("Bearer Auth 로그인 실패 (이메일 불 일치)")
	@Test
	void myInfoWithBadEmailBearerAuth() {
		//when 존재하지 않는 회원정보로 로그인 요청
		ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(OTHER_EMAIL, PASSWORD));

		//then 로그인 실패
		로그인_실패(response);
	}

	@DisplayName("Bearer Auth 로그인 실패 (패스워드 불 일치)")
	@Test
	void myInfoWithBadPasswordBearerAuth() {
		//when 틀린 패스워드로 로그인 요청
		ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(EMAIL, OTHER_PASSWORD));

		//then 로그인 실패
		로그인_실패(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		//given 임의의 토큰 문자열 생성
		String loginToken = "login_token";

		//when 내 정보 조회 요청
		ExtractableResponse<Response> response = 내_정보_조회_요청(loginToken);

		//then 조회 실패
		내_정보_조회_실패(response);
	}

	private void 로그인_성공(ExtractableResponse<Response> response) {
		TokenResponse tokenResponse = response.as(TokenResponse.class);
		assertThat(tokenResponse).isNotNull();
		assertThat(tokenResponse.getAccessToken()).isNotBlank();
	}

	private ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post("/login/token")
			.then().log().all()
			.extract();
	}

	private void 로그인_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	private ExtractableResponse<Response> 내_정보_조회_요청(String loginToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(loginToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
	}

	private void 내_정보_조회_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

}
