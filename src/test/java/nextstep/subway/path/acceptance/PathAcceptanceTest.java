package nextstep.subway.path.acceptance;

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
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 신림역;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 잠실역;
    private StationResponse 삼성역;
    private StationResponse 화곡역;
    private LineResponse 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        // 지하철역_등록_되어_있음
        신림역 = 지하철역_등록("신림역");
        강남역 = 지하철역_등록("강남역");
        교대역 = 지하철역_등록("교대역");
        잠실역 = 지하철역_등록("잠실역");
        삼성역 = 지하철역_등록("삼성역");
        화곡역 = 지하철역_등록("화곡역");
    }

    private StationResponse 지하철역_등록(String name) {
        return StationAcceptanceTest.지하철역_등록되어_있음(name).as(StationResponse.class);
    }

    @Test
    @DisplayName("모든 구간의 거리가 동일할 때 최적의 경로 조회")
    void 지하철역_경로_조회() {
        // given
        노선_등록_되어_있음();
        구간_등록_되어_있음();
        //동일한 거리의 다른 경로 구간 등록 되어있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 10);


        // when
        // 강남역에서 삼성역까지의 최단 경로 조회
        ExtractableResponse<Response> response = 지하철_경로_조회(강남역, 삼성역);

        // then
        // 최단 경로 조회됨
        PathResponse pathResponse = response.as(PathResponse.class);
        지하철_경로_조회됨(pathResponse, Arrays.asList(강남역, 교대역, 삼성역));
    }

    @Test
    @DisplayName("짧은 경로 조회")
    void 지하철역_최적_경로_조회() {
        // given
        노선_등록_되어_있음();
        구간_등록_되어_있음();
        // 구간 수는 많지만 더 짧은 거리의 구간이 등록되어 있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);


        // when
        // 강남역에서 삼성역까지의 최단 경로 조회
        ExtractableResponse<Response> response = 지하철_경로_조회(강남역, 삼성역);

        // then
        // 최단 경로 조회됨
        PathResponse pathResponse = response.as(PathResponse.class);
        지하철_경로_조회됨(pathResponse, Arrays.asList(강남역, 교대역, 잠실역, 삼성역));
    }

    @Test
    @DisplayName("연결되어 있지 않는 경로 조회")
    void 지하철역_최적_경로_조회_예외() {
        // given
        노선_등록_되어_있음();
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 삼성역, 50);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);

        // when
        // 강남역에서 삼성역까지의 최단 경로 조회
        ExtractableResponse<Response> response = 지하철_경로_조회(강남역, 신림역);

        // then
        // 최단 경로 조회됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_경로_조회됨(PathResponse pathResponse, List<StationResponse> expectedStations) {
        List<Long> stationIds = pathResponse.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);

    }

    private ExtractableResponse<Response> 지하철_경로_조회(StationResponse 강남역, StationResponse 삼성역) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths/sourceId/{sourceId}/targetId/{targetId}", 강남역.getId(), 삼성역.getId())
                .then().log().all()
                .extract();
    }

    private void 구간_등록_되어_있음() {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 잠실역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 삼성역, 10);
    }

    private void 노선_등록_되어_있음() {
        LineRequest lineRequest = new LineRequest("이호선", "bg-red-600", 강남역.getId(), 교대역.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("오호선", "bg-red-600", 신림역.getId(), 화곡역.getId(), 10);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }
}
