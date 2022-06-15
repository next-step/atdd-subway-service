package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

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
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 오호선;
    private LineResponse 이호선;
    private LineResponse 육호선;
    private LineResponse 부산일호선;
    private StationResponse 공덕역;
    private StationResponse 영등포구청역;
    private StationResponse 합정역;
    private StationResponse 당산역;
    private StationResponse 서면역;
    private StationResponse 동래역;

    /**
     * 합정역      --- *6호선* ---         공덕역
     * |                                        |
     * *2호선*                            *5호선*
     * |                                        |
     * 당산역      --- *2호선* ---   영등포구청역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        //given
        공덕역 = 지하철역_등록되어_있음("공덕역").as(StationResponse.class);
        영등포구청역 = 지하철역_등록되어_있음("영등포구청역").as(StationResponse.class);
        당산역 = 지하철역_등록되어_있음("당산역").as(StationResponse.class);
        합정역 = 지하철역_등록되어_있음("합정역").as(StationResponse.class);
        서면역 = 지하철역_등록되어_있음("서면역").as(StationResponse.class);
        동래역 = 지하철역_등록되어_있음("동래역").as(StationResponse.class);

        오호선 = 지하철_노선_등록되어_있음(new LineRequest("오호선", "bg-red-600", 공덕역.getId(), 영등포구청역.getId(), 6)).as(LineResponse.class);
        육호선 = 지하철_노선_등록되어_있음(new LineRequest("육호선", "bg-red-600", 합정역.getId(), 공덕역.getId(), 4)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 합정역.getId(), 영등포구청역.getId(), 8)).as(LineResponse.class);
        부산일호선 = 지하철_노선_등록되어_있음(new LineRequest("부산일호선", "bg-red-600", 동래역.getId(), 서면역.getId(), 4)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 합정역, 당산역, 3);
    }

    /**
     * Feature: 지하철 경로 관련 기능
     *
     * Background
     * Given 지하철 역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철 역 등록되어 있음
     *
     * Scenario: 최단 경로 탐색
     * When 지하철 출발역과 도착역 사이 최단 경로 조회 요첨
     * Then 최단 경로 조회됨
     */
    @Test
    @DisplayName("지하철 역과 역사이의 최단 경로를 탐색한다.")
    void shortestPath() {
        ExtractableResponse<Response> 최단_경로_조회_결과 = 최단_경로_조회_요청(합정역, 영등포구청역);
        최단_경로_조회됨(최단_경로_조회_결과);
        최단_경로에_지하철역_순서_정렬됨(최단_경로_조회_결과, Arrays.asList(합정역, 당산역, 영등포구청역));
    }

    /**
     * Feature: 지하철 경로 관련 기능
     *
     * Background
     * Given 지하철 역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철 역 등록되어 있음
     *
     * Scenario: 최단 경로 탐색 예외
     * When 동일한 지하철 출발역과 도착역으로 최단 경로 조회 요청
     * Then 최단 경로 조회 실패됨
     * When 연결되어 있지 않은 지하철 출발역과 도착역으로 최단 경로 조회 요청
     * Then 최단 경로 조회 실패됨
     * When 존재하지 않는 지하철 출발역이나 도착역으로 최단 경로 조회 요청
     * Then 최단 경로 조회 실패됨
     */
    @Test
    @DisplayName("최단 경로 탐색을 할 수 없다.")
    void shortestPathException() {
        ExtractableResponse<Response> 최단_경로_조회_결과 = 최단_경로_조회_요청(합정역, 합정역);
        최단_경로_조회_실패됨(최단_경로_조회_결과);

        ExtractableResponse<Response> 연결되어_있지_않은_역_최단_경로_조회_결과 = 최단_경로_조회_요청(합정역, 서면역);
        최단_경로_조회_실패됨(연결되어_있지_않은_역_최단_경로_조회_결과);

        StationResponse 존재하지_않는_역 = new StationResponse(99L, "존재하지 않는 역", LocalDateTime.now(), LocalDateTime.now());
        ExtractableResponse<Response> 존재하지_않는_역_최단_경로_조회_결과 = 최단_경로_조회_요청(존재하지_않는_역, 서면역);
        최단_경로_조회_실패됨(존재하지_않는_역_최단_경로_조회_결과);
    }

    private void 최단_경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStationResponse, StationResponse targetStationResponse) {
        return RestAssured
                .given()
                .queryParam("source", sourceStationResponse.getId())
                .queryParam("target", targetStationResponse.getId())
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단_경로에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }
}
