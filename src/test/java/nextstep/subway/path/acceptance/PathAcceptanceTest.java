package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestUtils.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.path.acceptance.PathAcceptanceTestUtils.*;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 신논현역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 매봉역;
    private StationResponse 서초역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 고속터미널역;
    private StationResponse 반포역;
    private StationResponse 건대입구역;

    /**
     *  고속터미널역 --- *7호선*[5] ---  반포역
     *
     *                                                        신논현역
     *                                                         |
     *                                                      *신분당선*[7]
     *                                                         |
     *  서초역  --- *2호선*[6] ---  교대역    --- *2호선*[5] ---    강남역
     *                            |                            |
     *                          *3호선*[4]                   *신분당선*[5]
     *                            |                            |
     *                          남부터미널역 --- *3호선*[4] ---   양재역  --- *3호선*[6] --- 매봉역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        신논현역 = 지하철역_등록되어_있음("신논현역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        매봉역 = 지하철역_등록되어_있음("매봉역").as(StationResponse.class);
        서초역 = 지하철역_등록되어_있음("서초역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);
        반포역 = 지하철역_등록되어_있음("반포역").as(StationResponse.class);
        건대입구역 = 지하철역_등록되어_있음("건대입구역").as(StationResponse.class);

        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 신논현역.getId(), 양재역.getId(), 12, 900);
        신분당선 = 지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 5);
        이호선 = 지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);
        LineRequest 삼호선_요청 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 8);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_요청).as(LineResponse.class);
        LineRequest 칠호선_요청 = new LineRequest("칠호선", "bg-lime-700", 고속터미널역.getId(), 반포역.getId(), 5);
        지하철_노선_등록되어_있음(칠호선_요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(신분당선, 신논현역, 강남역, 7);
        지하철_노선에_지하철역_등록되어_있음(이호선, 서초역, 교대역, 6);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 4);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 양재역, 매봉역, 6);
    }

    /**
     * Feature: 지하철 경로 조회
     *
     * Background
     *   Given 여러개의 지하철역이 등록되어 있음
     *   And   어려개의 지하철이 노선 등록되어 있음
     *   And   각 지하철 노선에 지하철역 등록되어 있음
     *   And   각 지하철 노선에 여러 구간이 등록되어 있음
     *
     * Scenario: 출발역과 도착역 사이의 최단 경로 조회
     *   When  지하철 경로 조회 요청
     *   Then  지하철 경로 조회됨
     *   When  지하철 경로 조회 요청 (출발역과 도착역이 같은 경우)
     *   Then  지하철 경로 조회 실패됨
     *   When  지하철 경로 조회 요청 (출발역과 도착역이 연결되어 있지 않은 경우)
     *   Then  지하철 경로 조회 실패됨
     *   When  지하철 경로 조회 요청 (존재하지 않은 출발역이나 도착역을 조회 할 경우)
     *   Then  지하철 경로 조회 실패됨
     */
    @TestFactory
    @DisplayName("지하철 경로 조회 통합 인수 테스트")
    Collection<DynamicTest> PathAcceptance() {
        return Arrays.asList(
                dynamicTest("지하철 경로 조회를 하면 최단 거리의 경로가 조회된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(서초역, 매봉역);

                    // then
                    지하철_경로_조회됨(response);
                    지하철_경로_목록_검증(response, Arrays.asList(서초역, 교대역, 남부터미널역, 양재역, 매봉역));
                    지하철_경로_거리_검증(response, 20);
                    지하철_경로_운임_검증(response, 1450);
                }),
                dynamicTest("출발/도착역을 같은 역으로 조회할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역, 강남역);

                    // then
                    지하철_경로_조회_실패됨(response);
                }),
                dynamicTest("출발역과 도착역이 연결되지 않은 경우 조회할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(고속터미널역, 매봉역);

                    // then
                    지하철_경로_조회_실패됨(response);
                }),
                dynamicTest("존재하지 않은 출발/도착역은 조회할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 지하철_경로_조회_요청(건대입구역, 매봉역);

                    // then
                    지하철_경로_조회_실패됨(response);
                })
        );
    }

    @Test
    @DisplayName("지하철 경로 조회를 했을때 노선 추가 운임이 있으면, 해당 요금이 포함된 운임이 조회된다.")
    void findShortestPathByAdditionalLineFare() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(신논현역, 매봉역);

        // then
        지하철_경로_조회됨(response);
        지하철_경로_목록_검증(response, Arrays.asList(신논현역, 강남역, 양재역, 매봉역));
        지하철_경로_거리_검증(response, 18);
        지하철_경로_운임_검증(response, 2350);
    }
}
