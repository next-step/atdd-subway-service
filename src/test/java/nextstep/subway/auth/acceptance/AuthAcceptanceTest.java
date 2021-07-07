package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.domain.MemberRepository;
@DisplayName("인증 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

	MemberAcceptanceTest memberAcceptanceTest;

	@BeforeEach
	void authSetUp() {
		memberAcceptanceTest = new MemberAcceptanceTest();
		ExtractableResponse<Response> response = memberAcceptanceTest.회원_생성을_요청("taminging@kakao.com", "taminging", 20);
		memberAcceptanceTest.회원_생성됨(response);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		ExtractableResponse<Response> response = 로그인("taminging@kakao.com", "taminging");
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.body().jsonPath().getString("accessToken")).isNotEmpty();
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		ExtractableResponse<Response> response = 로그인("taminging@kakao.com", "taminging2");
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		String invalidAccessToken = "invalid access token";
		ExtractableResponse<Response> response = get("/members/me", invalidAccessToken);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	public ExtractableResponse<Response> 로그인(String email, String password) {
		TokenRequest tokenRequest = new TokenRequest(email, password);
		return post(tokenRequest,"/login/token");
	}

}
