package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
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

public class AuthAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> 회원_생성;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        회원_생성 = MemberAcceptanceTest.회원_생성을_요청(EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        MemberAcceptanceTest.회원_생성됨(회원_생성);
    }

    /**
     * Feature: 로그인 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 로그인 됨
    */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        final ExtractableResponse<Response> 토큰발급 = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_검증(토큰발급);
    }

    /**
     * When 토큰을 빼고 로그인사용자 정보를 조회 한다.
     * Then 잘못된 요청 응답이 온다.
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        final ExtractableResponse<Response> 로그인_사용자_정보_조회 = MemberAcceptanceTest.나의_정보_조회_요청("");

        // then
        assertThat(로그인_사용자_정보_조회.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
    * Given 유훃하지 않은 토큰을 준비한다.
    * When 유효하지 않은 토큰으로 로그인사용자 정보를 조회 한다.
    * Then 잘못된 요청 응답이 온다.
    */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String 유효하지_않은_토큰 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // when
        final ExtractableResponse<Response> 로그인_사용자_정보_조회 = MemberAcceptanceTest.나의_정보_조회_요청(유효하지_않은_토큰);

        // then
        assertThat(로그인_사용자_정보_조회.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 로그인_검증(ExtractableResponse<Response> 로그인_요청) {
        assertAll(
                () -> assertThat(로그인_요청.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(로그인_요청.jsonPath().getString("accessToken")).isNotBlank()
        );
    }


    public static String 토큰_발급(String email, String password) {
        return 로그인_요청(email, password).as(TokenResponse.class).getAccessToken();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new TokenRequest(email, password), ObjectMapperType.JACKSON_2)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

}
