package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSteps.*;
import static nextstep.subway.member.MemberAcceptanceSteps.내_회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceSteps.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {
    private final String EMAIL = "email@email.com";
    private final String INVALID_EMAIL = "INVALID_EMAIL";
    private final String PASSWORD = "password";
    private final Integer AGE = 20;
    private final String INVALID_TOKEN = "INVALID_TOKEN";

    /**
     * Feature: 로그인 기능
     *   Scenario: 로그인을 시도한다.
     */
    @BeforeEach
    void init() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @Test
    @DisplayName("Bearer Auth")
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_정보_조회됨(response);
    }

    @Test
    @DisplayName("유효하지 않은 이메일 - Bearer Auth 로그인 실패")
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(INVALID_EMAIL, PASSWORD);

        // then
        로그인_정보_조회_실패됨(response);
    }

    @Test
    @DisplayName("유효하지 않은 토큰 - Bearer Auth 조회 실패")
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(INVALID_TOKEN);

        // then
        로그인_정보_조회_실패됨(response);
    }
}
