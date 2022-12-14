package nextstep.subway.path.acceptance;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
import org.springframework.http.MediaType;


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
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Feature 지하철 최단 경로를 조회
     *  Background
     *      Given 지하철역(Station) 강남,양재,교대,남터역이 등록되어 있음
     *      And 지하철노선(Line) 신분당선, 이호선, 삼호선이 등록되어 있음
     *      And 지하철노선에 지하철역(Section) 각 노선별로 등록되어 있음
     * <p>
     *  Scenario 출발역과 도착역 사이의 최단 경로 조회
     *      When 지하철 최단 경로 조회 요청
     *      Then 출발역과 도착역 사이의 최단 경로 조회됨
     * <p>
     *  Scenario 출발역과 도착역이 같을 때 최단 경로 조회
     *      When 지하철 최단 경로 조회 요청
     *      Then 최단 경로 조회 실패
     * <p>
     *  Scenario 존재하지 않은 출발역 또는 도착역으로 최단 경로 조회
     *      When 지하철 최단 경로 조회 요청
     *      Then 최단 경로 조회 실패
     */
    @DisplayName("출발역과 도착역 사이의 최단 경로 조회한다")
    @Test
    void findPath() {
        // when
        ExtractableResponse<Response> response = 지하철노선_최단경로_조회(교대역.getId(),양재역.getId());

        // then
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(8);
    }

    public static ExtractableResponse<Response> 지하철노선_최단경로_조회(Long sourceId, Long targetId){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId).queryParam("target",targetId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

}
