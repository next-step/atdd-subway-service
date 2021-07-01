package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.VertexResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 사호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 서초역;
    private StationResponse 방배역;
    private StationResponse 사당역;
    private StationResponse 낙성대역;
    private StationResponse 고속터미널역;
    private StationResponse 잠원역;
    private StationResponse 남태령역;
    private StationResponse 총신대입구역;
    private StationResponse 동작역;
    private StationResponse 남성역;
    private StationResponse 내방역;
    private LineResponse 칠호선;
    private StationResponse 반포역;

    /**
     * Background
     * Given 지하철 역들이 등록되어 있음
     * And 지하철 노선들이 등록되어 있음
     * And 지하철 노선에 구간들이 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        서초역 = StationAcceptanceTest.지하철역_등록되어_있음("서초역").as(StationResponse.class);
        방배역 = StationAcceptanceTest.지하철역_등록되어_있음("방배역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        낙성대역 = StationAcceptanceTest.지하철역_등록되어_있음("낙성대역").as(StationResponse.class);

        잠원역 = StationAcceptanceTest.지하철역_등록되어_있음("잠원역").as(StationResponse.class);
        고속터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        남태령역 = StationAcceptanceTest.지하철역_등록되어_있음("남태령역").as(StationResponse.class);
        총신대입구역 = StationAcceptanceTest.지하철역_등록되어_있음("총신대입구").as(StationResponse.class);
        동작역 = StationAcceptanceTest.지하철역_등록되어_있음("동작역").as(StationResponse.class);

        남성역 = StationAcceptanceTest.지하철역_등록되어_있음("남성역").as(StationResponse.class);
        내방역 = StationAcceptanceTest.지하철역_등록되어_있음("내방역").as(StationResponse.class);
        반포역 = StationAcceptanceTest.지하철역_등록되어_있음("반포역").as(StationResponse.class);

        // and
        이호선 = 지하철_노선_등록되어_있음("2호선", "bg-green-600", 강남역, 교대역, 3);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "bg-yellow-600", 고속터미널역, 교대역, 2);
        사호선 = 지하철_노선_등록되어_있음("4호선", "bg-blue-600", 총신대입구역, 사당역, 3);
        칠호선 = 지하철_노선_등록되어_있음("7호선", "bg-dark-green-600", 고속터미널역, 내방역, 3);

        // and
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 서초역, 2);
        지하철_노선에_지하철역_등록되어_있음(이호선, 서초역, 방배역, 2);
        지하철_노선에_지하철역_등록되어_있음(이호선, 방배역, 사당역, 2);

        지하철_노선에_지하철역_등록되어_있음(칠호선, 내방역, 총신대입구역, 5);
        지하철_노선에_지하철역_등록되어_있음(칠호선, 총신대입구역, 남성역, 1);
    }

    /**
     * Scenario: 출발역과 도착역 사이의 최단거리를 가지는 구간을 찾는다
     * When 하나의 노선으로 연결된 다른 두개의 역 사이의 최단경로 요청
     * Then 지하철 최단경로 구간 조회됨
     * And 지하철 최단경로에 포함된 역들이 확인됨
     * And 지하철 최단경로 거리 확인됨
     * When 두개의 노선으로 연결된 다른 두 개의 역 사이의 최단경로 요청
     * Then 지하철 최단경로 구간 조회됨
     * And 지하철 최단경로에 포함된 역들이 확인됨
     * And 지하철 최단경로 거리 확인됨
     * When 세개의 노선으로 연결된 다른 두 개의 역 사이의 지하철 최단경로 요청
     * Then 지하철 최단경로 구간 조회됨
     * And 지하철 최단경로에 포함된 역들이 확인됨
     * And 지하철 최단경로 거리 확인됨
     */
    @TestFactory
    @DisplayName("성공하는 최단경로 조회 시나리오")
    List<DynamicTest> find_shortestPath() {
        return Arrays.asList(
                dynamicTest("하나의 노선에 존재하는 다른 두 역의 최단구간 요청", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_역_사이의_최단_구간_조회_요청(강남역.getId(), 서초역.getId());

                    // then
                    지하철_역사이의_최단_구간_조회_요청됨(response);
                    지하철_최단경로에_포함된_역들이_확인됨(response, Arrays.asList(강남역, 교대역, 서초역));
                    지하철_최단경로_거리_확인됨(response, 5);
                }),
                dynamicTest("두개의 노선으로 연결되는 다른 두 역의 최단구간 요청", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_역_사이의_최단_구간_조회_요청(서초역.getId(), 총신대입구역.getId());

                    // then
                    지하철_역사이의_최단_구간_조회_요청됨(response);
                    지하철_최단경로에_포함된_역들이_확인됨(response, Arrays.asList(서초역, 방배역, 사당역, 총신대입구역));
                    지하철_최단경로_거리_확인됨(response, 7);
                }),
                dynamicTest("세개의 노선으로 연결되는 다른 두 역의 최단구간 요청 - (구간의 수는 많지만 길이가 짧은 경로을 선택)", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_역_사이의_최단_구간_조회_요청(강남역.getId(), 남성역.getId());

                    // then
                    지하철_역사이의_최단_구간_조회_요청됨(response);
                    지하철_최단경로에_포함된_역들이_확인됨(response, Arrays.asList(강남역, 교대역, 서초역, 방배역, 사당역, 총신대입구역, 남성역));
                    지하철_최단경로_거리_확인됨(response, 13);
                })
        );
    }

    /**
     * Scenario: 최단거리를 가지는 구간을 찾을 때 발생하는 오류 상황을 파악한다.
     * When 동일한 두 역 사이의 최단경로 요청
     * Then 최단경로 요청 실패함
     * And 오류 메시지 확인됨
     * Given 연결되지 않은 역과 노선을 등록한다
     * When 노선으로 연결되지 않은 두 역 사이의 최단경로 요청
     * Then 최단경로 요청 실패함
     * And 오류 메시지 확인됨
     * When 등록되지 않은 역 사이의 최단경로 요청
     * Then 최단경로 요청 실패함
     * And 오류 메시지 확인됨
     */
    @TestFactory
    @DisplayName("실패하는 예외상황 시나리오")
    List<DynamicTest> path_error() {
        return Arrays.asList(
                dynamicTest("출발역과 도착역이 같을 경우", () -> {
                    // when
                    ExtractableResponse<Response> 지하철_역사이의_최단_구간_조회_요청 = 지하철_역_사이의_최단_구간_조회_요청(서초역.getId(), 서초역.getId());

                    // then
                    지하철_역_사이의_최단구간_조회_실패함(지하철_역사이의_최단_구간_조회_요청);
                    요청_실패_메시지_확인됨(지하철_역사이의_최단_구간_조회_요청, "경로조회 출발역과 도착역이 같습니다.");
                }),
                dynamicTest("서로 다른 두 역이 노선으로 연결되어 있지 않은 경우", () -> {
                    // given
                    StationResponse 노들역 = StationAcceptanceTest.지하철역_등록되어_있음("노들역").as(StationResponse.class);
                    StationResponse 흑석역 = StationAcceptanceTest.지하철역_등록되어_있음("흑석역").as(StationResponse.class);
                    지하철_노선_등록되어_있음("9호선", "bg-green-600", 노들역, 흑석역, 3);

                    // when
                    ExtractableResponse<Response> 지하철_역사이의_최단_구간_조회_요청 = 지하철_역_사이의_최단_구간_조회_요청(서초역.getId(), 노들역.getId());

                    // then
                    지하철_역_사이의_최단구간_조회_실패함(지하철_역사이의_최단_구간_조회_요청);
                    요청_실패_메시지_확인됨(지하철_역사이의_최단_구간_조회_요청, "대상들이 연결되어 있지 않습니다.");
                }),
                dynamicTest("등록되지 않은 역 사이의 최단경로 조회할 경우", () -> {
                    // when
                    ExtractableResponse<Response> 지하철_역사이의_최단_구간_조회_요청 = 지하철_역_사이의_최단_구간_조회_요청(서초역.getId(), 100L);

                    // then
                    지하철_역_사이의_최단구간_조회_실패함(지하철_역사이의_최단_구간_조회_요청);
                    요청_실패_메시지_확인됨(지하철_역사이의_최단_구간_조회_요청, "조회된 역이 없습니다.");
                }),
                dynamicTest("구간에 등록되지 않은 도착역 까지의 최단경로 조회할 경우", () -> {
                    // given
                    StationResponse 노량진역 = StationAcceptanceTest.지하철역_등록되어_있음("노량진역").as(StationResponse.class);

                    // when
                    ExtractableResponse<Response> 지하철_역사이의_최단_구간_조회_요청 = 지하철_역_사이의_최단_구간_조회_요청(서초역.getId(), 노량진역.getId());

                    // then
                    지하철_역_사이의_최단구간_조회_실패함(지하철_역사이의_최단_구간_조회_요청);
                    요청_실패_메시지_확인됨(지하철_역사이의_최단_구간_조회_요청, "도착점이 경로에 포함되어 있지 않습니다.");
                })
        );
    }

    private void 요청_실패_메시지_확인됨(ExtractableResponse<Response> response, String userErrorMessage) {
        String errorMessage = response.jsonPath().getObject("errorMessage", String.class);
        assertThat(errorMessage).isEqualTo(userErrorMessage);
    }

    private void 지하철_역_사이의_최단구간_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_최단경로_거리_확인됨(ExtractableResponse<Response> response, int distance) {
        Integer resultDistance = response.jsonPath()
                .getObject("distance", Integer.class);
        assertThat(resultDistance).isEqualTo(distance);
    }

    private void 지하철_최단경로에_포함된_역들이_확인됨(ExtractableResponse<Response> response, List<StationResponse> stations) {
        Stream<String> stationNames = response.jsonPath()
                .getList("stations", VertexResponse.class)
                .stream()
                .map(VertexResponse::getName);
        List<String> targetStationNames = stations.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(Arrays.equals(stationNames.toArray(), targetStationNames.toArray())).isTrue();
    }

    private void 지하철_역사이의_최단_구간_조회_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철_역_사이의_최단_구간_조회_요청(Long source, Long target) {
        return RestAssured.given().log().all()
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/paths?source=" + source + "&target=" + target)
                .then().log().all()
                .extract();
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStationResponse,
                                        StationResponse downStationResponse, int distance) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(lineName, color, upStationResponse.getId(),
                downStationResponse.getId(), distance)).as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStationResponse,
                                      StationResponse downStationResponse, int distance) {
        LineSectionAcceptanceTest.지하철_구간_등록_요청(lineResponse.getId(), new SectionRequest(upStationResponse.getId(),
                downStationResponse.getId(), distance));
    }
}
