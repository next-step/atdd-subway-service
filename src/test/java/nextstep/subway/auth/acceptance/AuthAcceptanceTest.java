package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.UNKNOWN_AGE;
import static nextstep.subway.member.MemberAcceptanceTest.UNKNOWN_EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.UNKNOWN_PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {



    @Test
    void 회원가입한_경우_토큰_발급() {
        // given
        ExtractableResponse<Response> createMember = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createMember);
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);

        // then
        응답_정상(loginResponse);
        토큰_발급_확인(loginResponse);
    }

    @Test
    void 회원정보가_없는경우_토큰_생성_실패() {

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(UNKNOWN_EMAIL, UNKNOWN_PASSWORD);

        // then
        토큰_생성_실패(loginResponse);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(TokenRequest.of(email, password))
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static void 응답_정상(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    public static void 토큰_발급_확인(ExtractableResponse<Response> response) {
        TokenResponse authResponse = response.as(TokenResponse.class);
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getAccessToken()).isNotNull();
    }

    public static void 토큰_생성_실패(ExtractableResponse<Response> loginResponse) {
        String accessToken = loginResponse.jsonPath().getString("accessToken");
        assertThat(loginResponse.statusCode()).isEqualTo(UNAUTHORIZED.value());
        assertThat(accessToken).isNull();
    }

    public static ExtractableResponse<Response> 토큰_생성_됨(String email, String password) {
        return 로그인_요청(email, password);
    }

}
