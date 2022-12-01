package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class PathRestAssured {
    public static ExtractableResponse<Response> 최단경로_조회_요청(
            StationResponse upStation,
            StationResponse downStation
    ) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={upStationId}&target={downStationId}", upStation.getId(), downStation.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_상태로_최단경로_조회_요청(
            String token,
            StationResponse upStation,
            StationResponse downStation
    ) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={upStationId}&target={downStationId}", upStation.getId(), downStation.getId())
                .then().log().all()
                .extract();
    }
}
