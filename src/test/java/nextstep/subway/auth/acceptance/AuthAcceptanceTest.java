package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.LoginRequest;
import nextstep.subway.member.MemberAcceptanceTest;

public class AuthAcceptanceTest extends AcceptanceTest {

	public static final String WRONG_EMAIL = "wrong@email.com";

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		ExtractableResponse<Response> memberCreateResponse = MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
		MemberAcceptanceTest.회원_생성됨(memberCreateResponse);

		LoginRequest loginRequest = new LoginRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(loginRequest)
			.post("/login/token")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("accessToken")).isNotNull();
		assertThat(response.jsonPath().getString("accessToken")).isNotEmpty();
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		LoginRequest loginRequest = new LoginRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(loginRequest)
			.post("/login/token")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		ExtractableResponse<Response> memberCreateResponse = MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
		MemberAcceptanceTest.회원_생성됨(memberCreateResponse);

		LoginRequest loginRequest = new LoginRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
		ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
			.when().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(loginRequest)
			.post("/login/token")
			.then().log().all()
			.extract();

		assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(loginResponse.jsonPath().getString("accessToken")).isNotNull();
		assertThat(loginResponse.jsonPath().getString("accessToken")).isNotEmpty();

		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when().log().all()
			.body(loginRequest)
			.get("/member/me")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

}
