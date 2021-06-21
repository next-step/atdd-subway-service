package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 칠호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남강역;
    private StationResponse 대교역;
    private StationResponse 널미터부남역;
    private StationResponse 남부터미널역;
    private StationResponse 노원역;
    private StationResponse 군자역;

    /**
     * 교대역    --- *2호선* ---     강남역 --- *2호선* ---   대교역
     * |            30                |         11
     * |                        *신분당선* 17
     * |                              |
     * |                            남강역
     * |                              |
     * *3호선* 43                  *신분당선* 10
     * |                              |
     * 남부터미널역  --- *3호선* ---   양재   --- *3호선* --- 널미터부남역
     *                  2                       21
     *
     *
     * 노원역 --- *7호선* --- 군자역
     *             77
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남강역 = StationAcceptanceTest.지하철역_등록되어_있음("남강역").as(StationResponse.class);
        대교역 = StationAcceptanceTest.지하철역_등록되어_있음("대교역").as(StationResponse.class);
        노원역 = StationAcceptanceTest.지하철역_등록되어_있음("노원역").as(StationResponse.class);
        군자역 = StationAcceptanceTest.지하철역_등록되어_있음("군자역").as(StationResponse.class);
        널미터부남역 = StationAcceptanceTest.지하철역_등록되어_있음("널미터부남역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 27))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);
        칠호선 = 지하철_노선_등록되어_있음(
                new LineRequest("칠호선", "bg-red-600", 노원역.getId(), 군자역.getId(), 77))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 43);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 남강역, 17);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 대교역, 11);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 널미터부남역, 21);
    }

    @Test
    void 없는_역의_경로를_확인해서_실패한다() {
        Long 등록안된역의_ID = 111L;
        // When
        PathRequest pathRequest = PathRequest.of(강남역.getId(), 등록안된역의_ID);
        ExtractableResponse<Response> pathsResponse = 출발역과_도착역으로_최단경로를_찾는다(pathRequest);

        // Then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 연결되지_않은_출발역과_도착역의_최단거리를_확인해서_실패한다() {
        // When
        PathRequest pathRequest = PathRequest.of(노원역.getId(), 강남역.getId());
        ExtractableResponse<Response> pathsResponse = 출발역과_도착역으로_최단경로를_찾는다(pathRequest);

        // Then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 출발역과_도착역이_같은_경우의_최단거리를_확인해서_실패한다() {
        // When
        PathRequest pathRequest = PathRequest.of(강남역.getId(), 강남역.getId());
        ExtractableResponse<Response> pathsResponse = 출발역과_도착역으로_최단경로를_찾는다(pathRequest);

        // Then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 등록된_노선들에서_출발역과_도착역의_최단거리를_확인한다() {
        // Given (setUp())

        // When
        PathRequest pathRequest = PathRequest.of(대교역.getId(), 널미터부남역.getId());
        ExtractableResponse<Response> pathsResponse = 출발역과_도착역으로_최단경로를_찾는다(pathRequest);

        // Then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        최단경로의_역들과_최단거리를_반환함(pathsResponse,
                Arrays.asList(대교역, 강남역, 남강역, 양재역, 널미터부남역), 59);
    }

    private ExtractableResponse<Response> 출발역과_도착역으로_최단경로를_찾는다(PathRequest pathRequest) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}", pathRequest.getSourceStationId(), pathRequest.getTargetStationId())
                .then().log().all()
                .extract();
    }

    private void 최단경로의_역들과_최단거리를_반환함(ExtractableResponse<Response> pathResponse,
                                     List<StationResponse> expectedStations,
                                     int distance) {
        PathResponse path = pathResponse.as(PathResponse.class);

        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
        assertThat(path.getDistance()).isEqualTo(distance);
    }

}
