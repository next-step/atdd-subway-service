package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관련 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @Test
    @DisplayName("성공적으로 로그인을 한다")
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청을_한다(EMAIL, PASSWORD);
        String 인증토큰 = 토큰을_얻는다(loginResponse);

        // then
        상태코드가_기대값과_일치하는지_검증한다(loginResponse, HttpStatus.OK);
        토큰이_존재하는지_확인한다(인증토큰);
    }


    @Test
    @DisplayName("Bearer Auth 로그인 실패")
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청을_한다(EMAIL, "TEST");

        // then
        상태코드가_기대값과_일치하는지_검증한다(response, HttpStatus.UNAUTHORIZED);
    }


    @Test
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 토큰을_이용하여_회원_정보_조회를_한다("INVALID");

        // then
        상태코드가_기대값과_일치하는지_검증한다(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ExtractableResponse<Response> 로그인_요청을_한다(String email, String password) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 상태코드가_기대값과_일치하는지_검증한다(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private String 토큰을_얻는다(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }

    private void 토큰이_존재하는지_확인한다(String 인증토큰) {
        assertThat(인증토큰).isNotEmpty();
    }
}
