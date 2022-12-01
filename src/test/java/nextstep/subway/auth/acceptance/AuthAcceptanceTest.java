package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.내정보_조회_성공;
import static nextstep.subway.member.MemberAcceptanceTest.내정보_조회_실패;
import static nextstep.subway.member.MemberAcceptanceTest.내정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private final String EMAIL = "email@email.com";
    private final String WRONG_EMAIL = "wrong@email.com";
    private final String PASSWORD = "password";
    private final String WRONG_PASSWORD = "wrong_password";
    private final int AGE = 20;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_성공됨(response);
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFail() {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_실패됨(response);
    }

    @DisplayName("잘못된 email로 로그인 실패")
    @Test
    void loginFailWithWrongEmail() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 로그인_요청(WRONG_EMAIL, PASSWORD);

        로그인_실패됨(response);
    }

    @DisplayName("잘못된 password로 로그인 실패")
    @Test
    void loginFailWithWrongPassword() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, WRONG_PASSWORD);

        로그인_실패됨(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 로그인_성공됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
