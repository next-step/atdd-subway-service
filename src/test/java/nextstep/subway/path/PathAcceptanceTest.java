package nextstep.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 경강선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 세종대왕릉역;
    private StationResponse 여주역;

    /**
     * 교대역    --- *2호선* ---   강남역      세종대왕릉역
     * |                          |            |
     * *3호선*                   *신분당선*     *경강선*
     * |                          |            |
     * 남부터미널역  --- *3호선* ---  양재        여주역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        세종대왕릉역 = StationAcceptanceTest.지하철역_등록되어_있음("세종대왕릉역").as(StationResponse.class);
        여주역 = StationAcceptanceTest.지하철역_등록되어_있음("여주역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 6);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 4);
        경강선 = 지하철_노선_등록되어_있음("경강선", "bg-red-600", 세종대왕릉역, 여주역, 12);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPath() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 양재역);

        // then
        최단경로_조회_성공(response);
        최단경로_역순서_확인(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        경로_길이_확인(response, 4);
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 같은 경우)")
    @Test
    void findPathFail_equalStation() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 교대역);

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 연결이 되어 있지 않은 경우)")
    @Test
    void findPathFail_notConnected() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 세종대왕릉역);

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("최단 거리 경로 조회 실패 (존재하지 않은 출발역이나 도착역을 조회 할 경우)")
    @Test
    void findPathFail_notExist() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, new StationResponse());

        //then
        최단경로_조회_실패(response);
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(StationResponse source, StationResponse target) {
        PathRequest request = new PathRequest(source.getId(), target.getId());

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/paths")
                .then()
                .log().all()
                .extract();
    }

    public static void 최단경로_역순서_확인(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 경로_길이_확인(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    public static void 최단경로_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
