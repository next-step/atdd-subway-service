package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final TokenRequest TOKEN_REQUEST = new TokenRequest("email@email.com", "password");

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        MemberAcceptanceTest.회원_생성_요청(TOKEN_REQUEST.getEmail(), TOKEN_REQUEST.getPassword(), 20);

        // when
        final ExtractableResponse<Response> response = 로그인_요청(TOKEN_REQUEST);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        final ExtractableResponse<Response> response = 로그인_요청(TOKEN_REQUEST);

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        final ExtractableResponse<Response> response = MemberAcceptanceTest.내정보_조회_요청("invalid-token");

        // then
        토큰_인증_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(final TokenRequest tokenRequest) {
        return post("/login/token", tokenRequest);
    }

    private static void 로그인_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 토큰_인증_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
