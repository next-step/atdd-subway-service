package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

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
	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();

		MemberAcceptanceTest.회원_등록되어_있음("test@gmail.com", "test123", 20);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		ExtractableResponse<Response> response = 로그인_요청("test@gmail.com", "test123");

		로그인_성공(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		ExtractableResponse<Response> response = 로그인_요청("test@gmail.com", "test1234");

		로그인_실패(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		ExtractableResponse<Response> findMemberResponse = MemberAcceptanceTest.내_정보_조회_요청("유효하지 않은 토큰");
		MemberAcceptanceTest.내_정보_조회_실패(findMemberResponse);
	}

	public static ExtractableResponse<Response> 로그인_요청(String name, String password) {
		TokenRequest tokenRequest = new TokenRequest(name, password);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post("/login/token")
			.then().log().all()
			.extract();
	}

	public static String 로그인_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		String accessToken = response.as(TokenResponse.class).getAccessToken();
		assertThat(accessToken).isNotNull();

		return accessToken;
	}

	public static void 로그인_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	public static String 로그인_되어_있음(String name, String password) {
		return 로그인_성공(로그인_요청(name, password));
	}
}
