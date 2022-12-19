package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 최단 경로 조회 인수 테스트")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     *
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Feature: 지하철 최단 경로 조회 기능
     * <p>
     * Background
     * Given 지하철역 등록되어 있음
     * AND 지하철 노선 등록되어 있음
     * AND 지하철 노선에 지하철 역 등록되어 있음
     * <p>
     * Scenario: Happy Case
     * WHEN 지하철 출발역에서 도착역 최단경로 조회
     * THEN 지하철 출발역에서 도착역 최단경로 목록 조회됨
     */
    @DisplayName("지하철 출발역에서 도착역 최단경로 조회 Happy 케이스 시나리오 테스트")
    @Test
    void getShortestPathHappyCaseScenario() {
        ExtractableResponse<Response> response = getShortestPath(강남역, 남부터미널역);
        assertOkStatus(response);
        assertShortestPathOrderAndDistance(response, Arrays.asList(강남역, 양재역, 남부터미널역), 12);
    }

    private ExtractableResponse<Response> getShortestPath(StationResponse source, StationResponse target) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", source.getId())
            .param("target", target.getId())
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    private static void assertOkStatus(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertShortestPathOrderAndDistance(
        ExtractableResponse<Response> response,
        List<StationResponse> expectedStations,
        int expectedTotalDistance
    ) {
        PathResponse path = response.as(PathResponse.class);

        List<Long> actualStationIds = path.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(actualStationIds).containsExactlyElementsOf(expectedStationIds),
            () -> assertThat(path.getDistance()).isEqualTo(expectedTotalDistance)
        );
    }

}

