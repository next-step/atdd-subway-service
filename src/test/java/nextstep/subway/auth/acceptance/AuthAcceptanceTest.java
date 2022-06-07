package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.토큰_인증_실패;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_조회_요청_토큰이용;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String 손성현_EMAIL = "chosunci@gmail.com";
    private static final String 손성현_PASSWORD = "1111";
    private static final int 손성현_AGE = 35;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(손성현_EMAIL, 손성현_PASSWORD, 손성현_AGE);
    }

    /**
     * When. 가입된 회원정보로 로그인을 시도한다.
     * Then. 로그인이 성공한다.
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(TokenRequest.of(손성현_EMAIL, 손성현_PASSWORD));

        // then
        로그인_됨(response);
    }

    /**
     * Given. 가임되지 않은 회원정보로 토큰요청정보를 생성한다.
     * When. 토큰 발급(로그인) 요청을 시도한다.
     * Then. 로그인이 실패한다.
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String wrongEmail = "wwww@wwww.com";
        String wrongPassword = "wwww";
        TokenRequest wrongTokenRequest = TokenRequest.of(wrongEmail, wrongPassword);

        // when
        ExtractableResponse<Response> response = 로그인_요청(wrongTokenRequest);

        // then
        로그인_실패(response);
    }

    /**
     * Given. 유효하지 않은 인증 토큰
     * When. 토큰을 이용하여 회원 정보를 조회한다.
     * Then. 토큰 인증에 실패한다.
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @ParameterizedTest
    @ValueSource(strings = {"1", "aa", "bbb", "asdofina", "!@#!@#!@#"})
    void myInfoWithWrongBearerAuth(String accessToken) {
        // given
        TokenResponse tokenResponse = new TokenResponse(accessToken);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청_토큰이용(new MemberRequest(손성현_EMAIL, 손성현_PASSWORD, 손성현_AGE), tokenResponse);

        // then
        토큰_인증_실패(response);
    }
}
