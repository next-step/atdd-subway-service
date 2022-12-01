package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestRestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    MemberRequest memberRequest;

    @BeforeEach
    void setup() {
        memberRequest = new MemberRequest("email@mail.com", "1234", 10);
    }

    /**
     * Feature: 로그인 기능
     * Scenario: 로그인을 시도한다.
     *   Given 회원 등록되어 있음
     *   When 로그인 요청
     *   Then 로그인 됨
     */
    @DisplayName("로그인을 시도한다")
    @Test
    void loginWithBearerAuth() {
        회원_등록되어_있음(memberRequest);
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(memberRequest);
        로그인_됨(로그인_응답);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(memberRequest);
        로그인_실패(로그인_응답);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse 유효하지_않은_토큰 = new TokenResponse("INVALID_TOKEN");
        ExtractableResponse<Response> 응답 = 내_정보_조회(유효하지_않은_토큰);
        토큰_유효하지_않음(응답);
    }

    private void 토큰_유효하지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_실패(ExtractableResponse<Response> 로그인_응답) {
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_됨(ExtractableResponse<Response> tokenResponse) {
        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.body().as(TokenResponse.class))
                .extracting(TokenResponse::getAccessToken)
                .isNotNull();

    }

}
