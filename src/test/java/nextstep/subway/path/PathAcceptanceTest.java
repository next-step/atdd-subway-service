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
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;


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


        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /**
     * Given : 경로 조회 가능한 노선들이 등록되어 있다
     * When : 출발역과 도착역의 경로 조회 요청하면
     * Then : 최단 경로와 거리를 응답한다.
     */
    @DisplayName("지하철 노선의 최단 경로를 조회한다")
    @Test
    void findPaths() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(교대역.getId(), 양재역.getId());

        // then
        지하철_경로_조회됨(response);
        지하철_경로_이름_확인됨(response, Arrays.asList("교대역", "남부터미널역", "양재역"));
    }

    /**
     * When : 출발역과 도착역을 같은 역으로 경로 조회 하면
     * Then : 최단 경로 조회에 실패한다.
     */
    @DisplayName("동일역 경로 조회 예외")
    @Test
    void findPathsException() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(교대역.getId(), 교대역.getId());

        // then
        지하철_경로_실패됨(response);
    }

    public static ExtractableResponse<Response> 지하철_경로_조회(Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    private static void 지하철_경로_이름_확인됨(ExtractableResponse<Response> response, List<String> stationNames) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStations())
                .extracting(StationResponse::getName)
                .containsExactlyElementsOf(stationNames);
    }

    public static void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_경로_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
