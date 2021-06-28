package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthSteps.로그인_요청;
import static nextstep.subway.member.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

// Feature
@DisplayName("로그인 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "oper912@naver.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 32;
    private static final String INVALID_TOKEN = "invalidToken";

    // Background
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given: 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    // Scenario
    @DisplayName("로그인을 시도한다.")
    @Test
    void scenario1() {
        // when: 로그인 요청
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);
        // then: 로그인 됨
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when: 잘못된 정보로 로그인 요청
        ExtractableResponse<Response> 잘못된_로그인_응답 = 로그인_요청(EMAIL, "wrongPassword");
        // then: 로그인 실패됨
        assertThat(잘못된_로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        // then
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> 잘못된_로그인_응답 = 로그인_요청(EMAIL, "wrongPassword");

        // then
        assertThat(잘못된_로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        TokenResponse 유효하지않은_토큰 = new TokenResponse(INVALID_TOKEN);

        // when
        ExtractableResponse<Response> 조회된_내정보 = 내정보_조회_요청(유효하지않은_토큰);

        // then
        assertThat(조회된_내정보.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
