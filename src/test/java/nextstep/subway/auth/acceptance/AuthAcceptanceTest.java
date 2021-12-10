package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {

        //given
        TokenRequest tokenRequest = new TokenRequest("test@gmail.com", "test1234");

        //when
        ExtractableResponse<Response> 로그인 = 로그인_요청(tokenRequest);

        //then
        assertThat(로그인.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(로그인.body().jsonPath().getString(".accessToken")).isNotEmpty();
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {

        //given
        TokenRequest tokenRequest = new TokenRequest("test@gmail.com", "112345");

        //when
        ExtractableResponse<Response> 로그인 = 로그인_요청(tokenRequest);

        //then
        assertThat(로그인.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

        //given
        TokenRequest tokenRequest = new TokenRequest("test@gmail.com", "test1234");
        ExtractableResponse<Response> 로그인 = 로그인_요청(tokenRequest);
        String accessToken = 로그인.body().jsonPath().getString(".accessToken");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    
    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {

        //when
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(tokenRequest)
                .post("/login/token")
                .then().log().all()
                .extract();

    }

}
