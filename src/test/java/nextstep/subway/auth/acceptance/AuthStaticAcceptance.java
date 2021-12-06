package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

public class AuthStaticAcceptance {

	private static final String LOGIN_PATH = "/login/token";
	private static final String MY_INFO_PATH = "/members/me";

	public static ExtractableResponse<Response> 로그인_요청(TokenRequest params) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post(LOGIN_PATH)
			.then().log().all()
			.extract();
	}

	public static void 로그인_성공됨(ExtractableResponse<Response> loginResponse) {
		assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(loginResponse.as(TokenResponse.class).getAccessToken()).isNotEmpty();
	}

	public static TokenRequest 로그인_요청값_생성(String email, String password) {
		return new TokenRequest(email, password);
	}

	public static void 로그인_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	public static ExtractableResponse<Response> 내정보_조회_요청(String token) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.when().get(MY_INFO_PATH)
			.then().log().all()
			.extract();
	}
}
