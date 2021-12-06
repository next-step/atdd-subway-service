package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthStaticAcceptance.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberStaticAcceptance.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

	private static final String 잘못된_비밀번호 = "wrongPw";
	private static final String 잘못된_토큰 = "wrongToken";

	@BeforeEach
	void setup() {
		super.setUp();
		ExtractableResponse<Response> memberResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		회원_생성됨(memberResponse);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		// given
		TokenRequest 로그인_요청값 = 로그인_요청값_생성(EMAIL, PASSWORD);

		// when
		ExtractableResponse<Response> response = 로그인_요청(로그인_요청값);

		// then
		로그인_성공됨(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		// given
		TokenRequest 잘못된_로그인_요청값 = 로그인_요청값_생성(EMAIL, 잘못된_비밀번호);

		// when
		ExtractableResponse<Response> response = 로그인_요청(잘못된_로그인_요청값);

		// then
		로그인_실패됨(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		// when
		ExtractableResponse<Response> response = 내정보_조회_요청(잘못된_토큰);

		// then
		로그인_실패됨(response);
	}

}
