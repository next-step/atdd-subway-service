package nextstep.subway.domain.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.domain.auth.dto.TokenRequest;
import nextstep.subway.domain.auth.dto.TokenResponse;
import nextstep.subway.domain.member.MemberAcceptanceTest;
import nextstep.subway.domain.member.dto.MemberResponse;
import nextstep.subway.global.error.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("로그인 기능")
class AuthAcceptanceTest extends AcceptanceTest {

    private String email = "hongji3354@gmail.com";
    private String password = "hongji3354";
    private int age = 25;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_등록되어_있음(email, password, age);
    }

    private void 회원_등록되어_있음(final String email, final String password, final int age) {
        MemberAcceptanceTest.회원_생성을_요청(email, password, age);
    }

    @DisplayName("로그인을 시도한다.")
    @Test
    void myInfoWithBearerAuth() {
        // when
        final ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(email, password);

        // then
        로그인_성공(로그인_요청_응답);
    }

    private ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_성공(final ExtractableResponse<Response> loginResponse) {
        final TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        assertAll(() -> {
            assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(tokenResponse.getAccessToken()).isNotBlank();
        });
    }

    @DisplayName("로그인을 시도하지만 실패한다.")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        final ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청("hongji3354@naver.com", password);

        // then
        로그인_실패(로그인_요청_응답);
    }

    private void 로그인_실패(final ExtractableResponse<Response> loginResponse) {
        final ErrorResponse errorResponse = loginResponse.as(ErrorResponse.class);
        assertAll(() -> {
            assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(errorResponse.getCode()).isEqualTo("C005");
        });
    }

    @DisplayName("유효하지 않은 토큰으로 내 정보 조회시 실패한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        final String invalidToken = "dsmkfmdskfg.dsgbfnufbdshjbdjkandjiansdj";

        // when
        final ExtractableResponse<Response> response = 내_정보_조회_요청(invalidToken);

        // then
        내_정보_조회_실패(response);
    }

    private void 내_정보_조회_실패(final ExtractableResponse<Response> response) {
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(errorResponse.getCode()).isEqualTo("C006");
        });
    }

    private ExtractableResponse<Response> 내_정보_조회_요청(final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

}
