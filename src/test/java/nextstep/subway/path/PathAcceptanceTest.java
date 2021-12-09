package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.AcceptanceTestUtil.get;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


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
    @DisplayName("최단 경로 조회")
    void 최단_경로_조회() {
        // when
        PathResponse 교대역_양재역_경로 = 최단_경로_조회(교대역, 양재역).as(PathResponse.class);;

        // then
        거리가_5인_교대역_남부터미널역_양재역_경로_응답(교대역_양재역_경로);
        총_거리_포함됨(교대역_양재역_경로);
        지하철_이용_요금_포함(교대역_양재역_경로);

    }


    @Test
    @DisplayName("출발지와 도착지가 같은 경우 예외 발생")
    void 경로조회_출발지와_도착지가_같은_경우_예외() {
        // when
        ExtractableResponse<Response> 출발지_도착지_같은_경우_응답 = 최단_경로_조회(교대역, 교대역);

        // then
        경로_조회_실패(출발지_도착지_같은_경우_응답);
    }

    private void 경로_조회_실패(ExtractableResponse<Response> 출발지_도착지_같은_경우_응답) {
        assertThat(출발지_도착지_같은_경우_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(출발지_도착지_같은_경우_응답.body().asString()).isEqualTo("출발역과 도착역이 같습니다.");
    }

    private void 거리가_5인_교대역_남부터미널역_양재역_경로_응답(PathResponse 교대역_양재역_경로) {
        assertThat(교대역_양재역_경로.getDistance()).isEqualTo(5);
        assertThat(교대역_양재역_경로.getStations()).extracting(StationResponse::getName)
            .containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName());
    }

    private ExtractableResponse<Response> 최단_경로_조회(StationResponse 출발역, StationResponse 도착역) {
        return get("/paths?source=" + 출발역.getId() + "&target=" + 도착역.getId());
    }

    private void 총_거리_포함됨(PathResponse 교대역_양재역_경로) {
        assertThat(교대역_양재역_경로.getDistance()).isNotNull();
    }

    private void 지하철_이용_요금_포함(PathResponse 교대역_양재역_경로) {
        assertThat(교대역_양재역_경로.getPay()).isNotNull();
    }

}
