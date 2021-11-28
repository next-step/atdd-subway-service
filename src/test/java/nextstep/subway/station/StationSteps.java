package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationRequest;

import static nextstep.subway.AcceptanceTest.*;

public class StationSteps {

    public static ExtractableResponse<Response> 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);
        return post("/stations", stationRequest);
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return get("/stations");
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return delete(uri);
    }
}
