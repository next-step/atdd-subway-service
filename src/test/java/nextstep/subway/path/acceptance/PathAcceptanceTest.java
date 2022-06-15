package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
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

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
            .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("지하철역 사이의 최단 경로를 조회한다.")
    void findShortestPath() {
        PathResponse pathResponse = 최단_경로를_조회함(교대역, 양재역).as(PathResponse.class);

        assertThat(pathResponse.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
    }

    @TestFactory
    @DisplayName("최단경로 조회의 예외케이스를 검증한다.")
    Stream<DynamicTest> findShortestPath_fail() {
        return Stream.of(
            dynamicTest("출발역과 도착역을 같은 역으로 조회할 경우", () -> 최단_경로_조회시_실패_검증(교대역, 교대역)),
            dynamicTest("출발역과 도착역이 연결이 되어 있지 않은 경우", () -> {
                StationResponse 연결되지_않는_역 = StationAcceptanceTest.지하철역_등록되어_있음("연결되지 않은 역")
                    .as(StationResponse.class);
                최단_경로_조회시_실패_검증(교대역, 연결되지_않는_역);
            }),
            dynamicTest("존재하지 않은 출발역이나 도착역을 조회 할 경우", () -> {
                StationResponse 존재하지_않는_역 = new StationResponse(-1L, "존재하지 않는 역", LocalDateTime.now(),
                    LocalDateTime.now());
                최단_경로_조회시_실패_검증(교대역, 존재하지_않는_역);
            })
        );
    }

    private void 최단_경로_조회시_실패_검증(StationResponse sourceStation, StationResponse targetStation ) {
        assertThat(최단_경로를_조회함(sourceStation, targetStation).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 최단_경로를_조회함(StationResponse sourceStation, StationResponse targetStation) {
        return get("/paths?source={sourceStationId}&target={targetStationId}", sourceStation.getId(), targetStation.getId());
    }

}
