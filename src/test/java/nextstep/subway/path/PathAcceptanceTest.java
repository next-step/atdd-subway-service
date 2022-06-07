package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 왕십리역;
    private StationResponse 마장역;
    private StationResponse 창동역;

    /**
     * (*3호선*)
     * 교대역    --- 10 ------  강남역 (*2호선*)
     * |                      |
     * 3                      10
     * |                      |
     * 남부터미널역 ---  2 ------  양재 (*3호선*)
     * (*신분당선*)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        마장역 = 지하철역_등록되어_있음("마장역").as(StationResponse.class);
        창동역 = new StationResponse(99L, "창동역", null, null);

        신분당선 = 지하철_노선_등록되어_있음(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(LineRequest.of("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(LineRequest.of("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);
        오호선 = 지하철_노선_등록되어_있음(LineRequest.of("오호선", "bg-purple-600", 왕십리역.getId(), 마장역.getId(), 5))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로 조회 - 성공")
    @Test
    void 최단_경로_조회_성공() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 5);
    }

    @DisplayName("최단 경로 조회 - 실패 (출발역과 도착역이 같은 경우)")
    @Test
    void 최단_경로_조회_실패_1() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 교대역.getId());

        // Then 최단 경로 실패
        최단_경로_조회_실패(response);
    }

    @DisplayName("최단 경로 조회 - 실패 (출발역과 도착역이 연결이 되어 있지 않은 경우)")
    @Test
    void 최단_경로_조회_실패_2() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 마장역.getId());

        // Then 최단 경로 실패
        최단_경로_조회_실패(response);
    }

    @DisplayName("최단 경로 조회 - 실패 (존재하지 않은 출발역이나 도착역을 조회 할 경우)")
    @Test
    void 최단_경로_조회_실패_3() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 창동역.getId());

        // Then 최단 경로 실패
        최단_경로_조회_실패(response);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회_조회됨(ExtractableResponse<Response> response, int distance) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    public static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
