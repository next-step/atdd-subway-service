package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		//회원정보가 등록되어 있음
		String email = "test2@dev.com";
		String password = "1234";
		int age = 10;
		MemberAcceptanceTest.회원_생성을_요청(email, password, age);

		//when - 로그인
		ExtractableResponse<Response> tokenResponse = 토큰정보를_요청한다(
			  email, password);

		//when - 토큰기반 myInfo 정보 조회
		ExtractableResponse<Response> memberResponse = MemberAcceptanceTest
			  .나의_정보_조회_요청(tokenResponse.body().as(TokenResponse.class).getAccessToken());

		//then
		MemberAcceptanceTest.회원_정보_조회됨(memberResponse, email, age);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		//회원정보가 등록되어 있음
		String email = "test@dev.com";
		String password = "1234";
		MemberAcceptanceTest.회원_생성을_요청(email, password, 10);

		//when - 로그인
		String wrongPassword = "4321";
		ExtractableResponse<Response> tokenResponse = 토큰정보를_요청한다(
			  email, wrongPassword);

		//then
		로그인_실패함(tokenResponse);

	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() throws InterruptedException {
		//회원정보가 등록되어 있음
		String email = "test@dev.com";
		String password = "1234";
		MemberAcceptanceTest.회원_생성을_요청(email, password, 10);

		//when - 로그인
		ExtractableResponse<Response> tokenResponse = 토큰정보를_요청한다(
			  email, password);

		//when - 토큰기반 myInfo 정보 조회
		ExtractableResponse<Response> memberResponse = MemberAcceptanceTest
			  .나의_정보_조회_요청("1asf233gf4");

		//then
		토큰_인증_실패함(memberResponse);
	}

	public static String 접근권한_토큰값을_가져온다(String email, String password) {
		ExtractableResponse<Response> response = 토큰정보를_요청한다(email, password);
		return response.body().as(TokenResponse.class).getAccessToken();
	}

	public static ExtractableResponse<Response> 토큰정보를_요청한다(String email,
		  String password) {
		ExtractableResponse<Response> tokenResponse = RestAssured.given().log().all()
			  .accept(MediaType.APPLICATION_JSON_VALUE)
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(new TokenRequest(email, password))
			  .when().post("/login/token")
			  .then().log().all()
			  .extract();
		return tokenResponse;
	}

	private void 로그인_실패함(ExtractableResponse<Response> tokenResponse) {
		assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	private void 토큰_인증_실패함(ExtractableResponse<Response> memberResponse) {
		assertThat(memberResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
