package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;

public class AuthAcceptanceTest extends AcceptanceTest {


    @Test
    @DisplayName("Bearer Auth")
    void myInfoWithBearerAuth() {
        // given
        회원_생성을_요청("test@test.com", "test1234", 20);

        // when
        ExtractableResponse<Response> response = 로그인_요청을_한다("test@test.com", "test1234");

        // then
        상태코드가_기대값과_일치하는지_검증한다(response, HttpStatus.OK);
    }

    @Test
    @DisplayName("Bearer Auth 로그인 실패")
    void myInfoWithBadBearerAuth() {
    }

    @Test
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    void myInfoWithWrongBearerAuth() {
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
        Assertions.assertThat(response.statusCode()).isEqualTo(status.value());
    }
}
