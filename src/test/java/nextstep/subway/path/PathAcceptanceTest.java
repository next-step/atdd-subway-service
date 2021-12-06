package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

import nextstep.subway.api.HttpMethod;
import nextstep.subway.path.dto.PathResponse;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;

import io.restassured.*;
import io.restassured.response.*;
import nextstep.subway.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;
import nextstep.subway.station.dto.*;

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

        신분당선 = 지하철_노선_등록되어_있음(
            LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
            LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
            LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 5))
            .as(LineResponse.class);

        지하철_노선에_지하철_구간_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Autowired
    public StationRepository stationRepository;

    @DisplayName("지하철 최단 경로를 조회한다.")
    @Test
    void shortestPathTest() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 남부터미널역.getId());

        // then
        정답_응답_확인(response);
        최단_경로_거리_확인(response);
    }

    private static ExtractableResponse<Response> 최단_경로_조회(Long source, Long target) {
        return HttpMethod.get(
            "/paths?source={source}&target={target}",
            source,
            target
        );
    }

    private void 정답_응답_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로_거리_확인(ExtractableResponse<Response> response) {
        assertThat(response.as(PathResponse.class).getTotalDistance()).isEqualTo(13);
    }
}
