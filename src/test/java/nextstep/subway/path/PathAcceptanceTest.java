package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(
                LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(LineResponse lineResponse, StationResponse upStation,
                                                               StationResponse downStation,
                                                               int distance) {
        return LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(lineResponse,
                upStation, downStation, distance);
    }

    @Test
    @DisplayName("교대역-양재역 최단경로 조회하기 (교대역-남부터미널역-양재역)")
    public void findMinPath() {
        ExtractableResponse<Response> 최단경로조회_요청결과 = 최단경로조회_요청하기(교대역, 양재역);
        최단_경로_목록_검증(최단경로조회_요청결과, 5, "교대역","남부터미널역", "양재역");
    }


    @Test
    @DisplayName("경로 시작역 도착역 일치 조회 에러 확인")
    public void findMinPathSourceTargetSame() {
        ExtractableResponse<Response> 최단경로조회_요청결과 = 최단경로조회_요청하기(교대역, 교대역);
        최단_경로_목록_요청결과_에러(최단경로조회_요청결과);
    }

    @Test
    @DisplayName("경로 조회시 미존재 역 에러 확인")
    public void findMinPathNotExsistStation() {
        ExtractableResponse<Response> 최단경로조회_요청결과 = 최단경로조회_요청하기(new StationResponse(), new StationResponse());
        최단_경로_목록_요청결과_에러(최단경로조회_요청결과);
    }

    private void 최단_경로_목록_요청결과_에러(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 최단_경로_목록_검증(ExtractableResponse<Response> response,int distance, String... stationNames) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getList("stations.name")).containsExactly(stationNames);
    }

    private ExtractableResponse<Response> 최단경로조회_요청하기(StationResponse source, StationResponse target) {
        Map<String, Long> param = new HashMap<>();
        param.put("source", source.getId());
        param.put("target", target.getId());

        return RestAssured
                .given().log().all()
                .queryParams(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

}
