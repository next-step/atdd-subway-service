package nextstep.subway.station;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

public class StationAcceptanceMethods {
    public static final String LOCATION_HEADER_NAME = "Location";
    private static final String STATION_URL_PATH = "/stations";
    private static final String SLASH_SIGN = "/";
    private static final String DOT_SIGN = ".";

    private StationAcceptanceMethods() {}

    public static ExtractableResponse<Response> 지하철역_생성_요청(StationRequest stationRequest) {
        return post(STATION_URL_PATH, stationRequest);
    }

    public static ExtractableResponse<Response> 지하철역_등록되어_있음(String name) {
        StationRequest stationRequest = StationRequest.of(name);
        return 지하철역_생성_요청(stationRequest);
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return get(STATION_URL_PATH);
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER_NAME);

        return delete(uri);
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION_HEADER_NAME)).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = StreamUtils.mapToList(createdResponses,
                                                           it -> Long.parseLong(it.header(LOCATION_HEADER_NAME)
                                                                                  .split(SLASH_SIGN)[2]));
        List<Long> resultLineIds = StreamUtils.mapToList(response.jsonPath().getList(DOT_SIGN, StationResponse.class),
                                                         StationResponse::getId);

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
