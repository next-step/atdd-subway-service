package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.utils.RestAssuredUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("로그인 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final TokenRequest TOKEN_REQUEST = new TokenRequest(
        "email@email.com",
        "password"
    );

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_등록되어_있음(TOKEN_REQUEST.getEmail(), TOKEN_REQUEST.getPassword(), 20);

        // when
        final ExtractableResponse<Response> response = 로그인_요청(
            TOKEN_REQUEST.getEmail(),
            TOKEN_REQUEST.getPassword()
        );

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private static ExtractableResponse<Response> 로그인_요청(
        final String email,
        final String password
    ) {
        final TokenRequest tokenRequest = new TokenRequest(email, password);
        return 로그인_요청(tokenRequest);
    }

    private static ExtractableResponse<Response> 로그인_요청(final TokenRequest tokenRequest) {
        return RestAssuredUtil.jsonPost(tokenRequest, "/login/token");
    }

    private void 로그인_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
