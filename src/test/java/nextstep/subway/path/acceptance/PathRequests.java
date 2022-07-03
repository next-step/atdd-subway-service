package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.MediaType;

public class PathRequests {
    public static ExtractableResponse<Response> 최단경로_조회_요청(Long sourceStationId, Long targetStationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStationId)
                .param("target", targetStationId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_후_최단경로_조회_요청(TokenResponse tokenResponse, Long sourceStationId, Long targetStationId) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .param("source", sourceStationId)
                .param("target", targetStationId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
