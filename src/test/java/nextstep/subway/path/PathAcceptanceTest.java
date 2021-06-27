package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 나홀로선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 독립역;
    private StationResponse 혼자역;
    private StationResponse 유령역;

    /**
     * 교대역    --- *2호선*(10) --- 강남역
     * |                        |
     * *3호선*(3)                *신분당선*(10)
     * |                        |
     * 남부터미널역--- *3호선*(2) --- 양재
     * 독립역 --- *나홀로선*(10) --- 혼자역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        독립역 = 지하철역_등록되어_있음("독립역").as(StationResponse.class);
        혼자역 = 지하철역_등록되어_있음("혼자역").as(StationResponse.class);
        유령역 = new StationResponse(9999l, "유령역", LocalDateTime.now(), LocalDateTime.now());

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        나홀로선 = 지하철_노선_등록되어_있음(new LineRequest("나홀로선", "bg-red-600", 독립역.getId(), 혼자역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    void 지하철_노선_최단_경로_조회_테스트() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_최단_경로_조회_요청(남부터미널역.getId(), 강남역.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        지하철_노선_최단_경로에_해당_역들이_포함되어있음(response, Arrays.asList(남부터미널역, 양재역, 강남역));
        지하철_노선_최단_경로의_거리를_확인한다(response, 12);
    }

    @Test
    void 지하철_노선_조회시_출발역과_도착역이_같은_경우_테스트() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_최단_경로_조회_요청(남부터미널역.getId(), 남부터미널역.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 지하철_노선_조회시_출발역과_도착역이_연결_되어_있지_않은_경우_테스트() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_최단_경로_조회_요청(남부터미널역.getId(), 혼자역.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 지하철_노선_조회시_존재하지_않은_출발역을_조회할_경우_테스트() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_최단_경로_조회_요청(유령역.getId(), 혼자역.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 지하철_노선_조회시_존재하지_않은_도착역을_조회할_경우_테스트() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_최단_경로_조회_요청(남부터미널역.getId(), 유령역.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_최단_경로_조회_요청(final Long source, final Long target) {
        // when
        return RestAssured
            .given().log().all()
            .when().get("/paths?source={sourceStationId}&target={targetStationId}", source, target)
            .then().log().all().extract();
    }

    public static void 지하철_노선_최단_경로에_해당_역들이_포함되어있음(ExtractableResponse<Response> response, final List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> actualStationIds = pathResponse.getStations()
                                                  .stream()
                                                  .map(Station::getId)
                                                  .collect(Collectors.toList());

        List<Long> expectStationsIds = expectedStations.stream()
                                                       .map(StationResponse::getId)
                                                       .collect(Collectors.toList());

        assertThat(actualStationIds)
            .isNotEmpty()
            .containsExactlyElementsOf(expectStationsIds);
    }

    public static void 지하철_노선_최단_경로의_거리를_확인한다(ExtractableResponse<Response> response, final int distance) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }
}
