package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 6));
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10));
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5));
        LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 남부터미널역.getId(), 3));
    }

    @Test
    void 최단_경로_조회() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(남부터미널역.getId(), 강남역.getId());

        //then
        최단_경로에_지하철역_순서_정렬됨(response, Arrays.asList(남부터미널역, 교대역, 강남역));
        최단_경로_총_거리값_확인됨(response, 13);
    }

    public static void 최단_경로_총_거리값_확인됨(ExtractableResponse<Response> response, long distance) {
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance);
    }

    public static void 최단_경로에_지하철역_순서_정렬됨(
            ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream().map(StationResponse::getId).collect(Collectors.toList());

        List<Long> expectedStationIds =
                expectedStations.stream().map(StationResponse::getId).collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(long sourceStationId, long targetStationId) {
        return RestAssured.given().log().all().accept(MediaType.APPLICATION_JSON_VALUE).when()
                .get("/paths?source={sourceStationId}&target={targetStationId}", sourceStationId, targetStationId)
                .then().log().all().extract();
    }
}
