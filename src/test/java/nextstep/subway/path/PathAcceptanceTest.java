package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역       --- *2호선* 10M   ---   강남역
     * |                                      |
     * *3호선*  3M                            *신분당선* 10M
     * |                                      |
     * 남부터미널역  --- *3호선* 5M    ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 남부터미널역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("지하철 최단 경로 조회")
    @Test
    void pathTest() {
        // given
        final int 예상최단거리 = 8;
        final List<StationResponse> 예상경로 = Arrays.asList(교대역, 남부터미널역, 양재역);
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);
        // then
        경로_조회_응답됨(response);
        경로_조회와_예상_경로와_일치함(response, 예상경로);
        경로_조회가_예상_거리와_일치함(response, 예상최단거리);
        지하철_이용_요금가_예상_요금과_일치함(response, 예상요금);
    }

    private void 경로_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class)).isNotNull();
    }

    private void 지하철_이용_요금가_예상_요금과_일치함(ExtractableResponse<Response> response, int expectedFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }

    private void 경로_조회와_예상_경로와_일치함(ExtractableResponse<Response> response, List<StationResponse> expectedPaths) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStations()).containsExactlyElementsOf(expectedPaths);

    }

    private void 경로_조회가_예상_거리와_일치함(ExtractableResponse<Response> response, int distance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        PathRequest pathRequest = new PathRequest(source.getId(), target.getId());
        return RestAssured
                .given().log().all()
                .body(pathRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
