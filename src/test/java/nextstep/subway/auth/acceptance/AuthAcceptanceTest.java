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

    /**
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 성공
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        String email = "tester@gmail.com";
        String password = "1234";
        회원_등록되어_있음(email, password, 20);

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(email, password);

        // then
        로그인_성공(로그인_응답);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    public static String 회원_등록되어_있음(String email, String password, Integer age) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(email, password, age))
                .when().post("/members")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.header("location");
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotEmpty();
        return accessToken;
    }
}
