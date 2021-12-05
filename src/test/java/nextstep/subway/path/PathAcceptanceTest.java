package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.AcceptanceTestUtil.get;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    void 최단_경로_조회() {
        // when
        PathResponse 교대역_양재역_경로 = 최단_경로_조회(교대역, 양재역);

        // then
        거리가_5인_교대역_남부터미널역_양재역_경로_응답(교대역_양재역_경로);

    }

    private void 거리가_5인_교대역_남부터미널역_양재역_경로_응답(PathResponse 교대역_양재역_경로) {
        assertThat(교대역_양재역_경로.getDistance()).isEqualTo(5);
        assertThat(교대역_양재역_경로.getStations()).extracting(StationResponse::getName)
            .containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName());
    }

    private PathResponse 최단_경로_조회(StationResponse 출발역, StationResponse 도착역) {
        return get("/paths?source=" + 출발역.getId() + "&target=" + 도착역.getId())
            .as(PathResponse.class);
    }

}
