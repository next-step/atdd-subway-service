package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;

public class AuthAcceptanceTest extends AcceptanceTest {

	@BeforeEach
	public void setUp() {
		super.setUp();
		회원이_생성됨(EMAIL, PASSWORD, 20);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		TokenRequest request = new TokenRequest(EMAIL, PASSWORD);
		ExtractableResponse<Response> response = 로그인을_요청한다(request);
		this.로그인_성공(response);
	}

	public static ExtractableResponse<Response> 로그인을_요청한다(TokenRequest request) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when()
			.post("/login/token")
			.then().log().all().extract();
	}

	private void 로그인_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 로그인_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		TokenRequest request = new TokenRequest(NEW_EMAIL, NEW_PASSWORD);
		ExtractableResponse<Response> response = 로그인을_요청한다(request);
		this.로그인_실패(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		//email, secret, 토큰 유효기간을 0 으로 설정한 테스트 토큰 생성
		JwtTokenProvider tokenProvider = new JwtTokenProvider();
		String token = tokenProvider.createToken(EMAIL,
			"test-secret",
			0);

		//토큰을 통해 내정보 조회로 유효하지 않은 토큰임을 확인
		ExtractableResponse<Response> myInfoResponse = 내정보를_조회한다(new TokenResponse(token));
		MemberAcceptanceTest.내정보를_조회_실패(myInfoResponse);
	}
}
