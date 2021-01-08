package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceSupport {

	public static ExtractableResponse<Response> 회원_가입_되어있음(String email, String password) {
		return RestAssured
				.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new MemberRequest(email, password, 30))
				.when().post("/members")
				.then().log().all().
						extract();
	}

	public static ExtractableResponse<Response> 회원_로그인_요청(String email, String password) {
		return RestAssured
				.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new TokenRequest(email, password))
				.when().post("/login/token")
				.then().log().all().
						extract();
	}

	public static void 로그인_성공함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.body().jsonPath().getString("accessToken")).isNotNull();
	}

	public static void 로그인_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	public static ExtractableResponse<Response> 회원기능_요청(String token) {
		return RestAssured
				.given().log().all()
				.header(createTokenHeader(token))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/members/me")
				.then().log().all().
						extract();
	}

	private static Header createTokenHeader(String token) {
		return new Header("authorization", String.format("Bearer %s", token));
	}

	public static void 인증_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
