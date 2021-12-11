package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.Objects;

public class ApiUtils {

    public static RequestSpecification basic() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static RequestSpecification authorization(String accessToken) {
        return basic()
                .header("Authorization", String.format("Bearer %s", accessToken));
    }

    public static ExtractableResponse<Response> get(String url, Map<String, Objects> params) {

        return basic()
                .params(params)
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> get(String url, String accessToken, Map<String, Objects> params) {

        return authorization(accessToken)
                .params(params)
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> post(String url, Object body) {

        return basic()
                .body(body)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String url, String accessToken, Object body) {

        return authorization(accessToken)
                .body(body)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String url, Object body, Map<String, Object> params) {

        return basic()
                .params(params)
                .body(body)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String url, String accessToken, Object body, Map<String, Object> params) {

        return authorization(accessToken)
                .params(params)
                .body(body)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> delete(String url, Map<String, Object> params) {

        return basic()
                .params(params)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String url, String accessToken, Map<String, Object> params) {

        return authorization(accessToken)
                .params(params)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }
}
