package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.TestMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import nextstep.subway.member.TestMember;
import nextstep.subway.member.dto.MemberResponse;

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

	@DisplayName("유효한 토큰으로 내 정보를 조회한다.")
	@Test
	void me() {
		// given
		회원_생성되어_있음(윤준석);
		TokenResponse token = 로그인_요청(윤준석.getEmail(), 윤준석.getPassword()).as(TokenResponse.class);

		// when
		ExtractableResponse<Response> response = 내_정보_조회_요청(token);

		// then
		내_정보_조회됨(response, 윤준석);
	}

	@DisplayName("유효하지 않은 토큰으로 내 정보를 조회하면 실패한다.")
	@Test
	void meFail() {
		// given
		회원_생성되어_있음(윤준석);
		로그인_요청(윤준석.getEmail(), 윤준석.getPassword()).as(TokenResponse.class);

		// when
		ExtractableResponse<Response> response = 내_정보_조회_요청("wrong-token");

		// then
		내_정보_조회_실패됨(response);
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

	private ExtractableResponse<Response> 내_정보_조회_요청(TokenResponse token) {
		return 내_정보_조회_요청(token.getAccessToken());
	}

	private ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
	}

	private void 내_정보_조회됨(ExtractableResponse<Response> response, TestMember expectedMember) {
		MemberResponse actualMember = response.as(MemberResponse.class);

		assertAll(
			() -> assertThat(actualMember.getEmail()).isEqualTo(expectedMember.getEmail()),
			() -> assertThat(actualMember.getAge()).isEqualTo(expectedMember.getAge()));
	}

	private void 내_정보_조회_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
