package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.path.dto.PathRequest;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceId, Long targetId) {
        PathRequest params = new PathRequest(sourceId, targetId);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_후_최단_경로_조회_요청(TokenResponse tokenResponse, Long sourceId, Long targetId) {
        PathRequest params = new PathRequest(sourceId, targetId);
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }
}
