package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회 인수테스트")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 칠호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 철산역;
    private StationResponse 가산디지털단지역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                         |
     * *3호선*                   *신분당선*
     * |                         |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        철산역 = StationAcceptanceTest.지하철역_등록되어_있음("철산역").as(StationResponse.class);
        가산디지털단지역 = StationAcceptanceTest.지하철역_등록되어_있음("가산디지털단지역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);
        칠호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("칠호선", "bg-dark-green-600", 철산역.getId(), 가산디지털단지역.getId(), 5))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Feature: 지하철 최단 경로 조회 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 노선 등록되어 있음
     *     And 노선에 구간이 등록 되어있음
     *
     *   Scenario: 지하철 최단 경로를 조회
     *     When 출발역과 도착역의 최단 경로를 조회 하면
     *     Then 최단경로와 최단거리를 응답받는다.
     **/
    @DisplayName("지하철역 최단경로(+거리)를 조회한다.")
    @Test
    void findShortestPath(){

        //when
        ExtractableResponse<Response> getResponse = 지하철_최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then
        최단_거리와_최단_경로_목록_검증됨(getResponse, 5, "교대역", "남부터미널역", "양재역");
    }

    /**
     * Feature: 지하철 최단 경로 조회 기능(실패)
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 노선 등록되어 있음
     *     And 노선에 구간이 등록 되어있음
     *
     *   Scenario: 지하철 최단 경로를 조회
     *     When 출발역과 도착역이 같은 역으로 조회 할 경우
     *     Then 조회에 실패한다.
     *     When 출발역과 도착역이 연결되지 않은 경로를 조회 할 경우
     *     Then 조회에 실패한다.
     *     When 존재하지 않은 출발역이나 도착역으로 조회 할 경우
     *     Then 조회에 실패한다.
     **/
    @DisplayName("지하철역 최단경로(+거리)를 조회한다.(실패)")
    @TestFactory
    Stream<DynamicTest> findShortestPath_fail(){
        return Stream.of(
            dynamicTest("출발역과 도착역이 같은 역으로 조회 할 경우 실패한다.", () -> {
                //when
                ExtractableResponse<Response> getResponse = 지하철_최단_경로_조회_요청(교대역.getId(), 교대역.getId());

                //then
                최단_경로_조회_실패됨(getResponse);
            }),

            dynamicTest("출발역과 도착역이 연결되지 않은 경로를 조회 할 경우 실패한다.", () -> {
                //when
                ExtractableResponse<Response> getResponse = 지하철_최단_경로_조회_요청(교대역.getId(), 철산역.getId());

                //then
                최단_경로_조회_실패됨(getResponse);
            }),

            dynamicTest("존재하지 않은 출발역이나 도착역으로 조회 할 경우 실패한다.", () -> {

                //when
                StationResponse 존재하지않는역 = new StationResponse(99999999L,"존재하지않는역", LocalDateTime.now(), LocalDateTime.now());
                ExtractableResponse<Response> getResponse1 = 지하철_최단_경로_조회_요청(교대역.getId(), 존재하지않는역.getId());
                ExtractableResponse<Response> getResponse2 = 지하철_최단_경로_조회_요청(존재하지않는역.getId(), 교대역.getId());

                //then
                최단_경로_조회_실패됨(getResponse1);
                최단_경로_조회_실패됨(getResponse2);
            })
        );
    }

    public static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(long sourceStationId, long targetStationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);

        return RestAssured
                .given().log().all()
                .queryParams(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단_거리와_최단_경로_목록_검증됨(ExtractableResponse<Response> getResponse, int distance, String... stationNames) {
        assertThat(getResponse.jsonPath().getLong("distance")).isEqualTo(distance);
        assertThat(getResponse.jsonPath().getList("stations.name")).containsExactly(stationNames);
    }

    private void 최단_경로_조회_실패됨(ExtractableResponse<Response> getResponse) {
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
