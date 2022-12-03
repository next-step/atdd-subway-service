package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
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

    private StationResponse 부산역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @DisplayName("지하철 구간 관리 시나리오 테스트")
    @TestFactory
    Stream<DynamicNode> scenario() {
        return Stream.of(
            dynamicTest("초기 설정을 한다.", this::initialization),
            dynamicTest("최단 경로를 조회한다.", this::find_shortest_path),
            dynamicTest("출발지와 목적지가 같은 최단 경로를 조회한다.", this::find_shortest_path_same_station),
            dynamicTest("출발역과 도착역이 연결이 되어 있지 않은 최단 경로를 조회한다.", this::find_shortest_path_no_connect_station),
            dynamicTest("등록되지 않은 역으로 최단 경로를 조회한다.", this::find_shortest_path_no_exist_station)
        );
    }

    private void initialization() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        부산역 = StationAcceptanceTest.지하철역_등록되어_있음("부산역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    private void find_shortest_path() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        최단_경로_조회_응답됨(response);
        경로_순서에_맞게_노출됨(response, Arrays.asList(교대역, 남부터미널역, 양재역));
        최단_경로_거리_확인됨(response, 5);
    }

    private void find_shortest_path_same_station() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        최단_경로_조회_실패됨(response);
    }

    private void find_shortest_path_no_connect_station() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(교대역.getId(), 부산역.getId());

        // then
        최단_경로_조회_실패됨(response);
    }

    private void find_shortest_path_no_exist_station() {
        //given
        StationResponse 테스트역 = new StationResponse(123L, "테스트역", LocalDateTime.now(), LocalDateTime.now());
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(교대역.getId(), 테스트역.getId());

        // then
        최단_경로_조회_실패됨(response);
    }

    private static ExtractableResponse<Response> 최단경로_조회_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .queryParam("source", source)
            .queryParam("target", target)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("paths")
            .then().log().all()
            .extract();
    }

    private static void 최단_경로_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 경로_순서에_맞게_노출됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> stationIds = pathResponse.getStations().stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private static void 최단_경로_거리_확인됨(ExtractableResponse<Response> response, int distance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }
}
