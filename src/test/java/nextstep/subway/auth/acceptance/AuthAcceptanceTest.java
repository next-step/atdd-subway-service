package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;
    private final TokenRequest tokenRequest = TokenRequest.of(EMAIL, PASSWORD);

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> 회원_생성을_요청 = MemberAcceptanceTest
                .회원_생성을_요청(EMAIL, PASSWORD, AGE);

        MemberAcceptanceTest.회원_생성됨(회원_생성을_요청);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when
        ExtractableResponse<Response> 로그인_요청 = 로그인_요청(TokenRequest.of(EMAIL, PASSWORD));
        //then
        final String accessToken = 로그인_성공됨(로그인_요청);

        // when
        ExtractableResponse<Response> 내_회원_정보_조회 = MemberAcceptanceTest.내_회원_정보_조회(accessToken);
        // then
        MemberAcceptanceTest.내_회원_정보_조회됨(내_회원_정보_조회);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> 로그인_요청 = 로그인_요청(TokenRequest.of(EMAIL, "PASSWORD"));
        //then
        로그인_실패됨(로그인_요청);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        final ExtractableResponse<Response> 로그인_요청 = 로그인_요청(tokenRequest);
        // then
        로그인_성공됨(로그인_요청);

        // when
        ExtractableResponse<Response> 내_회원_정보_조회 = MemberAcceptanceTest.내_회원_정보_조회("TOKEN");
        // then
        MemberAcceptanceTest.내_회원_정보_토큰_실패(내_회원_정보_조회);
    }

    public static ExtractableResponse<Response> 로그인_요청(final TokenRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_성공됨(final ExtractableResponse<Response> 로그인_요청) {
        assertThat(로그인_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(로그인_요청.jsonPath().getString("accessToken")).isNotBlank();

        return 로그인_요청.jsonPath().getString("accessToken");
    }

    public static void 로그인_실패됨(final ExtractableResponse<Response> 로그인_요청) {
        assertThat(로그인_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
