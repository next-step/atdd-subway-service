package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.domain.Station;
import org.springframework.http.MediaType;

public class PathAcceptanceTestFixture {

    public static ExtractableResponse<Response> 최단경로_조회_요청(Station upStation, Station downStation) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={upStationId}&target={downStationId}", upStation.getId(), downStation.getId())
            .then().log().all()
            .extract();
    }
}
