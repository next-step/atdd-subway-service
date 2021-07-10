package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, 20);
	}

	@Test
	@DisplayName("로그인 기능")
	void loginTest() {
		ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
		로그인_되어_있음(response);
	}

	private void 로그인_되어_있음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
	}

	public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
		return RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new TokenRequest(email, password))
				.when()
				.post("/login/token")
				.then().log().all()
				.extract();
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
		ExtractableResponse<Response> response = MemberAcceptanceTest.나의_정보_요청(accessToken);
		MemberAcceptanceTest.나의_정보_조회됨(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		String wrongPassword = "123";
		ExtractableResponse<Response> response = 로그인_요청(EMAIL, wrongPassword);
		로그인_실패(response);
	}

	private void 로그인_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		String wrongAccessToken = "qwe";
		ExtractableResponse<Response> response = MemberAcceptanceTest.나의_정보_요청(wrongAccessToken);
		나의_정보_조회_안됨(response);
	}

	private void 나의_정보_조회_안됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
