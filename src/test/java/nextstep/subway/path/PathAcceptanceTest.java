package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.ui.dto.PathResponse;
import nextstep.subway.path.ui.dto.StationInPathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    // given
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선*(10m) ---   강남역
     * |                               |
     * *3호선(3m)*                      *신분당선*(10m)
     * |                               |
     * 남부터미널역  --- *3호선*(7m) ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로를 조회할 수 있다.")
    @Test
    void findShortestPathTest() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(10);
        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationInPathResponse::getId)
                .collect(Collectors.toList());
        assertThat(stationIds).containsAll(Arrays.asList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()));
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse destination) {
        return RestAssured.given().log().all()
                .when().get("/paths?source=" + source.getId() + "&target=" + destination.getId())
                .then().log().all()
                .extract();
    }
}
