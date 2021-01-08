package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
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
		MemberAcceptanceTest.회원_생성을_요청(email, password, 10);

		//when - 로그인
		ExtractableResponse<Response> tokenResponse = 토큰정보를_요청한다(
			  email, password);

		//when - 토큰기반 myInfo 정보 조회
		ExtractableResponse<Response> memberResponse = 토근기반_로그인계정의_회원정보를_요청한다(
			  tokenResponse.body().as(TokenResponse.class).getAccessToken());

		//then
		MemberResponse member = memberResponse.body().as(MemberResponse.class);
		assertThat(member.getEmail()).isEqualTo(email);
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

		assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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

		Thread.sleep(1_000); //토큰만료 대기

		//when - 토큰기반 myInfo 정보 조회
		ExtractableResponse<Response> memberResponse = 토근기반_로그인계정의_회원정보를_요청한다(
			  tokenResponse.body().as(TokenResponse.class).getAccessToken());

		//then
		assertThat(memberResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private ExtractableResponse<Response> 토큰정보를_요청한다(String email,
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

	private ExtractableResponse<Response> 토근기반_로그인계정의_회원정보를_요청한다(String accessToken) {
		ExtractableResponse<Response> memberResponse = RestAssured.given().log().all()
			  .auth().oauth2(accessToken)
			  .accept(MediaType.APPLICATION_JSON_VALUE)
			  .when().get("/members/me")
			  .then().log().all()
			  .extract();
		return memberResponse;
	}
}
