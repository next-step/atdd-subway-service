package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.내_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String 이메일 = "test@abc.com";
    private static final String 비밀번호 = "password";

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        회원_생성을_요청(이메일, 비밀번호, 30);
    }

    /**
     * Scenario: 로그인을 시도한다.
     *  Given 회원이 등록되어 있음
     *  When 로그인 요첨
     *  Then 로그인 됨
     */
    @DisplayName("등록된 회원이 로그인하면 로그인되어 Access Token을 발급받는다.")
    @Test
    void 등록된_회원_로그인_성공() {
        // When
        ExtractableResponse<Response> response = 로그인_요청(이메일, 비밀번호);

        // Then
        로그인_성공(response);
        토큰_생성_됨(response);
    }

    private void 토큰_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.as(TokenResponse.class)).isNotNull();
    }

    /**
     * Scenario: 로그인을 시도한다.
     */
    @DisplayName("로그인 실패")
    class 로그인_실패 {
        /**
         * Given 회원이 등록되어 있음
         * When 유효하지 않은 비밀번호로 로그인 하면
         * Then 로그인 실패한다.
         */
        @DisplayName("유효하지 않은 비밀번호로 로그인하면 실패한다.")
        @Test
        void 유효하지_않은_비밀번호로_로그인() {
            // Given
            String 유효하지_않은_비밀번호 = "pass";

            // When
            ExtractableResponse<Response> response = 로그인_요청(이메일, 유효하지_않은_비밀번호);

            // Then
            인증_실패(response);
        }

        /**
         * When 등록되지 않은 이메일로 로그인하면
         * Then 로그인 실패한다.
         */
        @DisplayName("등록되지 않은 이메일로 로그인하면 실패한다.")
        @Test
        void 등록되지_않은_이메일로_로그인() {
            // Given
            String 등록되지_않은_이메일 = "no@test.com";

            // When
            ExtractableResponse<Response> response = 로그인_요청(등록되지_않은_이메일, 비밀번호);

            // Then
            인증_실패(response);
        }
    }

    /**
     * Given 유효하지 않은 토큰으로
     * When 내 정보 조회하면
     * Then 인증 실패한다
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰으로 내 정보 조회하면 인증 실패한다.")
    @Test
    void 유효하지_않은_토큰으로_내_정보_조회() {
        final String 유효하지_않은_토큰 = "1nVa1id.T0ke7";

        ExtractableResponse<Response> response = 내_정보_조회_요청(유효하지_않은_토큰);
        인증_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private static void 로그인_성공(ExtractableResponse<Response> response) {
        응답코드_확인(response, HttpStatus.OK);
        assertThat(response.as(TokenResponse.class)).isNotNull();
    }

    private static void 인증_실패(ExtractableResponse<Response> response) {
        응답코드_확인(response, HttpStatus.UNAUTHORIZED);
    }
}
