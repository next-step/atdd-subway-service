package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.rest.AuthRestAssured;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.rest.MemberRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유저 인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String 유저_아이디 = "test@test.com";
    private static final String 유저_패스워드 = "test";

    @BeforeEach
    public void setUp() {
        super.setUp();

        MemberRequest 유저_생성_요청 = new MemberRequest(유저_아이디, 유저_패스워드, 31);
        MemberRestAssured.회원_가입_요청(유저_생성_요청);
    }

    /**
     * Scenario: 로그인을 시도한다.
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("아이디와 패스워드가 정상적으로 주어지면 등록 된 회원인지 검증 후 발급 된 토근을 반환한다")
    @Test
    void myInfoWithBearerAuth() {
        // when
        TokenResponse tokenResponse = AuthRestAssured.로그인_요청(new TokenRequest(유저_아이디, 유저_패스워드))
                .as(TokenResponse.class);

        // then
        로그인_됨(tokenResponse);
    }

    /**
     * Scenario: 잘못 된 패스워드로 로그인을 시도한다.
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 실패
     */
    @DisplayName("잘못 된 패스워드가 주어지면 회원 검증 시 예외처리되어 토큰 발급에 실패한다")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = AuthRestAssured.로그인_요청(new TokenRequest(유저_아이디, "잘못된 패스워드"));

        // then
        로그인_실패(response);
    }

    /**
     * Scenario: 유효하지 않은 토큰으로 내정보 조회를 시도한다.
     * Given 회원 등록되어 있음
     * When 내정보 조회 요청
     * Then 내정보 조회 실패
     */
    @DisplayName("유효하지 않은 Bearer Auth 토큰이 주어진 경우 내정보 조회시 예외처리되어 요청에 실패한다")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String 유호하지_않은_토큰 = "qwer.qwer.qwer";

        // when
        ExtractableResponse<Response> response = MemberRestAssured.내정보_조회_요청(유호하지_않은_토큰);

        // then
        내정보_조회_실패(response);
    }

    private void 로그인_됨(TokenResponse tokenResponse) {
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 내정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
