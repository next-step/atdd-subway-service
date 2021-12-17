package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토큰 발급 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        ExtractableResponse<Response> 회원_생성_요청_응답 = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        MemberAcceptanceTest.회원_생성됨(회원_생성_요청_응답);
    }

    @Test
    void 토큰으로_회원_정보_조회() {
        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        로그인_성공(로그인_요청_응답);

        String 회원_토큰 = 토큰_조회(로그인_요청_응답);
        ExtractableResponse<Response> 토큰으로_회원_정보_조회_응답 = MemberAcceptanceTest.토큰으로_회원_정보_조회(회원_토큰);

        // then
        회원_정보_조회_응답됨(토큰으로_회원_정보_조회_응답);
    }

    @Test
    void 유효하지않은_토큰으로_회원_정보_조회() {
        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        로그인_성공(로그인_요청_응답);

        String 유효하지않은_회원_토큰 = "유효하지않은_회원_토큰";
        ExtractableResponse<Response> 토큰으로_회원_정보_조회_응답 = MemberAcceptanceTest.토큰으로_회원_정보_조회(유효하지않은_회원_토큰);

        // then
        회원_정보_조회_실패됨(토큰으로_회원_정보_조회_응답, "비로그인 상태입니다.");
    }

    private void 회원_정보_조회_실패됨(ExtractableResponse<Response> response, String errorMessage) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.response().body().asString()).isEqualTo(errorMessage);
    }

    @Test
    void 유효하지않은_이메일로_로그인_요청() {
        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청("WRONG_EMAIL", PASSWORD);

        // then
        로그인_실패(로그인_요청_응답, "잘못된 이메일입니다.");
    }

    @Test
    void 유효하지않은_비밀번호로_로그인_요청() {
        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, "WRONG_PASSWORD");

        // then
        로그인_실패(로그인_요청_응답, "비밀번호가 맞지 않습니다.");
    }


    public static void 로그인_실패(ExtractableResponse<Response> response, String errorMessage) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.response().body().asString()).isEqualTo(errorMessage);
    }

    public static String 토큰_조회(ExtractableResponse<Response> response) {
        return response.as(TokenResponse.class).getAccessToken();
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = TokenRequest.of(email, password);
        return 생성_요청("/login/token", tokenRequest);
    }

    public static void 회원_정보_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
