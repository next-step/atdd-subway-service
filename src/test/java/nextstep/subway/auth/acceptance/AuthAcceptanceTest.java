package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("인증을 통한 로그인 기능 테스트")
    void login() {
        // Given 회원 등록되어 있음
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.NEW_PASSWORD
                , MemberAcceptanceTest.AGE);
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.NEW_PASSWORD);

        // When 로그인 요청
        ExtractableResponse<Response> response = AuthAcceptanceTestSupport.로그인_요청(tokenRequest);
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        // Then 로그인 됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotNull();
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
