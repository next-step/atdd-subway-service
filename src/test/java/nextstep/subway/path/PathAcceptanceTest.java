package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.ui.LineControllerTest;
import nextstep.subway.line.ui.SectionControllerTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


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
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

    }

    /**
    * When 출발역 강남역 -> 남부터미널역 최단거리 조회를 한다.
    * Then 거리는 12, 역은 강남역 -> 양재 -> 남부터미널로 조회된다.
    */
    @Test
    @DisplayName("최단거리 조회 성공")
    void pathFind() {
        // when
        final ExtractableResponse<Response> 지하철역_최단_거리_조회 = 지하철역_최단_거리_조회(강남역.getId(), 남부터미널역.getId());

        // then
        assertAll(
            () -> assertThat(지하철역_최단_거리_조회.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(지하철역_최단_거리_조회.jsonPath().getInt("distance")).isEqualTo(12),
            () -> assertThat(지하철역_최단_거리_조회.jsonPath().getList("stations.name")).containsExactly(강남역.getName(), 양재역.getName(), 남부터미널역.getName())
        );

    }

    @Test
    @DisplayName("출발역과 도착역이 같을때")
    void pathFind_exception_same_station() {
        // when
        final ExtractableResponse<Response> 지하철역_최단_거리_조회 = 지하철역_최단_거리_조회(강남역.getId(), 강남역.getId());

        // then
        assertThat(지하철역_최단_거리_조회.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되지 않을때")
    void pathFind_exception_not_linked() {
        // given
        final StationResponse 당고개역 = StationAcceptanceTest.지하철역_등록되어_있음("당고개역").as(StationResponse.class);
        final StationResponse 남태령역 = StationAcceptanceTest.지하철역_등록되어_있음("남태령역").as(StationResponse.class);
        지하철_노선_등록되어_있음("사호선", "bg-red-600", 당고개역, 남태령역, 10);

        // when
        final ExtractableResponse<Response> 지하철역_최단_거리_조회 = 지하철역_최단_거리_조회(강남역.getId(), 남태령역.getId());

        // then
        assertThat(지하철역_최단_거리_조회.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("출발역과 도착역이 존재하지 않을때")
    void pathFind_exception_not_found() {
        // given
        final StationResponse 당고개역 = StationAcceptanceTest.지하철역_등록되어_있음("당고개역").as(StationResponse.class);

        // when
        final ExtractableResponse<Response> 지하철역_최단_거리_조회 = 지하철역_최단_거리_조회(강남역.getId(), 당고개역.getId());

        // then
        assertThat(지하철역_최단_거리_조회.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철역_최단_거리_조회(Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .param("source", sourceId)
                .param("target", targetId)
                .when().get("/paths")

                .then().log().all()
                .extract();
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation,
                                      int distance) {
        SectionControllerTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
        final ExtractableResponse<Response> 지하철_노선_조회_요청 = LineControllerTest.지하철_노선_조회_요청(line);
        SectionControllerTest.지하철_노선에_지하철역_등록됨(지하철_노선_조회_요청);
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
                                        StationResponse downStation, int distance) {
        return LineControllerTest.지하철_노선_등록되어_있음(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance))
                .as(LineResponse.class);
    }


    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 두 역의 최단 거리 경로를 조회
     *     Given 지하철역이 등록되어있음
     *     And 지하철 노선이 등록되어있음
     *     And 지하철 노선에 지하철역이 등록되어있음
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리도 함께 응답함
     *     And ** 지하철 이용 요금도 함께 응답함 **
    */
    @Test
    void findPathPrice() {
        // when
        final ExtractableResponse<Response> 지하철역_최단_거리_조회 = 지하철역_최단_거리_조회(강남역.getId(), 남부터미널역.getId());

        // then
        assertAll(
                () -> assertThat(지하철역_최단_거리_조회.jsonPath().getInt("distance")).isEqualTo(12),
                () -> assertThat(지하철역_최단_거리_조회.jsonPath().getList("stations.name")).containsExactly(강남역.getName(), 양재역.getName(), 남부터미널역.getName()),
                () -> assertThat(지하철역_최단_거리_조회.jsonPath().getInt("price")).isEqualTo(1350)
        );
    }
}
