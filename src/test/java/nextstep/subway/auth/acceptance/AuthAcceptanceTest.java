package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private final String email = "email";
    private final String password = "password";
    private final int age = 35;

    private TokenRequest request;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        AuthFactory.회원_생성을_요청(new MemberRequest(email, password, age));
        request = new TokenRequest(email, password);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = AuthFactory.토큰을_요청함(request);
        ExtractableResponse<Response> memberResponse = AuthFactory.토큰으로_인증함(response.as(TokenResponse.class));
        정상적으로_인증됨(memberResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = AuthFactory.토큰을_요청함(new TokenRequest("aaaa", "bbb"));
        인증되지_않았음(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> memberResponse = AuthFactory.토큰으로_인증함(new TokenResponse("aaaaa"));
        인증되지_않았음(memberResponse);
    }

    private void 정상적으로_인증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private void 인증되지_않았음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


}

