package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.acceptance.LineAcceptanceFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceFixture.지하철_최단_경로_요청;
import static nextstep.subway.station.StationAcceptanceFixture.지하철역_등록되어_있음;
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
    private StationResponse 구로디지털단지역;

    /**
     * 교대역      --- *2호선(50)* ---   강남역
     * |                              |
     * *3호선(15)*                      *신분당선(50)*
     * |                              |
     * 남부터미널역  --- *3호선(10)*  ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        구로디지털단지역 = 지하철역_등록되어_있음("구로디지털단지역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-300", 강남역.getId(), 양재역.getId(), 50, 1000)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-yellow-420", 교대역.getId(), 강남역.getId(), 50, 200)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-green-500", 교대역.getId(), 양재역.getId(), 25, 300)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 15);
    }

    @DisplayName("최단 경로 조회한 경우")
    @Test
    void shortest_path() {
        // when
        ExtractableResponse<Response> 지하철_경로_조회_응답 = 지하철_최단_경로_요청(교대역.getId(), 양재역.getId());

        // then
        assertAll(
                () -> assertThat(지하철_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getDistance()).isEqualTo(25),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getStations().size()).isEqualTo(3),
                () -> assertThat(지하철_경로_조회_응답.as(PathResponse.class).getFare()).isEqualTo(1850)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 경로 조회 불가")
    @Test
    void same_start_arrive_section() {
        // when
        ExtractableResponse<Response> 출발역과_도착역이_같은_지하철_최단_경로_응답 = 지하철_최단_경로_요청(교대역.getId(), 교대역.getId());

        // then
        assertThat(출발역과_도착역이_같은_지하철_최단_경로_응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 경로 조회 불가")
    @Test
    void not_connected_station_line() {
        // when
        ExtractableResponse<Response> 연결되지_않는_지하철_최단_경로_응답 = 지하철_최단_경로_요청(강남역.getId(), 구로디지털단지역.getId());

        // then
        assertThat(연결되지_않는_지하철_최단_경로_응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회하는 경우")
    @Test
    void not_exist_station() {
        // when
        ExtractableResponse<Response> 존재하지_않는_도착역_조회_응답 = 지하철_최단_경로_요청(강남역.getId(), 10L);

        // then
        assertThat(존재하지_않는_도착역_조회_응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
