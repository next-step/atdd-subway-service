package nextstep.subway.auth.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

public class AuthTestMethod {
	public static String getToken(ExtractableResponse<Response> response) {
		TokenResponse tokenResponse = response.as(TokenResponse.class);

		return tokenResponse.getAccessToken();
	}

	public static ExtractableResponse<Response> login(String EMAIL, String PASSWORD) {
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post("/login/token")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> requestWithToken(String token) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
	}
}
