package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.ShortestPathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
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
     * 교대역    --- *2호선(50)* ---   강남역
     * |                        |
     * *3호선*(38)                   *신분당선(50)*
     * |                        |
     * 남부터미널역  --- *3호선*(60) ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 50);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 50);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 98);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 38);
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    public void findShortestPath() throws Exception {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);

        // then
        최단_경로_조회_성공함(response);
        최단_경로와_총_거리가_응답됨(response, 98, 2_650, 교대역.getName(), 남부터미널역.getName(), 양재역.getName());
    }

    private void 최단_경로_조회_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 최단_경로와_총_거리가_응답됨(ExtractableResponse<Response> response, int distance, int price, String... stationNames) {
        ShortestPathResponse shortestPathResponse = response.as(ShortestPathResponse.class);
        List<String> stations = shortestPathResponse.getStations()
                .stream()
                .map(s -> s.getName())
                .collect(Collectors.toList());

        assertThat(stations).containsExactly(stationNames);
        assertThat(shortestPathResponse.getDistance()).isEqualTo(distance);
        assertThat(shortestPathResponse.getPrice()).isEqualTo(price);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
        Map<String, Long> param = new HashMap<>();
        param.put("source", sourceStation.getId());
        param.put("target", targetStation.getId());

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(param)
                .when().get("/paths")
                .then().log().all()
                .extract();
        return response;
    }
}
