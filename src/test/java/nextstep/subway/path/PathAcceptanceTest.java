package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
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
    private LineResponse 수인분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;
    private StationResponse 매봉역;
    private StationResponse 양재시민의숲;
    private StationResponse 정자역;

    /**
     * 교대역    --- *2호선* ---   강남역  --- *2호선* --- 역삼역   --- *2호선* ---  선릉역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  --- *3호선* ---  매봉
     *                          |
     *                          *신분당선*
     *                          |
     *                          양재시민의숲
     * <p>
     *      --- *수인분당선* ---  정자
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        매봉역 = 지하철역_등록되어_있음("매봉역").as(StationResponse.class);
        양재시민의숲 = 지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재시민의숲.getId(), 20).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역.getId(), 선릉역.getId(), 30).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 교대역.getId(), 매봉역.getId(), 30).as(LineResponse.class);
        수인분당선 = 지하철_노선_등록되어_있음("수인분당선", "bg-yello-600", 교대역.getId(), 매봉역.getId(), 30).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 10);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 10);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 10);
        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 10);
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 최단 경로 조회됨
     */
    @Test
    @DisplayName("지하철 최단 경로 조회한다.")
    void 최단_경로_조회() {
        // when
        ExtractableResponse<Response> 조회결과 = 지하철_경로_조회_요청(교대역.getId(), 매봉역.getId());
        // then
        지하철_경로_조회됨(조회결과);
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 최단 경로 조회 실패
     */
    @Test
    @DisplayName("출발역과 도착역이 같은 경우를 조회한다.")
    void 출발역과_도착역이_같은_경우_경로_조회() {
        // when
        ExtractableResponse<Response> 조회결과 = 지하철_경로_조회_요청(교대역.getId(), 교대역.getId());
        // then
        지하철_경로_조회_실패됨(조회결과);
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 최단 경로 조회 실패
     */
    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우를 조회한다.")
    void 출발역과_도착역이_연결이_되어_있지_않은_경우_경로_조회() {
        // when
        ExtractableResponse<Response> 조회결과 = 지하철_경로_조회_요청(교대역.getId(), 정자역.getId());
        // then
        지하철_경로_조회_실패됨(조회결과);
    }

    /**
     * When 지하철 경로 조회 요청
     * Then 최단 경로 조회 실패
     */
    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우를 조회한다.")
    void 존재하지_않은_출발역이나_도착역을_조회_할_경우_경로_조회() {
        // when
        ExtractableResponse<Response> 조회결과 = 지하철_경로_조회_요청(교대역.getId(), -1L);
        // then
        지하철_경로_조회_실패됨(조회결과);
    }


    private static void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 지하철_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 지하철_경로_조회_요청(Long sourceId, Long targetId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
