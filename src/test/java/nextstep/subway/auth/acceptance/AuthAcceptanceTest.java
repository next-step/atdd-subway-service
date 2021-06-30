package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_됨;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_요청;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_요청_실패됨;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.토큰_인증_실패;
import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.내_회원_정보_조회_요청;
import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.회원_등록되어_있음;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String GIVEN_EMAIL = "test@test.com";
    private static final String GIVEN_PASSWORD = "password";
    private static final int GIVEN_AGE = 10;
    private static final String INVALID_EMAIL = GIVEN_EMAIL + "_INVALID";
    private static final String INVALID_PASSWORD = GIVEN_PASSWORD + "_INVALID";
    private static final String INVALID_TOKEN = "INVALID_TOKEN";

    /**
     * Background
     * Given 회원 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_등록되어_있음(GIVEN_EMAIL, GIVEN_PASSWORD, GIVEN_AGE);
    }

    /**
     * Scenario: 로그인 인증
     * When 유효한 정보로 로그인_요청
     * Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(GIVEN_EMAIL, GIVEN_PASSWORD);
        // then
        로그인_됨(로그인_요청_응답);
    }

    /**
     * Scenario: 로그인 인증
     * When 잘못된 이메일, 비밀번호로 로그인_요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(INVALID_EMAIL, INVALID_PASSWORD);
        // then
        로그인_요청_실패됨(로그인_요청_응답);
    }

    /**
     * Scenario: 로그인 인증
     * When 유효하지 않은 토큰으로 회원 정보 요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse badTokenResponse = new TokenResponse(INVALID_TOKEN);
        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(badTokenResponse);
        // then
        토큰_인증_실패(response);
    }
}
