package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.utils.RestApiFixture;

public class AuthAcceptanceTest extends AcceptanceTest {

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
        final String email = "abc@mail.com";
        final String password = "<secret>";
        final int age = 20;
        회원_등록되어_있음(email, password, age);

        final ExtractableResponse<Response> response = 로그인_요청(email, password);
        로그인_됨(response);
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
        final ExtractableResponse<Response> response = 로그인_요청("unknown@mail.com", "<secret>");
		인증_실패함(response);
	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		final ExtractableResponse<Response> response = 내회원정보_조회_요청("unknownToken");
		인증_실패함(response);
	}

    private ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, Integer age) {
        return MemberAcceptanceTest.회원_생성을_요청(email, password, age);
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        final TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestApiFixture.post(tokenRequest, "/login/token");
    }

	private ExtractableResponse<Response> 내회원정보_조회_요청(String accessToken) {
		final RequestSpecification request = RestApiFixture.request().auth().oauth2(accessToken);
		return RestApiFixture.response(request.get("/members/me"));
	}

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
    }

    private void 인증_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
