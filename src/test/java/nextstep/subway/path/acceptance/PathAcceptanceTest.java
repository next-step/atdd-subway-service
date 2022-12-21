package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
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

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
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

    public PathAcceptanceTest() {
    }

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


        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-300", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);

        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-yellow-420", 교대역.getId(), 강남역.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);

        LineRequest 삼호선_요청 = new LineRequest("삼호선", "bg-green-500", 교대역.getId(), 양재역.getId(), 5);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(삼호선_요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("최단 경로 조회한 경우")
    @Test
    void shortest_path() {

        ExtractableResponse<Response> 지하철_경로_조회_응답 = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", 교대역.getId())
            .param("target", 양재역.getId())
            .when().get("/paths")
            .then().log().all()
            .extract();

        assertAll(
            () -> assertThat(지하철_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getDistance()).isEqualTo(5),
            () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getStations().size()).isEqualTo(3)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 경로 조회 불가")
    @Test
    void same_start_arrive_section() {

    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 경로 조회 불가")
    @Test
    void not_connected_station_line() {

    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회하는 경우")
    @Test
    void not_exist_station() {

    }
}
