package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.HttpStatus;

public class ExtraFareAcceptanceTestFixture {

    public static void 지하철_최단경로_요금_확인됨(ExtractableResponse<Response> response, int extraFare) {
        assertThat(response.as(PathResponse.class).getExtraFare()).isEqualTo(extraFare);
    }
}
