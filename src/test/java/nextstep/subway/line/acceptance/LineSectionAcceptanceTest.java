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

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private StationResponse 판교역;
    private StationResponse 성복역;
    private StationResponse 상현역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        성복역 = StationAcceptanceTest.지하철역_등록되어_있음("성복역").as(StationResponse.class);
        상현역 = StationAcceptanceTest.지하철역_등록되어_있음("상현역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("1번 시나리오: 지하철 구간을 관리")
    @Test
    void firstScenarioManageSubwaySection() {

        final ExtractableResponse<Response> createResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        지하철_노선에_지하철역_등록됨(createResponse);

        ExtractableResponse<Response> inquireAfterCreateResponse = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(inquireAfterCreateResponse, Arrays.asList(강남역, 양재역, 광교역));

        final ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        지하철_노선에_지하철역_제외됨(removeResponse);

        ExtractableResponse<Response> inquireAfterRemoveResponse = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(inquireAfterRemoveResponse, Arrays.asList(양재역, 광교역));

        final ExtractableResponse<Response> removeFailResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        지하철_노선에_지하철역_제외_실패됨(removeFailResponse);

        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 판교역, 정자역, 2);

        ExtractableResponse<Response> inquireAfterDoubleCreateResponse = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(inquireAfterDoubleCreateResponse, Arrays.asList(양재역, 판교역, 정자역, 광교역));

        final ExtractableResponse<Response> createAlreadyExistResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 3);
        지하철_노선에_지하철역_등록_실패됨(createAlreadyExistResponse);

        final ExtractableResponse<Response> createNotExistAnyResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 성복역, 상현역, 3);
        지하철_노선에_지하철역_등록_실패됨(createNotExistAnyResponse);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(
        final LineResponse line, final StationResponse upStation, final StationResponse downStation, final int distance) {

        final SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선에_지하철역_등록됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(final ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(final LineResponse line, final StationResponse station) {
        return RestAssured
            .given().log().all()
            .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
