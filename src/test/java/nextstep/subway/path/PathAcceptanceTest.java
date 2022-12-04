package nextstep.subway.path;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다")
    @Test
    void paths1() {
        // when
        ExtractableResponse<Response> pathsResponse = 최단_경로_조회_요청(강남역, 남부터미널역);

        // then
        최단_경로_응답(pathsResponse, Arrays.asList(강남역, 양재역, 남부터미널역), 12);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(String.format("/paths?source=%d&target=%d", source.getId(), target.getId()))
            .then().log().all()
            .extract();
    }

    private void 최단_경로_응답(ExtractableResponse<Response> response, List<StationResponse> stationResponses,
        int distance) {
        PathResponse path = response.as(PathResponse.class);

        List<PathResponse.PathStationResponse> stations = path.getStations();
        List<Long> stationIds = stations.stream()
            .map(PathResponse.PathStationResponse::getId)
            .collect(toList());
        List<String> stationNames = stations.stream()
            .map(PathResponse.PathStationResponse::getName)
            .collect(toList());

        assertThat(stationIds).containsExactlyElementsOf(
            stationResponses.stream().map(StationResponse::getId).collect(toList()));
        assertThat(stationNames).containsExactlyElementsOf(
            stationResponses.stream().map(StationResponse::getName).collect(toList()));
        List<LocalDateTime> createAtList = stations.stream()
            .map(PathResponse.PathStationResponse::getCreatedAt)
            .collect(toList());
        assertThat(createAtList).hasSize(stationResponses.size());
        assertThat(createAtList).doesNotContainNull();
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color,
        StationResponse upStationResponse, StationResponse downStationResponse, int distance) {
        return 지하철_노선_생성_요청(
            new LineRequest(name, color, upStationResponse.getId(), downStationResponse.getId(), distance))
            .as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation,
        int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }
}
