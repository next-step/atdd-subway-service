package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.application.AuthServiceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.토큰으로_회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenRequest request = new TokenRequest(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 로그인_요청(request);

        //then
        정상_처리(response);
        정상_토큰_발급(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //given
        TokenRequest request = new TokenRequest("wrongid@email.com", PASSWORD);

        //when
        ExtractableResponse<Response> response = 로그인_요청(request);

        //then
        인증_실패(response);
    }

    private void 인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> response = 토큰으로_회원_정보_조회_요청("wrongToken");

        //then
        인증_실패(response);
    }

    private void 정상_토큰_발급(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest request) {

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }


}
