package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.path.acceptance.step.PathAcceptanceStep.지하철_경로_응답됨;
import static nextstep.subway.path.acceptance.step.PathAcceptanceStep.지하철_경로_조회_요청;
import static nextstep.subway.path.acceptance.step.PathAcceptanceStep.지하철_경로_추출;
import static nextstep.subway.path.acceptance.step.PathAcceptanceStep.지하철_시작_종료지점이_경로에_포함됨;
import static nextstep.subway.path.acceptance.step.PathAcceptanceStep.지하철_이용_요금도_함께_응답함;
import static nextstep.subway.path.acceptance.step.PathAcceptanceStep.총_거리도_함께_응답함;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;


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

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        final LineRequest 노선_등록_요청1 = LineRequest.builder()
                .name("이호선")
                .color("green")
                .upStationId(교대역.getId())
                .downStationId(강남역.getId())
                .distance(10)
                .build();

        final LineRequest 노선_등록_요청2 = LineRequest.builder()
                .name("삼호선")
                .color("orange")
                .upStationId(교대역.getId())
                .downStationId(양재역.getId())
                .distance(5)
                .build();

        final LineRequest 노선_등록_요청3 = LineRequest.builder()
                .name("신분당선")
                .color("red")
                .upStationId(양재역.getId())
                .downStationId(강남역.getId())
                .distance(10)
                .build();

        이호선 = 지하철_노선_등록되어_있음(노선_등록_요청1).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(노선_등록_요청2).as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(노선_등록_요청3).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 5);
    }

    /**
     * Scenario: 지하철 경로를 조회
     * When 지하철 경로를 요청
     * Then 지하철 경로 등록됨
     * Then 최단 거리 경로를 응답
     * And 총 거리도 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     * <p>
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * <p>
     * 최단거리 조회
     * 강남 => 양재 : 10
     * 강남 => 교대 => 남부터미널=> 양재 : 20
     */

    @Test
    void findPath() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역, 양재역);

        // then
        지하철_경로_응답됨(response);
        PathResponse 지하철_경로 = 지하철_경로_추출(response);

        지하철_시작_종료지점이_경로에_포함됨(지하철_경로, 강남역, 양재역);
        총_거리도_함께_응답함(지하철_경로);
        지하철_이용_요금도_함께_응답함(지하철_경로);
    }
}
