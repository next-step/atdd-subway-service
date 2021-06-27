package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.acceptance.TestToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemeberSteps {

    public static void 회원_등록_되어_있음(String email, String password, int age) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/members")
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 내_정보_조회_요청_유효하지_않은_토큰(TestToken token) {
        return RestAssured.given().log().all()
                .auth()
                .oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 내_회원_정보_조회됨(TestToken token) {
        RestAssured.given().log().all()
                .auth()
                .oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    public static void 내_정보_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
