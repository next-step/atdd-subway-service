package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600",
                강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600",
                교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600",
                교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("최단경로 조회 확인")
    void findShortestRoute() {
        //given
        PathResponse pathResponse = 최단경로확인(교대역.getId(), 양재역.getId())
                .jsonPath()
                .getObject("", PathResponse.class);
        //then
        assertThat(pathResponse.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void sourceTargetEqualExceptionTest() {
        assertThatThrownBy(() -> 최단경로확인(교대역.getId(), 교대역.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
    void disconnectExceptionTest() {
        //given
        StationResponse 인천역 = StationAcceptanceTest.지하철역_등록되어_있음("인천역").as(StationResponse.class);
        StationResponse 동인천역 = StationAcceptanceTest.지하철역_등록되어_있음("동인천역").as(StationResponse.class);
        LineResponse 일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "blue", 
                인천역.getId(), 동인천역.getId(),5)).as(LineResponse.class);
        
        //when
        //then
        assertThatThrownBy(() -> 최단경로확인(교대역.getId(), 인천역.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 출발역인 경우")
    void sourceStationNotExistExceptionTest() {
        assertThatThrownBy(() -> 최단경로확인(Long.MAX_VALUE, 교대역.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 도착역인 경우")
    void targetStationNotExistExceptionTest() {
        assertThatThrownBy(() -> 최단경로확인(교대역.getId(), Long.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static ExtractableResponse<Response> 최단경로확인(Long source, Long target) {
        final Map<String, Long> paramMap = new HashMap();
        paramMap.put("source", source);
        paramMap.put("source", target);

        return RestAssured
                .given().log().all()
                .params(paramMap)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
