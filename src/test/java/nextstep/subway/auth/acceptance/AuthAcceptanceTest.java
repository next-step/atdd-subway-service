package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_요청;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
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
    String PASSWORD = "password";
    int AGE = 20;
    // Given 회원 등록되어 있음
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

    @DisplayName("Bearer Auth 로그인 시도시 실패한다.")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotEmpty()
        );
    }
}
