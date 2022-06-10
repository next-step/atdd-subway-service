package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 사당역;
    private StationResponse 범계역;

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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        범계역 = StationAcceptanceTest.지하철역_등록되어_있음("범계역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로 구하기")
    @Test
    public void findPath() {
        //when
        ExtractableResponse<Response> response = 최단경로_요청(교대역.getId(), 양재역.getId());
        //then
        최단경로_응답_확인(response,5, "교대역", "남부터미널역", "양재역");
    }
    @DisplayName("같은 역일 경우, BadRequest")
    @Test
    public void findPath_fail_sameStation() {
        //when
        ExtractableResponse<Response> response = 최단경로_요청(교대역.getId(), 교대역.getId());
        //then
        최단경로_응답_BadRequest(response);
    }

    @DisplayName("역이 연결되지 않았을 경우, BadRequest")
    @Test
    public void findPath_fail_stationNotConnected() {
        //given
        LineResponse 사호선 = 지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-red-500", 사당역.getId(), 범계역.getId(), 5)).as(LineResponse.class);
        //when
        ExtractableResponse<Response> response = 최단경로_요청(사당역.getId(), 교대역.getId());
        //then
        최단경로_응답_BadRequest(response);
    }

    @DisplayName("역이 없는 경우, NotFound")
    @Test
    public void findPath_fail_stationNotFound() {
        //when
        ExtractableResponse<Response> response = 최단경로_요청(사당역.getId(), 범계역.getId());
        //then
        최단경로_응답_NotFound(response);
    }

    private ExtractableResponse<Response> 최단경로_요청(Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceId)
                .param("target", targetId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 최단경로_응답_확인(ExtractableResponse<Response> response, int expectedDistance, String... strings) {
        List<StationResponse> stations = response.body().jsonPath().getList("stations", StationResponse.class);
        List<String> stationNames = stations.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        int distance = response.body().jsonPath().getInt("distance");
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.statusCode()),
                () -> assertThat(stationNames).containsExactly(strings),
                () -> assertEquals(expectedDistance, distance)
        );
    }

    private void 최단경로_응답_BadRequest(ExtractableResponse<Response> response) {
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    private void 최단경로_응답_NotFound(ExtractableResponse<Response> response) {
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }
}
