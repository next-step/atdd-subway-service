package nextstep.subway.path.acceptance;

import static io.restassured.RestAssured.*;
import static nextstep.subway.utils.CommonTestFixture.PATH_BASE_PATH;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceTestUtils {
    private static final String DEPARTURE_ID = "departureId";
    private static final String ARRIVAL_ID = "arrivalId";

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(StationResponse departure, StationResponse arrival) {
        Map<String, Long> params = new HashMap<>();
        params.put(DEPARTURE_ID, departure.getId());
        params.put(ARRIVAL_ID, arrival.getId());

        return given().log().all()
                .params(params)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH_BASE_PATH)
                .then().log().all()
                .extract();
    }

    public static void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
