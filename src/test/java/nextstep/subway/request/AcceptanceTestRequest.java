package nextstep.subway.request;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AcceptanceTestRequest {
    public static ExtractableResponse<Response> get(String path, Given given) {
        return then(givenWhen(given).get(path));
    }

    public static ExtractableResponse<Response> post(String path, Given given) {
        return then(givenWhen(given).post(path));
    }

    public static ExtractableResponse<Response> delete(String path, Given given) {
        return then(givenWhen(given).delete(path));
    }

    private static RequestSpecification givenWhen(Given given) {
        RequestSpecification givenSpecification = given.append(given());
        return when(givenSpecification);
    }

    private static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    private static RequestSpecification when(RequestSpecification requestSpecification) {
        return requestSpecification.when().log().all();
    }

    private static ExtractableResponse<Response> then(Response response) {
        return response.then().extract();
    }
}
