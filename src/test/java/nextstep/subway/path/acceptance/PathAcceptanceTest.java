package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTestActions.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


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
     * <p>교대역 ---*2호선* (10)--- 강남역</p>
     * |                        |
     * <p>*3호선* (5)          *신분당선* (10)</p>
     * |                        |
     * <p>남부터미널역 -- *3호선* (3)-- 양재</p>
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 남부터미널역.getId(), 5))
                .as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 3);
    }

    /**
     * Given 지하철 노선이 등록되어 있다
     * <p>
     * When 출발역과 도착역을 입력하면
     * <p>
     * Then 최단 경로가 조회된다
     */
    @Test
    void pathFind() {
        //when
        PathResponse response = RestAssured
                .given().log().all()
                .when().get("/paths?source={source}&target={target}", 남부터미널역.getId(), 강남역.getId())
                .then().log().all()
                .extract().as(PathResponse.class);

        //then
        assertThat(response.getStations()).containsExactly(남부터미널역, 양재역, 강남역);
        assertThat(response.getDistance()).isEqualTo(13);
    }
}
