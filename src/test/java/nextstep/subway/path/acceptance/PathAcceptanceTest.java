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

import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.step.LineSectionStep.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.step.PathStep.최단_경로_조회_요청;
import static nextstep.subway.path.step.PathStep.최단_경로_조회됨;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 선릉역;

    /**
     * 교대역    --- *2호선* --- >  강남역
     * |            (10)         |
     * *3호선* (10)   (25)         *신분당선* (10)
     * |                        |
     * v                       v
     * 선릉역  --- *3호선* --->  양재
     *            (15)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역, 양재역, 10, 900));
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역, 강남역, 10, 500));
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역, 양재역, 25, 700));

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 선릉역, 10);
    }

    @Test
    void shortestPath_최단_경로를_조회한다() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);

        // then
        최단_경로_조회됨(response, 20, Arrays.asList(교대역, 강남역, 양재역));
        요금_조회됨(response, 2150);
    }

    private void 요금_조회됨(ExtractableResponse<Response> response, int fare) {
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare);
    }

    private static LineResponse 지하철_노선_등록되어_있음(LineRequest params) {
        return 지하철_노선_생성_요청(params).as(LineResponse.class);
    }

    private static void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }
}
