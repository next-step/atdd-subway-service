package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
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
    private StationResponse 선릉역;
    private StationResponse 양재시민의숲역;
    private StationResponse 역삼역;
    private StationResponse 삼성역;

    /**
     * *3호선*
     *  |             10                   10      10
     * 교대역 -------------------  강남역  ------역삼역-----  선릉  *2호선*  삼성역(역삼역과 연결 공사중)
     * |                           |
     * |  2                    10  |
     * |              3            |
     * 남부터미널역  --------------   양재 --*3호선*
                                10 |
                                   |
                                   |
                                 양재시민의숲역
                                *신분당선*
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        양재시민의숲역 = StationAcceptanceTest.지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 2);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 선릉역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲역, 10);
    }

    @DisplayName("최단 경로 조회")
    @Test
    void 최단_경로_조회() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(양재시민의숲역.getId(), 선릉역.getId());

        // then
        최단경로_조회됨(response);
        경로_포함됨(response, 양재시민의숲역.getId(), 양재역.getId(), 강남역.getId(), 역삼역.getId(), 선릉역.getId());
        최단경로_거리_포함됨(response, 40);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void 출발역과_도착역이_같은_경우() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 교대역.getId());

        // then
        경로_조회_실패함(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void 출발역과_도착역이_연결이_되어_있지_않은_경우() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 삼성역.getId());

        // then
        경로_조회_실패함(response);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void 존재하지_않은_출발역이나_도착역을_조회_할_경우() {
        // given
        StationResponse 아현역 = new StationResponse(20L, "아현역", now(), now());

        // when
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 아현역.getId());

        // then
        경로_조회_실패함(response);
    }

    private ExtractableResponse<Response> 최단_경로_조회(Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source=" + sourceStationId + "&target=" + targetStationId)
                .then().log().all().extract();
    }

    private void 최단경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 경로_포함됨(ExtractableResponse<Response> response, Long... expectedStationIds) {
        List<Long> resultStationIds = new ArrayList<>(response.jsonPath().getList("stations.id", Long.class));

        assertThat(resultStationIds).containsExactly(expectedStationIds);
    }

    private void 최단경로_거리_포함됨(ExtractableResponse<Response> response, int expectedDistance) {
        int resultDistance = response.jsonPath().getInt("distance");

        assertThat(resultDistance).isEqualTo(expectedDistance);
    }

    private void 경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
