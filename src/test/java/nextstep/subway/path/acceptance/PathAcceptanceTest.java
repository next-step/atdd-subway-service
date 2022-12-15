package nextstep.subway.path.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.global.message.GlobalAdviceMessage;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.rest.LineRestAssured;
import nextstep.subway.line.rest.LineSectionRestAssured;
import nextstep.subway.path.message.PathMessage;
import nextstep.subway.path.rest.PathRestAssured;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.rest.StationRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 구호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 당산역;
    private StationResponse 여의도역;
    private StationResponse 학동역;

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

        강남역 = StationRestAssured.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationRestAssured.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationRestAssured.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationRestAssured.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        당산역 = StationRestAssured.지하철역_등록되어_있음("당산역").as(StationResponse.class);
        여의도역 = StationRestAssured.지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        학동역 = StationRestAssured.지하철역_등록되어_있음("학동역").as(StationResponse.class);

        LineCreateRequest 신분당선_생성_요청 = new LineCreateRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 13, 700);
        LineCreateRequest 이호선_생성_요청 = new LineCreateRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 15, 0);
        LineCreateRequest 삼호선_생성_요청 = new LineCreateRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 7, 0);
        LineCreateRequest 구호선_생성_요청 = new LineCreateRequest("구호선", "bg-red-600", 당산역.getId(), 여의도역.getId(), 17, 0);

        신분당선 = LineRestAssured.지하철_노선_등록되어_있음(신분당선_생성_요청).as(LineResponse.class);
        이호선 = LineRestAssured.지하철_노선_등록되어_있음(이호선_생성_요청).as(LineResponse.class);
        삼호선 = LineRestAssured.지하철_노선_등록되어_있음(삼호선_생성_요청).as(LineResponse.class);
        LineSectionRestAssured.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        구호선 = LineRestAssured.지하철_노선_등록되어_있음(구호선_생성_요청).as(LineResponse.class);
    }
    
    /**
     * Given 시작역과 도착역이 주어진경우
     * When 최단 경로 조회시
     * Then 최단 경로를 반환한다
     */
    @DisplayName("시작역[강남역]와 도착역[남부터미널역]을 선택하여 최단 경로를 조회하면 경로에 포함 된 역과 총 거리를 반환한다")
    @Test
    void find_shortest_path_test() {
        // when
        ExtractableResponse<Response> response = PathRestAssured.최단경로조회_요청하기(강남역.getId(), 남부터미널역.getId());

        // then
        JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(17),
                () -> assertThat(jsonPath.getList("stations.name")).containsExactly("강남역", "양재역", "남부터미널역"),
                () -> assertThat(jsonPath.getInt("fare")).isEqualTo(2_150)
        );
    }

    /**
     * Given 시작역이 등록이 안되어 있는 경우
     * When 최단경로 조회시
     * Then 예외처리 된다
     */
    @DisplayName("노선에 등록되지 않은 시작역을 선택하여 최단 경로를 조회하면 예외 처리되어 최단 경로를 조회할 수 없다")
    @Test
    void find_shortest_path_with_not_enrolled_source_station_test() {
        // when
        ExtractableResponse<Response> response = PathRestAssured.최단경로조회_요청하기(학동역.getId(), 남부터미널역.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo(PathMessage.GRAPH_ERROR_NOT_FOUND_SOURCE_STATION.message())
        );
    }

    /**
     * Given 도착역 등록이 안되어 있는 경우
     * When 최단경로 조회시
     * Then 예외처리 된다
     */
    @DisplayName("노선에 등록되지 않은 도착역을 선택하여 최단 경로를 조회하면 예외 처리되어 최단 경로를 조회할 수 없다")
    @Test
    void find_shortest_path_with_not_enrolled_target_station_test() {
        // when
        ExtractableResponse<Response> response = PathRestAssured.최단경로조회_요청하기(강남역.getId(), 학동역.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo(PathMessage.GRAPH_ERROR_NOT_FOUND_TARGET_STATION.message())
        );
    }

    /**
     * Given 출발역과 도착역이 동일한 경우
     * When 최단경로 조회시
     * Then 예외처리 된다
     */
    @DisplayName("시작역와 도착역을 동일한 역으로 선택하여 최단 경로를 조회하면 예외 처리되어 최단 경로를 조회할 수 없다")
    @Test
    void find_shortest_path_with_same_stations_test() {
        // when
        ExtractableResponse<Response> response = PathRestAssured.최단경로조회_요청하기(강남역.getId(), 강남역.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo(PathMessage.GRAPH_ERROR_SOURCE_AND_TARGET_STATION_IS_EQUALS.message())
        );
    }

    /**
     * Given 출발역과 도착역이 동일한 경우
     * When 최단경로 조회시
     * Then 예외처리 된다
     */
    @DisplayName("연결되지 않은 역의 최단 경로를 조회하면 예외 처리되어 최단 경로를 조회할 수 없다")
    @Test
    void find_shortest_path_with_not_connected_stations_test() {
        // when
        ExtractableResponse<Response> response = PathRestAssured.최단경로조회_요청하기(강남역.getId(), 여의도역.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo(PathMessage.GRAPH_ERROR_NOT_CONNECTED_STATIONS.message())
        );
    }

    /**
     * Given 등록이 안된 출발역이 주어진 경우
     * When 최단경로 조회시
     * Then 예외처리 된다
     */
    @DisplayName("미등록된역을 선택하여 최단 경로를 조회하면 예외 처리되어 최단 경로를 조회할 수 없다")
    @Test
    void find_shortest_path_with_not_found_source_station_test() {
        // when
        ExtractableResponse<Response> response = PathRestAssured.최단경로조회_요청하기(99L, 여의도역.getId());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo(GlobalAdviceMessage.NOT_FOUND_ENTITY.message())
        );
    }
}
