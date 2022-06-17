package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.나의_정보_조회_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("로그인 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    String EMAIL = "email@email.com";
    String FAIL_EMAIL = "fail@email.com";
    String PASSWORD = "password";
    String FAIL_PASSWORD = "failPassword";
    int AGE = 20;

    @BeforeEach
    void setUP() {
        super.setUp();

        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * When 로그인을 요청하면
     * Then 로그인이 된다.
     */
    @DisplayName("Bearer Auth 로그인을 시도한다.")
    @Test
    void myInfoWithBearerAuth() {
        // When
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        // Then
        로그인_됨(response);
    }

    /**
     * When 다른 이메일로 로그인을 요청하면
     * Then 로그인을 실패한다.
     */
    @DisplayName("Bearer Auth 다른 이메일로 로그인 시도시 실패한다.")
    @Test
    void myInfoWithBadEmailBearerAuth() {
        // When
        ExtractableResponse<Response> response = 로그인_요청(FAIL_EMAIL, PASSWORD);
        // Then
        로그인_실패됨(response);
    }

    /**
     * When 다른 비밀번호로 로그인을 요청하면
     * Then 로그인을 실패한다.
     */
    @DisplayName("Bearer Auth 다른 비밀번호로 로그인 시도시 실패한다.")
    @Test
    void myInfoWithBadPasswordBearerAuth() {
        // When
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, FAIL_PASSWORD);
        // Then
        로그인_실패됨(response);
    }

    /**
     * When 유효하지 않은 토큰으로 나의 정보를 요청하면
     * Then 정보 조회에 실패한다.
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰으로 나의 정보를 요청하면 실패한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // When
        String invalidToken = "InvalidTokenText";
        ExtractableResponse<Response> response = 나의_정보_조회_요청(invalidToken);
        // Then
        나의_정보_조회_실패됨(response);
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotEmpty()
        );
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 나의_정보_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
