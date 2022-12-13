package nextstep.subway.utils;

import static nextstep.subway.utils.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

public class AuthAcceptanceUtils {

	private static final String LOGIN_API_URL = "/login/token";

	public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
		TokenRequest tokenRequest = new TokenRequest(email, password);
		return post(LOGIN_API_URL, tokenRequest).extract();
	}

	public static String 로그인_완료되어_토큰_발급(final String email, final String password) {
		return 로그인_요청(email, password)
			.as(TokenResponse.class)
			.getAccessToken();
	}

	public static void 로그인_성공(ExtractableResponse<Response> 로그인_요청) {
		assertAll(
			() -> assertThat(로그인_요청.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(로그인_요청.as(TokenResponse.class)).isNotNull()
		);
	}

	public static void 로그인_실패(ExtractableResponse<Response> 로그인_요청) {
		assertThat(로그인_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	public static void 인증_실패(ExtractableResponse<Response> 로그인_요청) {
		assertThat(로그인_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
