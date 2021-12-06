package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.TestMember.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

	@DisplayName("로그인을 시도한다.")
	@Test
	void login() {
		// given
		회원_생성되어_있음(윤준석);

		// when
		ExtractableResponse<Response> response = 로그인_요청(윤준석.getEmail(), 윤준석.getPassword());

		// then
		로그인_됨(response);
	}

	@DisplayName("아이디와 비밀번호가 다를 경우 로그인에 실패한다.")
	@Test
	void loginFail() {
		// given
		회원_생성되어_있음(윤준석);

		// when
		ExtractableResponse<Response> response = 로그인_요청(윤준석.getEmail(), "wrong-password");

		// then
		로그인_실패됨(response);
	}

	private ExtractableResponse<Response> 로그인_요청(String email, String password) {
		TokenRequest tokenRequest = new TokenRequest(email, password);
		return post("/login/token", tokenRequest);
	}

	private void 로그인_됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		TokenResponse token = response.as(TokenResponse.class);
		assertThat(token).isNotNull();
	}

	private void 로그인_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
	}

}
