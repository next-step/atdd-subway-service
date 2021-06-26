package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.PageController.URIMapping.PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    public static final RestAssuredTemplate restAssuredTemplate = new RestAssuredTemplate(PATH);

    @Test
    @DisplayName("지하철 최단경로를 조회한다.")
    public void getTest() {
        // when
        ExtractableResponse<Response> response = requestPath(getDefaultParam());

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(PathResponse.class).getStationList()).isNotNull(),
            () -> assertThat(response.as(PathResponse.class).getDistance()).isNotNull()
        );
    }

    private Map<String, String> getDefaultParam() {
        return new HashMap<String, String>() {
            {
                put("source", "1");
                put("target", "6");
            }
        };
    }

    public static ExtractableResponse<Response> requestPath(Map<String, String> query) {
        return restAssuredTemplate.get(query);
    }
}
