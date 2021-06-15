package nextstep.subway.request;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AcceptanceTestRequest {
    public static ExtractableResponse<Response> get(Given given, When when) {
        when.setType(MethodType.GET);
        return execute(given, when);
    }

    public static ExtractableResponse<Response> post(Given given, When when) {
        when.setType(MethodType.POST);
        return execute(given, when);
    }

    public static ExtractableResponse<Response> delete(Given given, When when) {
        when.setType(MethodType.DELETE);
        return execute(given, when);
    }

    private static ExtractableResponse<Response> execute(Given given, When when) {
        RequestSpecification givenSpecification = given.append(given());
        Response append = when.append(givenSpecification);
        return then(append);
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
