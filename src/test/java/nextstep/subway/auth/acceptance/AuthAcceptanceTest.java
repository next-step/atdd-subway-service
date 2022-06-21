package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.내_정보_요청;
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

@DisplayName("로그인 기능 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String 이메일 = "email@email.com";
    public static final String 패스워드 = "password";
    public static final int 나이 = 20;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(이메일, 패스워드, 나이);
    }

    @DisplayName("로그인을 할 경우 액세스 토큰이 발급 된다.")
    @Test
    void loginAndGetAccessToken() {
        //when
        final String 액세스_토큰 = 로그인_요청(이메일, 패스워드).as(TokenResponse.class).getAccessToken();

        //then
        액세스_토큰_확인(액세스_토큰);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        final String 액세스_토큰 = 로그인_요청(이메일, 패스워드).as(TokenResponse.class).getAccessToken();
        액세스_토큰_확인(액세스_토큰);

        //when
        final ExtractableResponse<Response> 내_정보 = 내_정보_요청(액세스_토큰);

        //then
        내_정보_확인(내_정보, 이메일, 나이);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //given
        final String 잘못된_패스워드 = "wrongpassword";

        //when
        final ExtractableResponse<Response> 결과 = 로그인_요청(이메일, 잘못된_패스워드);

        //then
        액세스_토큰_발급_실패(결과);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        final String 유효하지_않은_토큰 = "notvalidtoken";

        //when
        final ExtractableResponse<Response> 결과 = 내_정보_요청(유효하지_않은_토큰);

        //then
        내_정보_확인_실패(결과);
    }

    private void 액세스_토큰_확인(final String accessToken) {
        assertThat(accessToken).isNotBlank();
    }

    public static Long 아이디_추출(final ExtractableResponse<Response> response) {
        return Long.valueOf(response.header("Location").substring("/members/".length()));
    }

    public static void 내_정보_확인(final ExtractableResponse<Response> response, final String email, final int age) {
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }

    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        final TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 액세스_토큰_발급_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 내_정보_확인_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
