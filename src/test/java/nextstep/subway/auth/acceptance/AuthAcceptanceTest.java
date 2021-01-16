package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.member.acceptance.MemberAcceptanceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.acceptance.MemberAcceptanceTestSupport.*;

class AuthAcceptanceTest extends AuthAcceptanceTestSupport {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        MemberAcceptanceTestSupport.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 토큰_발급_요청(EMAIL, PASSWORD);
        // then
        토큰_생성_완료(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 토큰_발급_요청(EMAIL, "invalidPassword");
        // then
        토큰_발급_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String invalidAccessToken = "invalidAccessToken";
        // when
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(invalidAccessToken);
        // then
        내_정보_조회_실패(findResponse);
    }
}
