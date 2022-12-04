package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathAcceptanceFixture {

    public static ExtractableResponse<Response> 비회원_지하철_최단_경로_요청(Long 출발역_식별_번호, Long 도착역_식별_번호) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", 출발역_식별_번호)
                .param("target", 도착역_식별_번호)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_지하철_최단_경로_요청(String accessToken,
                                                                Long 출발역_식별_번호, Long 도착역_식별_번호) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", 출발역_식별_번호)
                .param("target", 도착역_식별_번호)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

}
