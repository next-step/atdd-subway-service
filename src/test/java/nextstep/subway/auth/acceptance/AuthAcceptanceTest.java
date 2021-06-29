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
		TokenRequest tokenRequest = new TokenRequest("taminging@kakao.com", "taminging");
		ExtractableResponse<Response> response =  post(tokenRequest,"/login/token");
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		TokenRequest tokenRequest = new TokenRequest("taminging@kakao.com", "taminging2");
		ExtractableResponse<Response> response =  post(tokenRequest,"/login/token");
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		String invalidAccessToken = "invalid access token";
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.auth().oauth2(invalidAccessToken)
			.when()
			.get("/members/me")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
