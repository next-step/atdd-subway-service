package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTestFixture.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 공항선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 김포공항역;
    private StationResponse 마곡나루역;

    /**
     * 김포공항    --- *공항선* --- 마곡나루
     * 교대역    --- *2호선* ---   강남역
     * |                            |
     * *3호선*                   *신분당선*
     * |                           |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        김포공항역 = 지하철역_등록되어_있음("김포공항역").as(StationResponse.class);
        마곡나루역 = 지하철역_등록되어_있음("마곡나루역").as(StationResponse.class);

        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 5);
        신분당선 = 지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);
        이호선 = 지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);
        LineRequest 삼호선_요청 = new LineRequest("삼호선", "bg-blue-600", 교대역.getId(), 양재역.getId(), 20);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_요청).as(LineResponse.class);
        LineRequest 공항선_요청 = new LineRequest("공항선", "bg-purple-600", 김포공항역.getId(), 마곡나루역.getId(), 30);
        공항선 = 지하철_노선_등록되어_있음(공항선_요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 15);
    }

    /**
     *  Given 지하철역과 지하철 노선을 생성하고
     *  When 출발역과 도착역 사이의 최단 경로를 조회하면
     *  Then 출발역과 도착역 사이의 최단 경로가 조회된다.
     */
    @DisplayName("출발역과 도착역 사이의 최단 경로를 조회한다.")
    @Test
    void findShortestPath() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(남부터미널역, 강남역);

        최단_경로_조회됨(response, 10, Arrays.asList(남부터미널역, 양재역, 강남역));
    }

    /**
     *  Given 지하철역과 지하철 노선을 생성하고
     *  When 동일한 출발역과 도착역으로 최단 경로를 조회하면
     *  Then 최단 경로 조회에 실패한다.
     */
    @DisplayName("출발역과 도착역이 같을 경우 최단 경로를 조회할 수 없다.")
    @Test
    void findShortestPathWithSameStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 강남역);

        최단_경로_조회_실패됨_400(response);
    }

    /**
     *  Given 지하철역과 지하철 노선을 생성하고
     *  When 연결되어 있지 않은 출발역과 도착역의 최단 경로를 조회하면
     *  Then 최단 경로 조회에 실패한다.
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 최단 경로를 조회할 수 없다.")
    @Test
    void findShortestPathWithNotConnectStation() {
        ExtractableResponse<Response> response = 최단_경로_조회_요청(김포공항역, 강남역);

        최단_경로_조회_실패됨_400(response);
    }

    /**
     *  Given 지하철역과 지하철 노선을 생성하고
     *  When 존재하지 않는 출발역이나 도착역으로 최단 경로를 조회하면
     *  Then 최단 경로 조회에 실패한다.
     */
    @DisplayName("존재하지 않는 출발역이나 도착역으로 최단 경로를 조회할 수 없다.")
    @Test
    void findShortestPathWithNotExistStation() {
        StationResponse 존재하지_않는_역 = new StationResponse(Long.MAX_VALUE, "구글역", LocalDateTime.now(), LocalDateTime.now());
        ExtractableResponse<Response> response = 최단_경로_조회_요청(김포공항역, 존재하지_않는_역);

        최단_경로_조회_실패됨_404(response);
    }


    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response, int expectTotalDistance, List<StationResponse> expectStations) {
        List<Long> expectStationsIds = expectStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int responseTotalDistance = response.jsonPath().getInt("distance");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualStationIds).containsAll(expectStationsIds),
                () -> assertThat(responseTotalDistance).isEqualTo(expectTotalDistance)
        );
    }

    public static void 최단_경로_조회_실패됨_400(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 최단_경로_조회_실패됨_404(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
