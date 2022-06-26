package nextstep.subway.path.factory;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;

public class PathAcceptanceFactory {

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }

    public static LineResponse 지하철_노선_등록되어_있음(
            String name,
            String color,
            StationResponse upStation,
            StationResponse downStation,
            int distance,
            int extraFare
    ) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(
            LineResponse line,
            StationResponse upStation,
            StationResponse downStation,
            int distance
    ) {
        return 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 최단_거리_조회(
            String accessToken,
            PathRequest pathRequest
    ) {
        Map<String, Long> queryParams = new HashMap<>();
        queryParams.put("source", pathRequest.getSource());
        queryParams.put("target", pathRequest.getTarget());

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .queryParams(queryParams)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
