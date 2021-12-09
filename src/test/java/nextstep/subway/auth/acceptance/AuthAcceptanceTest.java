package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.ApiRequest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_등록되어_있음("email@test.com", "dflkasjdfsl@fdkljsa!", 31);

    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인을_요청한다("email@test.com", "dflkasjdfsl@fdkljsa!");
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인을_요청한다("wrong@idtest.com", "123123213");
        로그인_실패(response);
    }
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> tokenResponse = 로그인을_요청한다("email@test.com", "dflkasjdfsl@fdkljsa!");
        ExtractableResponse<Response> memberResponse = 토큰으로_멤버를_요청한다(tokenResponse.as(TokenResponse.class));
        같은_멤버인지_확인한다(memberResponse.as(LoginMember.class), "email@test.com");
    }

    private ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, int age) {
        return MemberAcceptanceTest.회원_생성을_요청(email, password, age);
    }

    private ExtractableResponse<Response> 로그인을_요청한다(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return ApiRequest.post("/login/token", tokenRequest);
    }

    private ExtractableResponse<Response> 토큰으로_멤버를_요청한다(TokenResponse tokenResponse) {
        return ApiRequest.get("/token/check?token=" + tokenResponse.getAccessToken());
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 같은_멤버인지_확인한다(LoginMember loginMember, String email) {
        assertThat(loginMember.getEmail()).isEqualTo(email);
    }


}
