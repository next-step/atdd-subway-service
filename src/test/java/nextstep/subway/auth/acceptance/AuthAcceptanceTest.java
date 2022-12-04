package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthFixture.로그인_요청;
import static nextstep.subway.member.MemberFixture.AGE;
import static nextstep.subway.member.MemberFixture.EMAIL;
import static nextstep.subway.member.MemberFixture.PASSWORD;
import static nextstep.subway.member.MemberFixture.회원_생성을_요청;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {


    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /*
    Feature: 로그인 기능
        Scenario: 로그인을 시도한다.
            Given 회원 등록되어 있음
            When 로그인 요청
            Then 로그인 됨
     */
    @Test
    @DisplayName("로그인 기능")
    void scenario1() {
        //when
        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD)
            .as(TokenResponse.class);

        //then
        Assertions.assertThat(tokenResponse.getAccessToken()).isNotNull();

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
