package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathRequest;
import org.springframework.http.MediaType;

public class PathAcceptance {
    public static ExtractableResponse<Response> shortest_path_found(Long source, Long target) {
        PathRequest pathRequest = new PathRequest(source, target);

        return RestAssured.given().log().all()
                .body(pathRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/paths")
                .then().log().all()
                .extract();
    }
}
