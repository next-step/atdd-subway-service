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

	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
	}
}
