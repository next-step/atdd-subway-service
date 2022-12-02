package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인_요청_성공함;
import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인_요청_실패함;
import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.ServerProperties.Jetty.Accesslog.FORMAT;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@sixthou.kro.kr";
    private static final String PASSWORD = "1q2w3e!@";
    private static final int AGE = 25;


    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // when
        ExtractableResponse<Response> response = 로그인을_요청(EMAIL, PASSWORD);
        // then
        로그인_요청_성공함(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //  Given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String failPassword = "qwerty";
        //  When
        ExtractableResponse<Response> response = 로그인을_요청(EMAIL, failPassword);
        //  Then
        로그인_요청_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //  Given
        //  When /members/me에 유효하지 않는 요청
        // then 요청 실패함.
    }





}
