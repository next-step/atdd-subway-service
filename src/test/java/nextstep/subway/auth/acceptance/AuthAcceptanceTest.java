package nextstep.subway.auth.acceptance;

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

import static nextstep.subway.auth.AuthHelper.*;
import static nextstep.subway.member.MemberHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String WRONG_PASSWORD = "wrong_password";
    public static final int AGE = 20;

    @BeforeEach
    void 미리_생성() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        //then
        assertThat(response.jsonPath().getObject(".", TokenResponse.class).getAccessToken()).isNotBlank();
    }


    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, WRONG_PASSWORD);

        //when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = 로그인_요청(tokenRequest);
        String accessToken = loginResponse.jsonPath().getObject(".", TokenResponse.class).getAccessToken();
        accessToken = accessToken.concat("hi");
        //when
        ExtractableResponse findMeResponse = 내_정보_찾기(accessToken);
        //then
        assertThat(findMeResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
