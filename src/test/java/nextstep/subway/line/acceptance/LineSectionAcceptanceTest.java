package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 관리한다. (성공적)")
    @Test
    void manageLineSection() {
        지하철_노선에_지하철역_등록되어_있음(신분당선, 정자역, 강남역, 5);

        ExtractableResponse<Response> registerResponse = 지하철_구간_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_구간_등록됨(registerResponse);

        ExtractableResponse<Response> searchResponse = 지하철_노선에_등록된_역_목록_조회_요청(신분당선);
        등록한_지하철_구간이_반영된_역목록이_조회됨(searchResponse, 정자역, 강남역, 양재역, 광교역);

        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        지하철_노선에_지하철역_제외됨(removeResponse);

        ExtractableResponse<Response> searchResponse2 = 지하철_노선에_등록된_역_목록_조회_요청(신분당선);
        삭제한_지하철_구간이_반영된_역목록이_조회됨(searchResponse2, 강남역);
    }

    @DisplayName("지하철 구간을 관리한다. (실패적)")
    @Test
    void manageLineSection2() {
        ExtractableResponse<Response> registerResponse = 지하철_구간_노선에_모두_등록된_역들_등록_요청(신분당선, 강남역, 광교역, 2);
        지하철_노선에_지하철역_등록_실패됨(registerResponse);

        ExtractableResponse<Response> registerResponse2 = 지하철_구간_노선에_없는_역들_등록_요청(신분당선, 정자역, 양재역, 3);
        지하철_노선에_지하철역_등록_실패됨(registerResponse2);

        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    private ExtractableResponse<Response> 지하철_노선에_등록된_역_목록_조회_요청(LineResponse lineResponse) {
        return LineAcceptanceTest.지하철_노선_조회_요청(lineResponse);
    }

    private ExtractableResponse<Response> 지하철_구간_노선에_모두_등록된_역들_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    private ExtractableResponse<Response> 지하철_구간_노선에_없는_역들_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 지하철_노선에_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 등록한_지하철_구간이_반영된_역목록이_조회됨(ExtractableResponse<Response> response, StationResponse... expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = idListOf(line.getStations().stream());
        List<Long> expectedStationIds = idListOf(Arrays.stream(expectedStations));

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private void 삭제한_지하철_구간이_반영된_역목록이_조회됨(ExtractableResponse<Response> response, StationResponse expectedStation) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = idListOf(line.getStations().stream());
        Long expectedStationId = expectedStation.getId();

        assertThat(stationIds).doesNotContain(expectedStationId);
    }

    private List<Long> idListOf(Stream<StationResponse> expectedStations) {
        return expectedStations.map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
