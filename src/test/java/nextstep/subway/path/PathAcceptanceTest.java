package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineRestHelper;
import nextstep.subway.line.acceptance.LineSectionRestHelper;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationRestHelper;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


@SuppressWarnings("NonAsciiCharacters")
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
     * *3호선*                   *신분당선*
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationRestHelper.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationRestHelper.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationRestHelper.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationRestHelper.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineRestHelper.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = LineRestHelper.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = LineRestHelper.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        ExtractableResponse<Response> response = LineSectionRestHelper
                .지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * 사당역
     *
     * 교대역    --- *2호선* ---   강남역
     * *3호선*                   *신분당선*
     * 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("등록되지 않은 역 경로 조회")
    @Test
    public void notRegisteredStation() {
        ExtractableResponse<Response> response = PathRestHelper
                .지하철_경로_탐색_요청(1000L, 강남역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * 남성역    --- *7호선* ---   이수역
     *
     * 교대역    --- *2호선* ---   강남역
     * *3호선*                   *신분당선*
     * 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("연결되지 않은 역 경로 조회")
    @Test
    public void notConnectedLineTest() {
        StationResponse 남성역 = StationRestHelper.지하철역_등록되어_있음("남성역").as(StationResponse.class);
        StationResponse 이수역 = StationRestHelper.지하철역_등록되어_있음("이수역").as(StationResponse.class);
        LineRestHelper.지하철_노선_등록되어_있음("칠호선", "bg-red-600", 남성역, 이수역, 10);

        ExtractableResponse<Response> response = PathRestHelper
                .지하철_경로_탐색_요청(남성역.getId(), 강남역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    /**
     * 교대역    --- *2호선* ---   강남역
     * *3호선*                   *신분당선*
     * 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("지하철 역 경로 조회")
    @Test
    public void pathsTest() {
        StationResponse 남성역 = StationRestHelper.지하철역_등록되어_있음("남성역").as(StationResponse.class);
        StationResponse 이수역 = StationRestHelper.지하철역_등록되어_있음("이수역").as(StationResponse.class);
        LineRestHelper.지하철_노선_등록되어_있음("칠호선", "bg-red-600", 남성역, 이수역, 10);

        PathResponse pathResponse = PathRestHelper.지하철_경로_탐색_요청(교대역.getId(), 양재역.getId())
                .as(PathResponse.class);

        List<StationResponse> stations = pathResponse.getStations();
        assertThat(stations.get(0).getName()).isEqualTo("교대역");
        assertThat(stations.get(1).getName()).isEqualTo("남부터미널역");
        assertThat(stations.get(2).getName()).isEqualTo("양재역");
        assertThat(pathResponse.getDistance()).isEqualTo(5);

    }

    @DisplayName("같은 역 경로 조회")
    @Test
    public void requestSameStation() {
        ExtractableResponse<Response> response = PathRestHelper.지하철_경로_탐색_요청(교대역.getId(), 교대역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
