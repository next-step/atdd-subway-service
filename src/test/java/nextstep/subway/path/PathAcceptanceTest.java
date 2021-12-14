package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RequestUtil;
import nextstep.subway.utils.StatusCodeCheckUtil;

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
     * 남부터미널역 --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "red", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "green", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "orange", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    private StationResponse 지하철역_등록되어_있음(String name) {
        return StationAcceptanceTest.지하철역_생성_요청(name).as(StationResponse.class);
    }

    private LineResponse 지하철_노선_등록되어_있음(
        String name, String color, StationResponse upStation, StationResponse downStation, int distance
    ) {
        final LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(
        LineResponse line, StationResponse upStation, StationResponse downStation, int distance
    ) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    @Test
    void 최단_경로_조회() {
        // when
        final ExtractableResponse<Response> response = 최단_경로_조회_요청(남부터미널역, 강남역);

        // then
        최단_경로_응답됨(response);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(final StationResponse 남부터미널역, final StationResponse 강남역) {
        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("source", 남부터미널역.getId());
        queryParams.put("target", 강남역.getId());

        return RequestUtil.get("/paths", queryParams);
    }

    private void 최단_경로_응답됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);

        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        assertThat(stations).containsExactly(남부터미널역, 양재역, 강남역);

        final int distance = response.jsonPath().getObject("distance", Integer.class);
        assertThat(distance).isEqualTo(12);
    }
}
