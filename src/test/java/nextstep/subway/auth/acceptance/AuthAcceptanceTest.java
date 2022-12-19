package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("인증 관련 기능 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_EMAIL = "wrongemail@email.com";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final int AGE = 20;
    private static final int WRONG_AGE = 21;
    private MemberResponse 정상회원;
    private TokenRequest 정상회원_로그인_요청;
    private TokenRequest 비정상회원_로그인_요청;

    @BeforeEach
    void setup() {
        회원_생성을_요청(new MemberRequest(EMAIL, PASSWORD, AGE));
        정상회원_로그인_요청 = new TokenRequest(EMAIL, PASSWORD);
        비정상회원_로그인_요청 = new TokenRequest(WRONG_EMAIL, WRONG_PASSWORD);
    }

    /**
     * Feature: 로그인 기능
     * <p>
     * Scenario: 로그인을 시도한다.
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(정상회원_로그인_요청);
        // then
        로그인_성공(response);
    }

    /**
     * Scenario: 등록하지 않은 회원정보로 로그인을 시도한다.
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("등록되지 않은 회원 정보로 로그인 - Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(비정상회원_로그인_요청);
        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }


    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank()
        );
    }

    private static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
