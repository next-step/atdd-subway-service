package nextstep.subway.auth.acceptance;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;

public class AuthAcceptanceTest extends AcceptanceTest {

	private ExtractableResponse<Response> memberResponse;

	@BeforeEach
	public void setup() {
		memberResponse = MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		ExtractableResponse<Response> response = 로그인을_시도한다(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD);

		로그인_시도하여_토큰을_받아옴(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		ExtractableResponse<Response> response = 로그인을_시도한다(MemberAcceptanceTest.EMAIL, "123");

		로그인_실패(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
	}

	private ExtractableResponse<Response> 로그인을_시도한다(final String email, final String password) {
		TokenRequest request = new TokenRequest(email, password);
		return given().log().all()
			.body(request)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/login/token")
			.then().log().all().extract();
	}

	private void 로그인_시도하여_토큰을_받아옴(final ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.as(TokenResponse.class).getAccessToken()).isNotEmpty();
	}

	private void 로그인_실패(final ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
