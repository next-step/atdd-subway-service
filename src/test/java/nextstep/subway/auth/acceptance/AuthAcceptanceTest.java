package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestRestAssured.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestRestAssured.회원_등록되어_있음;
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
    void login() {
        회원_등록되어_있음(memberRequest);
        TokenResponse 로그인_토큰 = 로그인_요청(memberRequest);
        로그인_됨(로그인_토큰);
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

    private void 로그인_됨(TokenResponse tokenResponse) {
        MemberResponse 로그인_응답 = AuthAcceptanceTestRestAssured.내_정보_조회(tokenResponse);
        assertThat(로그인_응답.getEmail()).isEqualTo(memberRequest.getEmail());
    }

}
