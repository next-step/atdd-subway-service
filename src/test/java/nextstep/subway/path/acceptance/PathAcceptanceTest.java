package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.LineSectionRestAssuredUtils.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.utils.LineSectionRestAssuredUtils.지하철_노선에_지하철역_등록됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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
     *               (10)
     * 교대역    --- *2호선* ---   강남역
     * |                             |
     * *3호선* (3)              *신분당선* (10)
     * |                             |
     * 남부터미널역  --- *3호선* ---   양재
     *                   (2)
     */

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "green", 교대역, 강남역, 10).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "orange", 교대역, 양재역, 5).as(LineResponse.class);

        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3));
    }

    @Test
    @DisplayName("지하철 경로를 조회")
    public void searchPath() {
        ExtractableResponse<Response> result = 경로_조회_요청(남부터미널역, 강남역);

        PathResponse path = result.as(PathResponse.class);
        assertThat(path.getStations()).containsExactly(남부터미널역, 양재역, 강남역);
        assertThat(path.getDistance()).isEqualTo(12);
    }

    private ExtractableResponse<Response> 경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured.given().log().all()
                .when()
                .get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }
}
















