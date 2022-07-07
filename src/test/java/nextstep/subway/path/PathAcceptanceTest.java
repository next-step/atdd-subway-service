package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final Long 존재하지_않는_역_아이디 = -1L;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 일호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 서울역;
    private StationResponse 삼각지역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     * 서울역 ---*1호선*--- 삼각지역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
        삼각지역 = StationAcceptanceTest.지하철역_등록되어_있음("삼각지역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-blue-600", 서울역, 삼각지역, 4);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * When 두 지하철역의 아이디로 최단 경로를 조회 하면
     * Then 최단 경로가 조회되며 그 거리를 확인할 수 있다.
     *  And 지하철 이용 요금도 함께 확인할 수 있다.
     */
    @Test
    @DisplayName("두 지하철역의 아이디로 최단 경로를 조회하면 최단 경로와 그 거리와 요금을 확인할 수 있다.")
    void 최단_경로_조회() {
        // When
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 남부터미널역.getId());

        // Then
        지하철_경로_조회됨(response);
        지하철_경로_최단거리_확인(response, 12);
        // 지하철_경로_요금_확인(response, );
    }

    /**
     * When 출발역과 도착역을 동일한 역으로 최단 경로를 조회하면
     * Then 조회할 수 없다.
     */
    @Test
    @DisplayName("출발역과 도착역을 동일한 역으로 최단 경로를 조회하면 조회할 수 없다.")
    void 출발역_도착역이_같은_경우_조회() {
        // When
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 강남역.getId());

        // Then
        지하철_경로_조회_실패됨(response);
    }

    /**
     * When 출발역과 도착역이 연결되지 않은 경우 최단 경로를 조회하면
     * Then 조회할 수 없다.
     */
    @Test
    @DisplayName("출발역과 도착역이 연결되지 않은 경우 최단 경로를 조회하면 조회할 수 없다.")
    void 출발역_도착역이_연결되지_않은_경우_조회() {
        // When
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 서울역.getId());

        // Then
        지하철_경로_조회_실패됨(response);
    }

    /**
     * When 존재하지 않은 출발역이나 도착역으로 최단 경로를 조회하면
     * Then 역을 찾을 수 없다는 NOT FOUND를 반환한다
     */
    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역으로 최단 경로를 조회하면 역을 찾을 수 없다는 NOT FOUND를 반환한다.")
    void 출발역이나_도착역이_존재하지_않는_경우_조회() {
        // When
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 존재하지_않는_역_아이디);

        // Then
        역_조회_실패됨(response);
    }

    private static ExtractableResponse<Response> 지하철_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_경로_최단거리_확인(ExtractableResponse<Response> response, int shortestDistance) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(shortestDistance);
    }

    public static void 지하철_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 역_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
