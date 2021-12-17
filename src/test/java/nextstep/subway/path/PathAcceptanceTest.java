package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
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
    private String 토큰;
    
    /**
     * 교대역    --- *2호선 (30)* ------- 강남역
     * |                             |
     * *3호선 (20)*                 *신분당선 (50)*
     * |                             |
     * 남부터미널역  --- *3호선(30)* ---- 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        
        LineRequest 이호선_요청 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 30, 300);
        LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 50, 500);
        LineRequest 삼호선_요청 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 남부터미널역.getId(), 20, 700);
        
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(이호선_요청).as(LineResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(삼호선_요청).as(LineResponse.class);
        
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 30);
        
        MemberAcceptanceTest.회원_생성을_요청("jennie267@email.com", "pwjennie", 10);

        토큰 = AuthAcceptanceTest.토큰_조회(AuthAcceptanceTest.로그인_요청("jennie267@email.com", "pwjennie"));
    }
    
    @DisplayName("지하철 최단 경로 시나리오 통합 테스트")
    @Test
    void 지하철_최단_경로_통합_테스트() {
        // When
        ExtractableResponse<Response> 교대역_양재역_최단_경로_조회 = 경로_조회_요청(교대역, 양재역, 토큰);

        // Then
        경로_조회_됨(교대역_양재역_최단_경로_조회, Arrays.asList(교대역, 남부터미널역, 양재역), 50, 850);
        
        
        // When
        LineSectionAcceptanceTest.지하철_노선에_지하철역_제외_요청(삼호선, 양재역);
        ExtractableResponse<Response> 삼호선_양재역_삭제후_최단_경로_조회 = 경로_조회_요청(교대역, 양재역, 토큰);
        
        // Then
        경로_조회_됨(삼호선_양재역_삭제후_최단_경로_조회, Arrays.asList(교대역, 강남역, 양재역), 80, 1_050);
        
        
        // Given
        StationResponse 신규역 = StationAcceptanceTest.지하철역_등록되어_있음("신규역").as(StationResponse.class);
        
        // When
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 신규역, 15);
        ExtractableResponse<Response> 신분당선_신규역_등록후_최단_경로_조회 = 경로_조회_요청(교대역, 양재역, 토큰);
        
        // Then
        경로_조회_됨(신분당선_신규역_등록후_최단_경로_조회, Arrays.asList(교대역, 강남역, 신규역, 양재역), 80, 1_050);
        
    }
    
    
    @DisplayName("지하철 최단 경로를 조회한다")
    @Test
    void 지하철_최단_경로_조회() {
        // When
        ExtractableResponse<Response> response = 경로_조회_요청(교대역, 양재역, 토큰);

        // Then
        경로_조회_됨(response, Arrays.asList(교대역, 남부터미널역, 양재역), 50, 850);
        
    }
    
    @DisplayName("지하철역 제외 후 최단 경로를 조회한다")
    @Test
    void 지하철역_제외_후_지하철_최단_경로_조회() {
        //When
        LineSectionAcceptanceTest.지하철_노선에_지하철역_제외_요청(삼호선, 양재역);
        ExtractableResponse<Response> 삼호선_양재역_삭제후_최단_경로_조회 = 경로_조회_요청(교대역, 양재역, 토큰);
        
        // Then
        경로_조회_됨(삼호선_양재역_삭제후_최단_경로_조회, Arrays.asList(교대역, 강남역, 양재역), 80, 1_050);
        
    }
    
    @DisplayName("지하철역 추가 후 최단 경로를 조회한다")
    @Test
    void 지하철역_추가_후_지하철_최단_경로_조회() {
        // Given
        StationResponse 신규역 = StationAcceptanceTest.지하철역_등록되어_있음("신규역").as(StationResponse.class);
        // When
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 신규역, 10);
        ExtractableResponse<Response> 삼호선_신규역_추가후_최단_경로_조회 = 경로_조회_요청(교대역, 양재역, 토큰);
        
        // Then
        경로_조회_됨(삼호선_신규역_추가후_최단_경로_조회, Arrays.asList(교대역, 남부터미널역, 신규역, 양재역), 50, 850);
        
    }
    
    public static ExtractableResponse<Response> 경로_조회_요청(StationResponse sourceStation, StationResponse targetStation, String token) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStation.getId())
                .param("target", targetStation.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 경로_조회_됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses, int distance, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        
        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactlyElementsOf(stationResponses),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance),
                () -> assertThat(pathResponse.getFare()).isEqualTo(fare)
                );
    }
    
}
