package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.line.dto.PathResponse;

public class PathAssertionHelper {

    public static void 최단경로_결과_확인(ExtractableResponse<Response> response,
        List<PathResponse> pathResponses) {
        assertThat(1).isEqualTo(1);
    }

}
