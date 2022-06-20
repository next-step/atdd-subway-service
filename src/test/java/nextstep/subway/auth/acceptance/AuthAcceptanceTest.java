package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final MemberRequest 회원가입 = new MemberRequest("tasklet1579@next.co.kr", "test1234", 30);
    private static final LoginMember 로그인_회원 = new LoginMember(1L, "tasklet1579@next.co.kr", 30);
    private static final TokenRequest 로그인 = new TokenRequest("tasklet1579@next.co.kr", "test1234");
    private static final TokenRequest 없는_아이디 = new TokenRequest("tasklet1571@next.co.kr", "test1234");
    private static final TokenRequest 비밀번호_틀림 = new TokenRequest("tasklet1579@next.co.kr", "test1231");

    @BeforeEach
    void beforeEach() {
        setUp();
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        ExtractableResponse<Response> 회원가입_결과 = 회원가입_요청(회원가입);

        // when
        ExtractableResponse<Response> 로그인_결과 = 로그인_요청(로그인);

        // then
        로그인_성공(로그인_결과);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        회원가입_요청(회원가입);

        // when
        // then
        assertAll(
                () -> {
                    ExtractableResponse<Response> 로그인_결과 = 로그인_요청(없는_아이디);
                    로그인_실패(로그인_결과);
                },
                () -> {
                    ExtractableResponse<Response> 로그인_결과 = 로그인_요청(비밀번호_틀림);
                    로그인_실패(로그인_결과);
                }
        );
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        ExtractableResponse<Response> 회원가입_결과 = 회원가입_요청(회원가입);
        ExtractableResponse<Response> 로그인_결과 = 로그인_요청(로그인);

        // when
        ExtractableResponse<Response> 나의_정보_조회_결과 = 나의_정보_조회_요청(로그인_토큰(로그인_결과) + "disable");

        // then
        나의_정보_조회_실패(나의_정보_조회_결과);
    }

    public static ExtractableResponse<Response> 회원가입_요청(MemberRequest request) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .body(request)
                          .when().post("/members")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest request) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .body(request)
                          .when().post("/login/token")
                          .then().log().all()
                          .extract();
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank();
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static String 로그인_토큰(ExtractableResponse<Response> response) {
        return response.as(TokenResponse.class).getAccessToken();
    }

    public static ExtractableResponse<Response> 나의_정보_조회_요청(String accessToken) {
        return RestAssured.given().header("Authorization", "Bearer " + accessToken).log().all()
                          .accept(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/members/me")
                          .then().log().all()
                          .extract();
    }

    public static void 나의_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
