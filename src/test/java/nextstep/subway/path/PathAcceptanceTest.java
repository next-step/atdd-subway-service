package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 존재하지않는역;

    /**
     * <pre>
     * 교대역  ---   *2호선*   ---   강남역
     *   |                             |
     * *3호선*                    *신분당선*
     *   |                             |
     * 남부터미널역  --- *3호선* ---  양재
     * </pre>
     */
    @DisplayName("최단 경로 조회")
    @TestFactory
    Stream<DynamicNode> path() {
        return Stream.of(
                dynamicTest("초기 설정을 한다.", this::init),
                dynamicTest("최단거리를 조회한다.", this::searchShortestPath),
                dynamicTest("출발역과 도착역이 같은 최단거리를 조회한다.",
                            this::searchShortestPathWithSameStation),
                dynamicTest("출발역과 도착역이 연결이 되어 있지 않은 최단 거리를 조회한다.",
                            this::searchShortestPathWithNoConnectStation),
                dynamicTest("존재하지 않은 출발역이나 도착역으로 최단 거리를 조회한다.",
                            this::searchShortestPathWithNoStation)
        );
    }

    private void init() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        존재하지않는역 = new StationResponse(7L, "존재하지않는역", LocalDateTime.now(), LocalDateTime.now());

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    private void searchShortestPath() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);

        최단_경로_조회됨(response);
        final PathResponse pathResponse = response.as(PathResponse.class);
        최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(교대역, 남부터미널역, 양재역));
        최단_경로_거리_확인됨(pathResponse, 5);
    }

    private void searchShortestPathWithSameStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 강남역);

        최단_경로_조회_실패됨(response);
    }

    private void searchShortestPathWithNoConnectStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 남부터미널역);

        최단_경로_조회_실패됨(response);
    }

    private void searchShortestPathWithNoStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 존재하지않는역);

        최단_경로_조회_실패됨(response);
    }

    private void 최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 최단_경로_지하철역_순서_정렬됨(PathResponse response, List<StationResponse> stationResponses) {
        List<String> actual = response.getStations()
                                      .stream()
                                      .map(StationResponse::getName)
                                      .collect(Collectors.toList());
        List<String> expected = stationResponses.stream()
                                                .map(StationResponse::getName)
                                                .collect(Collectors.toList());
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    private void 최단_경로_거리_확인됨(PathResponse response, int distance) {
        assertThat(response.getDistance()).isEqualTo(distance);
    }

    public ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStation,
                                                             StationResponse targetStation) {
        return RestAssured
                .given().log().all()
                .when().get("/paths/?source={sourceStationId}&target={targetStationId}", sourceStation.getId(),
                                                                                         targetStation.getId())
                .then().log().all()
                .extract();
    }
}
