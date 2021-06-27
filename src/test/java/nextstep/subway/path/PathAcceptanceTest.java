package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단경로 조회")
    @Test
    void paths() {
        // when
        ExtractableResponse<Response> response = 최단경로_조회_요청(양재역, 교대역);

        // then
        최단경로_조회됨(response);
        최단경로_일치(response, Arrays.asList(양재역, 남부터미널역, 교대역));
    }

    private ExtractableResponse<Response> 최단경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    private void 최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단경로_일치(ExtractableResponse<Response> response, List<StationResponse> expectedList) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<String> actual = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        List<String> expected = expectedList.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(actual).isEqualTo(expected);
    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String lineColor, Long upStationId, Long downStationId, int distance) {
        LineRequest lineRequest = new LineRequest(lineName, lineColor, upStationId, downStationId, distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }
}
