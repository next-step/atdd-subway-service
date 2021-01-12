package nextstep.subway.path;

import io.restassured.RestAssured;
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
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 인천역;

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
        인천역 = StationAcceptanceTest.지하철역_등록되어_있음("인천역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 1000L);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-200", 교대역.getId(), 강남역.getId(), 1000L);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-yellow-300", 교대역.getId(), 양재역.getId(), 500L);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 300L);
    }

    @DisplayName("`교대역`에서 `양재역`까지 최단경로를 조회")
    @Test
    void findShortPathTest() {
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(교대역, 양재역);
        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getStations()).hasSize(3),
                () -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(500L)
        );
    }

    @DisplayName("예외 상황 - 출발역과 도착역이 같은 경우")
    @Test
    void exceptionToSearchPathOfSameStation() {
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(교대역, 교대역);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_노선_경로탐색_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("sourceId", source.getId())
                .pathParam("targetId", target.getId())
                .when().get("/paths?source={sourceId}&target={targetId}")
                .then().log().all().extract();
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, long distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    private LineResponse 지하철_노선_등록되어_있음(String line, String color, Long upStationId, Long downStationId, long distance) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(line, color, upStationId, downStationId, distance))
                .as(LineResponse.class);
    }
}
