package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
     * |                             |
     * *3호선*                   *신분당선*
     * |                             |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        String uri = String.format("/lines/%d/sections", lineResponse.getId());
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        post(uri, sectionRequest);
    }

    @Test
    @DisplayName("최단거리 검색")
    public void 최단거리_검색() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 양재역.getId());
        PathResponse pathResponse = response.as(PathResponse.class);

        assertAll(
                () -> assertThat(pathResponse.getDistance().getDistance()).isEqualTo(5),
                () -> {
                    List<String> stationNames = pathResponse.getStations().getStationResponses()
                            .stream()
                            .map(stationResponse -> stationResponse.getName())
                            .collect(Collectors.toList());
                    assertThat(stationNames).contains("교대역", "남부터미널역", "양재역");
                }
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 동일할 경우 오류발생")
    public void 출발역과_도착역이_동일할_경우_오류발생() {
        ExtractableResponse<Response> response = 최단_거리_요청(교대역.getId(), 교대역.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("존재하지 않는 역으로 조회할 경우 오류발생")
    public void 존재하지_않는_역으로_조회할_경우_오류발생() {
        ExtractableResponse<Response> response = 최단_거리_요청(0L, 교대역.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 최단_거리_요청(Long sourceId, Long targetId) {
        String uri = String.format("/paths?source=%d&target=%d", sourceId, targetId);
        return get(uri);
    }

}
