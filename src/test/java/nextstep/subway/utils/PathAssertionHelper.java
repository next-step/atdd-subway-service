package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpStatus;

public class PathAssertionHelper {

    public static void 최단경로_결과_확인(ExtractableResponse<Response> response, List<String> stations,
        int distance) {
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations")).hasSize(3).extracting("name")
                .containsExactlyElementsOf(stations),
            () -> assertThat(response.jsonPath().get("distance").toString()).isEqualTo(
                String.valueOf(distance / 1.0)));

        ;
    }

    public static void 최단경로_조회불가(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
