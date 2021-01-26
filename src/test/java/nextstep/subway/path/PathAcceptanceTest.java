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

import java.util.Arrays;
import java.util.List;

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
     *              (30)
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*(20)               ...
     * |                        |
     * 양재   ---  *3호선*  ---  남부터미널역
     *             (10)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
    }

    @DisplayName("남부터미널-강남까지 최단경로는 남부터미널-양재-강남, 최단거리는 60, 요금은 2250원 이다.")
    @Test
    void findShortestPathTest() {
        //given
        신분당선 = this.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 100);
        이호선 = this.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 30);
        삼호선 = this.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 남부터미널역, 30);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 양재역, 남부터미널역, 10);

        //when
        ExtractableResponse<Response> response = 경로_조회_요청();

        //then
        PathResponse result = response.body().as(PathResponse.class);
        최단경로_응답함(result, Arrays.asList(남부터미널역, 양재역, 교대역, 강남역));
        총_거리_응답함(result, 60);
        지하철_이용요금_응답함(result, 2250); // 1250 + 800 + 200 = 2250
    }

    @DisplayName("남부터미널-강남까지 최단경로는 남부터미널-양재-강남, 최단거리는 60, 요금은 2750 이다.")
    @Test
    void findShortestPathWithExtraFare() {
        //given
        신분당선 = this.지하철_노선_추가요금_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 100, 1000);
        이호선 = this.지하철_노선_추가요금_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 30, 500);
        삼호선 = this.지하철_노선_추가요금_등록되어_있음("삼호선", "bg-red-600", 교대역, 남부터미널역, 30, 100);

        //when
        ExtractableResponse<Response> response = 경로_조회_요청();

        //then
        PathResponse result = response.body().as(PathResponse.class);
        최단경로_응답함(result, Arrays.asList(남부터미널역, 교대역, 강남역));
        총_거리_응답함(result, 60);
        지하철_이용요금_응답함(result, 2750); // 1250 + 800 + 200 + 500 = 2750
    }

    private LineResponse 지하철_노선_추가요금_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare));
        return response.body().as(LineResponse.class);
    }

    private void 지하철_이용요금_응답함(PathResponse response, int fare) {
        assertThat(response.getFare()).isEqualTo(fare);
    }

    private void 총_거리_응답함(PathResponse response, int distance) {
        assertThat(response.getDistance()).isEqualTo(distance);
    }

    private void 최단경로_응답함(PathResponse response, List<StationResponse> stations) {
        assertThat(response.getStations()).isEqualTo(stations);
    }

    private ExtractableResponse<Response> 경로_조회_요청() {
        return RestAssured
                .given().log().all()
                .queryParam("source", 남부터미널역.getId())
                .queryParam("target", 강남역.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, 0));
        return response.body().as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse dsownStation, int distance) {
        지하철_노선에_지하철역_등록_요청(line, upStation, dsownStation, distance);
    }
}
