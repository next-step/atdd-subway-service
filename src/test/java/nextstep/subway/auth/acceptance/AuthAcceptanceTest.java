package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String BASE_URI = "/login/token";
    private static final String NOT_EXIST_EMAIL = "NOT_EXIST@EMAIL.COM";
    private static final String NOT_CORRECT_PASSWORD = "NOT_CORRECT_PASSWORD";
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        TokenRequest request = TokenRequest.of(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URI)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        TokenResponse tokenResponse = response.jsonPath().getObject("", TokenResponse.class);
        String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());

        // then
        assertThat(payload).isEqualTo(EMAIL);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        TokenRequest 비밀번호불일치 = TokenRequest.of(EMAIL, NOT_CORRECT_PASSWORD);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(비밀번호불일치)
                .when().post(BASE_URI)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // given
        TokenRequest 존재하지않은_ID = TokenRequest.of(NOT_EXIST_EMAIL, NOT_CORRECT_PASSWORD);

        // when
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(존재하지않은_ID)
                .when().post(BASE_URI)
                .then().log().all().extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

    }

}
