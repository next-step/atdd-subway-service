package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 경로 조회")
    @Test
    void 최단_거리() {

        ExtractableResponse<Response> 지하철_경로_조회_요청 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", 1L)
                .param("target", 2L)
                .when().get("/paths")
                .then().log().all()
                .extract();


    }
}
