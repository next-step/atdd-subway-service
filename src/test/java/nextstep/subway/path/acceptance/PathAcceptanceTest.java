package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineRestAssured.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionRestAssured.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.path.acceptance.PathRestAssured.지하철_경로_조회_요청;
import static nextstep.subway.station.acceptance.StationRestAssured.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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
    private LineResponse 분당선;
    private LineResponse 구호선;
    private LineResponse 칠호선;
    private StationResponse 종합운동장역;
    private StationResponse 삼성역;
    private StationResponse 선릉역;
    private StationResponse 강남역;
    private StationResponse 신논현역;
    private StationResponse 선정릉역;
    private StationResponse 한티역;
    private StationResponse 반포역;
    private StationResponse 이수역;

    /**
     * 이수역 --- *7호선*[20] --- 반포역
     *
     * 신논현역 --- *9호선*[12] --- 선정릉역 --- *9호선*[15] --- 종합운동장역
     *   |                         |                      |
     * *신분당선*[10]              *분당선*[5]           *2호선*[4]
     *   |                        |                     |
     * 강남역 --- *2호선*[6] --- 선릉역 --- *2호선*[4] --- 삼성역
     *                         |
     *                      *분당선*[5]
     *                        |
     *                      한티역
     */
    @BeforeEach
    void setup() {
        종합운동장역 = 지하철역_등록되어_있음("종합운동장역").as(StationResponse.class);
        삼성역 = 지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        신논현역 = 지하철역_등록되어_있음("신논현역").as(StationResponse.class);
        선정릉역 = 지하철역_등록되어_있음("선정릉역").as(StationResponse.class);
        한티역 = 지하철역_등록되어_있음("한티역").as(StationResponse.class);
        반포역 = 지하철역_등록되어_있음("반포역").as(StationResponse.class);
        이수역 = 지하철역_등록되어_있음("이수역").as(StationResponse.class);

        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 신논현역.getId(), 강남역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-green-600", 강남역.getId(), 선릉역.getId(), 6);
        이호선 = 지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);
        LineRequest 구호선_요청 = new LineRequest("구호선", "bg-gold-600", 신논현역.getId(), 선정릉역.getId(), 12);
        구호선 = 지하철_노선_등록되어_있음(구호선_요청).as(LineResponse.class);
        LineRequest 칠호선_요청 = new LineRequest("칠호선", "bg-khaki-600", 이수역.getId(), 반포역.getId(), 20, 1000);
        칠호선 = 지하철_노선_등록되어_있음(칠호선_요청).as(LineResponse.class);
        LineRequest 분당선_요청 = new LineRequest("분당선", "bg-yellow-600", 선정릉역.getId(), 선릉역.getId(), 5);
        분당선 = 지하철_노선_등록되어_있음(분당선_요청).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(이호선, 선릉역, 삼성역, 4);
        지하철_노선에_지하철역_등록되어_있음(이호선, 삼성역, 종합운동장역, 4);
        지하철_노선에_지하철역_등록되어_있음(구호선, 선정릉역, 종합운동장역, 15);
        지하철_노선에_지하철역_등록되어_있음(분당선, 선릉역, 한티역, 5);
    }

    /**
     * Feature: 지하철 경로 조회 기능
     *
     *   Background (@BeforeEach)
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선이 여러개 등록되어 있음
     *     And 각 지하철 노선에 지하철역 등록되어 있음
     *     And 각 지하철 노선에는 여러 구간이 등록되어 있음
     *
     *   Scenario: 출발역과 도착역 사이 최단 경로 조회
     *     When 지하철 경로 조회 요청
     *     Then 최단 경로 조회됨
     *     When 지하철 경로 조회 요청(출발역과 도착역이 연결 안된 경우)
     *     Then 경로 조회 실패됨
     *     When 지하철 경로 조회 요청(존재하지 않는 출발역이나 도착역 조회한 경우)
     *     Then 경로 조회 실패됨
     *     When 지하철 경로 조회 요청(출발역과 도착역이 같은 경우)
     *     Then 경로 조회 실패됨
     */
    @DisplayName("출발지부터 도착지까지의 최단 경로를 반환한다.")
    @TestFactory
    Collection<DynamicTest>  findShortestPath() {
        // BackGround
        return Arrays.asList(
                DynamicTest.dynamicTest("지하철 경로 조회를 하면 최단 거리의 경로가 조회된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(신논현역.getId(), 한티역.getId());

                    // then
                    지하철_경로_조회됨(response);
                    지하철_최단_경로_조회됨(response, Arrays.asList(신논현역, 강남역, 선릉역, 한티역), 21, 1550);
                }),
                DynamicTest.dynamicTest("연결되지 않은 출발역과 도착역 사이의 경로를 조회할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(선릉역.getId(), 이수역.getId());

                    // then
                    지하철_경로_조회_실패됨(response);
                }),
                DynamicTest.dynamicTest("존재하지 않는 출발역이나 도착역 사이의 경로를 조회할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(null, 이수역.getId());

                    // then
                    지하철_경로_조회_실패됨(response);
                }),
                DynamicTest.dynamicTest("출발역과 도착역이 같을 때 경로를 조회할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(종합운동장역.getId(), 종합운동장역.getId());

                    // then
                    지하철_경로_조회_실패됨(response);
                })
        );
    }

    @DisplayName("최단 경로에 속한 노선에 추가 요금이 존재하면, 해당 요금이 포함된 최단 경로를 반환한다.")
    @Test
    void findShortestPathWithLineFare() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(이수역.getId(), 반포역.getId());
        int fare = 1450;

        // then
        지하철_경로_조회됨(response);
        지하철_최단_경로_조회됨(response, Arrays.asList(이수역, 반포역), 20, fare + 칠호선.getLineFare());
    }

    private static void 지하철_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStations, int expectDistance, int expectFare) {
        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        List<Long> expectStationIds = expectStations.stream().map(StationResponse::getId).collect(Collectors.toList());
        List<String> actualStationNames = response.jsonPath().getList("stations.name", String.class);
        List<String> expectStationNames = expectStations.stream().map(StationResponse::getName).collect(Collectors.toList());
        int actualDistance = response.jsonPath().getInt("distance");
        int actualFare = response.jsonPath().getInt("fare");
        assertAll(
                () -> assertThat(actualStationIds).containsExactlyElementsOf(expectStationIds),
                () -> assertThat(actualStationNames).containsExactlyElementsOf(expectStationNames),
                () -> assertThat(actualDistance).isEqualTo(expectDistance),
                () -> assertThat(actualFare).isEqualTo(expectFare)
        );
    }
}
