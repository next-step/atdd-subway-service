package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 종합운동장;
    private StationResponse 잠실새내;
    private StationResponse 잠실;
    private StationResponse 석촌;
    private StationResponse 가락시장;
    private StationResponse 오금;
    private StationResponse 천호;
    private StationResponse 마천;
    private StationResponse 강남;
    private StationResponse 광교;

    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private LineResponse 팔호선;
    private LineResponse 신분당선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        종합운동장 = 지하철역_등록되어_있음("종합운동장").as(StationResponse.class);
        잠실새내 = 지하철역_등록되어_있음("잠실새내").as(StationResponse.class);
        잠실 = 지하철역_등록되어_있음("잠실").as(StationResponse.class);
        석촌 = 지하철역_등록되어_있음("석촌").as(StationResponse.class);
        가락시장 = 지하철역_등록되어_있음("가락시장").as(StationResponse.class);
        오금 = 지하철역_등록되어_있음("오금").as(StationResponse.class);
        천호 = 지하철역_등록되어_있음("천호").as(StationResponse.class);
        마천 = 지하철역_등록되어_있음("마천").as(StationResponse.class);
        강남 = 지하철역_등록되어_있음("강남").as(StationResponse.class);
        광교 = 지하철역_등록되어_있음("광교").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("2호선", "green", 종합운동장.getId(), 잠실새내.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("3호선", "orange", 가락시장.getId(), 오금.getId(), 20);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("5호선", "purple", 천호.getId(), 마천.getId(), 90);
        오호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("8호선", "pink", 잠실.getId(), 석촌.getId(), 10);
        팔호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        lineRequest = new LineRequest("신분당선", "red", 강남.getId(), 광교.getId(), 120);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(이호선, 잠실새내, 잠실, 10);
        지하철_노선에_지하철역_등록_요청(오호선, 천호, 오금, 50);
        지하철_노선에_지하철역_등록_요청(팔호선, 석촌, 가락시장, 20);
        지하철_노선에_지하철역_등록_요청(팔호선, 천호, 잠실, 30);
    }

    @DisplayName("종합운동장 ~ 석촌 최단 경로 조회")
    @Test
    void findShortestPath01() {
        ExtractableResponse<Response> response = 최단경로_조회(종합운동장, 석촌);

        최단경로_지하철역_순서_정렬됨(response, Arrays.asList(종합운동장, 잠실새내, 잠실, 석촌));
    }

    public static ExtractableResponse<Response> 최단경로_조회(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={stationId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static void 최단경로_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse path = response.as(PathResponse.class);

        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }
}
