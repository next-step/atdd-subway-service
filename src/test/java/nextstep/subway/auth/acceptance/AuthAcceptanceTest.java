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

	@DisplayName("로그인을 시도한다.")
	@Test
	void myInfoWithBearerAuth() {
		ExtractableResponse<Response> memberCreateResponse = MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
		MemberAcceptanceTest.회원_생성됨(memberCreateResponse);

		ExtractableResponse<Response> response = 로그인_시도(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
		로그인_성공함(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		ExtractableResponse<Response> response = 로그인_시도(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);

		로그인_실패함(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		ExtractableResponse<Response> memberCreateResponse = MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
		MemberAcceptanceTest.회원_생성됨(memberCreateResponse);

		ExtractableResponse<Response> response = MemberAcceptanceTest.내_정보_조회_요청함("asdfsad");
		정보_조회_요청_실패함(response);
	}

	public static ExtractableResponse<Response> 로그인_시도(String email, String password) {
		LoginRequest loginRequest = new LoginRequest(email, password);
		return RestAssured.given().log().all()
			.when().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(loginRequest)
			.post("/login/token")
			.then().log().all()
			.extract();
	}

	public static void 로그인_성공함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("accessToken")).isNotNull();
		assertThat(response.jsonPath().getString("accessToken")).isNotEmpty();
	}

	public static void 로그인_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	public static void 정보_조회_요청_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
}
