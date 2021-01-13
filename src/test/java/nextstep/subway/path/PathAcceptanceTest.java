package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.fares.domain.Fare;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 수인분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 선정릉역;
    private StationResponse 선릉역;
    private StationResponse 역삼역;
    private StationResponse 양재시민의숲역;
    private StationResponse 청계산입구역;

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
        선정릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선정릉역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        양재시민의숲역 = StationAcceptanceTest.지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);
        청계산입구역 = StationAcceptanceTest.지하철역_등록되어_있음("청계산입구역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
            .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 900))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
            .as(LineResponse.class);
        수인분당선 = 지하철_노선_등록되어_있음(new LineRequest("수인분당선", "bg-red-600", 선정릉역.getId(), 선릉역.getId(), 5))
            .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3, 500);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲역, 10, 1000);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재시민의숲역, 청계산입구역, 50, 0);
    }

    @DisplayName("10km 이내의 최단 경로를 조회한다")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);

        // then
        최단_경로_조회됨(response, Arrays.asList(교대역, 남부터미널역, 양재역), 5);
        지하철_이용_요금_응답함(response, 1250 + 500);
    }

    @DisplayName("10km ~ 50km 사이의 지하철 경로를 검색한다")
    @Test
    void findShortestPathWithBetween10kmAnd50km() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재시민의숲역);

        // then
        최단_경로_조회됨(response, Arrays.asList(교대역, 남부터미널역, 양재역, 양재시민의숲역), 15);
        지하철_이용_요금_응답함(response, 1250 + 100 + 1000);
    }

    @DisplayName("50km 초과 경로의 지하철 경로를 검색한다")
    @Test
    void findShortestPathWithOver50km() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 청계산입구역);

        // then
        최단_경로_조회됨(response, Arrays.asList(교대역, 남부터미널역, 양재역, 양재시민의숲역, 청계산입구역), 65);
        지하철_이용_요금_응답함(response, 1250 + 800 + 100 + 1000);
    }

    @DisplayName("추가 요금이 있는 노선이 존재하는 지하철 경로를 검색한다")
    @Test
    void findShortestPathWithExtraCharge() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 강남역);

        // then
        최단_경로_조회됨(response, Arrays.asList(교대역, 강남역), 10);
        지하철_이용_요금_응답함(response, 1250 + 900);
    }

    @DisplayName("로그인 하지 않은 청소년은 요금 할인이 적용안된다")
    @Test
    void findShortestPathWithAnonymous() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(양재역, 양재시민의숲역);

        // then
        최단_경로_조회됨(response, Arrays.asList(양재역, 양재시민의숲역), 10);
        지하철_이용_요금_응답함(response, 1250 + 1000);
    }

    @DisplayName("로그인한 청소년은 할인이 적용된다")
    @Test
    void findShortestPathWithExtraChargeMax() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(양재역, 양재시민의숲역);

        // then
        최단_경로_조회됨(response, Arrays.asList(양재역, 양재시민의숲역), 10);
        지하철_이용_요금_응답함(response, 1250 + 100 + 1000);
    }

    @DisplayName("출발역과 도착역을 동일하게 하여 최단 경로를 조회한다")
    @Test
    void findShortestPathWithSameStation() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 교대역);

        // then
        최단_경로_조회_실패됨(response);
    }

    @DisplayName("연결되어 있지 않은 출발역과 도착역의 최단 경로를 조회한다")
    @Test
    void findShortestPathWithNotConnected() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(선정릉역, 양재역);

        // then
        최단_경로_조회_실패됨(response);
    }

    @DisplayName("존재하지 않는 역으로 최단 경로를 조회한다")
    @Test
    void findShortestPathWithNoStation() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(역삼역, 교대역);

        // then
        최단_경로_조회_실패됨(response);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStation,
                                                            StationResponse targetStation) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceStation.getId())
                .queryParam("target", targetStation.getId())
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response,
                                 List<StationResponse> expectedStations, int distance) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    private void 지하철_이용_요금_응답함(ExtractableResponse<Response> response, int fares) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fares);
    }

    public static void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
