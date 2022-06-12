package nextstep.subway.path;

import static nextstep.subway.utils.PathApiHelper.지하철_경로_조회_요청;
import static nextstep.subway.utils.PathAssertionHelper.최단경로_결과_확인;
import static org.mockito.Mockito.when;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceMockTest extends AcceptanceTest {

    @MockBean
    private LineService lineService;

    @MockBean
    private StationService stationService;

    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;


    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 7);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 9);
        삼호선 = new Line("삼호선", "bg-red-600", 남부터미널역, 양재역, 8);
        삼호선.addStation(교대역, 남부터미널역, 3);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * background
        * given : 위와같은 지하철 도면일때
     * when 교대~양재역 까지의 거리 및 경로를 조회했을때
     * then 교대~남부터미널~양재 경로로 안내된다.
     */
    @Test
    public void 지하철_경로_조회하기() {
        //given
        when(lineService.findAllLines()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));
        when(stationService.findById(1L)).thenReturn(교대역);
        when(stationService.findById(3L)).thenReturn(양재역);

        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_response = 지하철_경로_조회_요청(1L, 3L);

        //then
        최단경로_결과_확인(지하철_경로_조회_요청_response, Arrays.asList("교대역", "남부터미널역", "양재역"), 11);
    }

}
