package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


@DisplayName("지하철 경로 조회")
@ExtendWith(MockitoExtension.class)
public class PathAcceptanceTest extends AcceptanceTest {

    public static final String path = "/paths";

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
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineRequest 이호선_등록_요청 = new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10);
        이호선 = 지하철_노선_등록되어_있음(이호선_등록_요청).as(LineResponse.class);
        LineRequest 신분당선_등록_요청 = new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(신분당선_등록_요청).as(LineResponse.class);
        LineRequest 삼호선_등록_요청 = new LineRequest("삼호선", "orange", 교대역.getId(), 양재역.getId(), 5);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_등록_요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    void 최단_경로_조회() {
        //when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then
        응답결과_확인(response, HttpStatus.OK);
        List<StationResponse> stations = Arrays.asList(교대역, 남부터미널역, 양재역);
        경유지_확인(response, stations);
        경유거리_확인(response, 5);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceStationId, Long targetStationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);
        return get(path, params);
    }

    private void 경유거리_확인(ExtractableResponse<Response> response, int expectedDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    private void 경유지_확인(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<StationResponse> actualStations = pathResponse.getStations();
        assertThat(actualStations).isEqualTo(expectedStations);
    }
}
