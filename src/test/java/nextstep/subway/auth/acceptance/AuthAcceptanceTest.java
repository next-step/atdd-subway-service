package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

	private static String EMAIL;
	private static String PASSWORD;

	@BeforeEach
	public void setup() {
		EMAIL = "chaeyun17@github.com";
		PASSWORD = "chaeyun123";

		MemberAcceptanceTest.회원_등록됨(EMAIL, PASSWORD);
	}

	@DisplayName("로그인을 시도한다")
	@Test
	void scenario1() {
		// when
		TokenResponse response = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

		// then
		로그인_응답됨(response);

		// when
		ExtractableResponse<Response> response2 = 로그인_요청(EMAIL, "Wrong_Password");

		// then
		로그인_실패(response2);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		// given
		TokenResponse tokenResponse = 로그인됨(EMAIL, PASSWORD);

		// when
		ExtractableResponse<Response> response = MemberAcceptanceTest.내_정보_조회_요청(tokenResponse);

		// then
		인증_성공_응답함(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		// when
		ExtractableResponse<Response> response = 내_정보_조회_토큰_없이_요청();

		// then
		내_정보_조회_실패(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		// given
		TokenResponse tokenResponse = new TokenResponse("invalid-token-123");

		// when
		ExtractableResponse<Response> response = MemberAcceptanceTest.내_정보_조회_요청(tokenResponse);

		// then
		내_정보_조회_실패(response);
	}

	private static void 인증_성공_응답함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private static void 로그인_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	private void 내_정보_조회_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	public static TokenResponse 로그인됨(String email, String password) {
		return 로그인_요청(email, password).as(TokenResponse.class);
	}

	private static ExtractableResponse<Response> 로그인_요청(String email, String password) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TokenRequest(email, password))
			.when().post("/login/token")
			.then().log().all()
			.extract();
	}

	private static void 로그인_응답됨(TokenResponse tokenResponse) {
		assertThat(tokenResponse.getAccessToken()).isNotNull();
	}

	private static ExtractableResponse<Response> 내_정보_조회_토큰_없이_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/members/me")
			.then().log().all()
			.extract();
	}

	public static Header getAuthHeader(TokenResponse tokenResponse) {
		return new Header("Authorization", "Bearer " + tokenResponse.getAccessToken());
	}

}
