package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
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
     *              (10)
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*(3)               *신분당선* (10)
     * |                        |
     * 남부터미널역 --- *3호선* --- 양재
     *                 (2)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = this.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = this.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = this.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }


    @DisplayName("남부터미널-강남까지 최단경로는 남부터미널-양재-강남, 최단거리는 12 이다.")
    @Test
    void findShortestPathTest() {
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("source", 남부터미널역.getId())
                .queryParam("target", 강남역.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();

        //then
        PathResponse result = response.body().as(PathResponse.class);
        assertThat(result.getDistance()).isEqualTo(12);
        assertThat(result.getStations()).containsExactly(남부터미널역, 양재역, 강남역);

    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance));
        return response.body().as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse dsownStation, int distance) {
        지하철_노선에_지하철역_등록_요청(line, upStation, dsownStation, distance);
    }
}
