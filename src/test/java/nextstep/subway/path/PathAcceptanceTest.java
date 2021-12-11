package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;
    
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    
    @BeforeEach
    public void setUp() {
        super.setUp();
        
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        
        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 30);
        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 50);
        LineRequest 삼호선_요청 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 남부터미널역.getId(), 60);
        
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(삼호선_요청).as(LineResponse.class);
        
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 30);
        
        
    }
    
    
    @DisplayName("지하철 최단 경로를 조회한다")
    @Test
    void 지하철_최단_경로_조회() {
        ExtractableResponse<Response> response = 경로_조회_요청(교대역, 양재역);

        경로_조회_됨(response, Arrays.asList(교대역, 강남역, 양재역), 80);
        
    }
    
    public static ExtractableResponse<Response> 경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStation.getId())
                .param("target", targetStation.getId())
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 경로_조회_됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses, int distance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        
        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactlyElementsOf(stationResponses),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance)
                );
    }
    
}
