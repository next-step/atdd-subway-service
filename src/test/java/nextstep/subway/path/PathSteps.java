package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;

import static nextstep.subway.AcceptanceTest.*;

public class PathSteps {

    public static ExtractableResponse<Response> 최단_경로_조회_요청(String accessToken, StationResponse sourceStation, StationResponse targetStation) {
        return 최단_경로_조회_요청(accessToken, sourceStation.getId(), targetStation.getId());
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        return getWithAuth("/paths?source={source}&target={target}", accessToken, sourceStationId, targetStationId);
    }
}
