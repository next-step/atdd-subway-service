package nextstep.subway.request;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@FunctionalInterface
public interface WhenFunction {
    Response apply(RequestSpecification requestSpecification, When when);
}
