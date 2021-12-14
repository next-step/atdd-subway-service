package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

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

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.of("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    void 최단_경로_조회() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_요청_응답 = 최단_경로_조회_요청(강남역, 남부터미널역);

        // then
        최단_경로_조회_요청_응답됨(최단_경로_조회_요청_응답);
        최단_경로_조회_경로_포함됨(최단_경로_조회_요청_응답, Arrays.asList(강남역, 양재역, 남부터미널역));
        최단_경로_조회_거리_포함됨(최단_경로_조회_요청_응답, 12);
    }

    @Test
    void 최단_경로_조회_출발역과_도착역이_같은_경우_조회할_수_없다() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_요청_응답 = 최단_경로_조회_요청(강남역, 강남역);

        // then
        최단_경로_조회_요청_실패됨(최단_경로_조회_요청_응답, "출발역과 도착역이 같은 경우 최단 거리를 구할 수 없습니다.");
    }

    @Test
    void 최단_경로_조회_출발역과_도착역이_연결되어있지_않은_경우_조회할_수_없다() {
        // given
        StationResponse 동암역 = StationAcceptanceTest.지하철역_등록되어_있음("동암역").as(StationResponse.class);
        StationResponse 송내역 = StationAcceptanceTest.지하철역_등록되어_있음("송내역").as(StationResponse.class);
        LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.of("일호선", "bg-red-600", 동암역.getId(), 송내역.getId(), 5)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> 최단_경로_조회_요청_응답 = 최단_경로_조회_요청(강남역, 송내역);

        // then
        최단_경로_조회_요청_실패됨(최단_경로_조회_요청_응답, "출발역과 도착역이 연결되어있지 않은 경우 조회할 수 없습니다.");
    }

    private void 최단_경로_조회_경로_포함됨(ExtractableResponse<Response> response, List<StationResponse> expectedResponse) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> resultLineIds = pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = expectedResponse.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private void 최단_경로_조회_거리_포함됨(ExtractableResponse<Response> response, int expectedDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        HashMap<String, Long> parametersMap = new HashMap<>();
        parametersMap.put("source", source.getId());
        parametersMap.put("target", target.getId());
        return 조회_요청(PATH_ROOT_PATH, parametersMap);
    }

    private void 최단_경로_조회_요청_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로_조회_요청_실패됨(ExtractableResponse<Response> response, String errorMessage) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.response().body().asString()).isEqualTo(errorMessage);
    }
}
