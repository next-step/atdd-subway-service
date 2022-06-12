package nextstep.subway.path;

import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_등록되어_있음;
import static nextstep.subway.utils.LineSectionApiHelper.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.utils.PathApiHelper.지하철_경로_조회_요청;
import static nextstep.subway.utils.PathAssertionHelper.최단경로_결과_확인;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceMockTest extends AcceptanceTest {

    @MockBean
    private PathService pathService;

    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 교대역;
    StationResponse 남부터미널역;
    LineResponse 신분당선;
    LineResponse 이호선;
    LineResponse 삼호선;

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

        신분당선 = 지하철_노선_등록되어_있음(
            new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 7)).as(
            LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 9)).as(
            LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
            new LineRequest("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 8)).as(
            LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * background given : 위와같은 지하철 도면일때 when 교대~양재역 까지의 거리 및 경로를 조회했을때 then 교대~남부터미널~양재 경로로 안내된다.
     */
    @Test
    public void 지하철_경로_조회하기() {
        //given
        when(pathService.findPath(any(Long.class), any(Long.class))).thenReturn(PathResponse.of(
            Arrays.asList(new Station("교대역"), new Station("남부터미널역"), new Station("양재역")), 21));

        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_response = 지하철_경로_조회_요청(교대역.getId(),
            양재역.getId());

        //then
        최단경로_결과_확인(지하철_경로_조회_요청_response, Arrays.asList("교대역", "남부터미널역", "양재역"), 21);
    }

}
