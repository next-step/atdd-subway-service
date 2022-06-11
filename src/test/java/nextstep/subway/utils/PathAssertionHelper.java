package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class PathAssertionHelper {

    public static void 최단경로_결과_확인(ExtractableResponse<Response> response, List<String> stations,
        int distance) {
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations")).hasSize(3).extracting("name")
                .containsExactlyElementsOf(stations),
            () -> assertThat(response.jsonPath().get("distance").toString()).isEqualTo(
                String.valueOf(distance)));

        ;
    }

}
