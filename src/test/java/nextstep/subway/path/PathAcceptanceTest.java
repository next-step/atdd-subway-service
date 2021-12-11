package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestTestApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


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

        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(),
            10);
        LineRequest lineRequest2 = new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(),
            10);
        LineRequest lineRequest3 = new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(),
            5);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest2).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest3).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findShortestPath() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 남부터미널역);

        // then 최단 경로 조회 됨
        최단_경로_조회됨(response);
        최단_경로와_예상_경로_일치함(response, Arrays.asList(강남역, 교대역, 남부터미널역));
        최단_경로의_예상_거리_일치함(response, 13);
    }

    @DisplayName("동일한 역으로 최단 경로를 조회 할 수 없다.")
    @Test
    void findShortestPathSameStation() {
        // when 동일한 역으로 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 강남역);

        // then 최단 경로 조회 됨
        최단_경로_조회_실패됨(response);
    }

    static void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source,
        StationResponse target) {
        String uri = String.format("paths?source=%d&target=%d", source.getId(), target.getId());

        return RestTestApi.get(uri);
    }

    static void 최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static void 최단_경로와_예상_경로_일치함(ExtractableResponse<Response> response,
        List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
            .map(Station::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    static void 최단_경로의_예상_거리_일치함(ExtractableResponse<Response> response, int expectedDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }
}