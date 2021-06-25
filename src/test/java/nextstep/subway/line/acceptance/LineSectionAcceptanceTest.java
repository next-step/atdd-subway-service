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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private ExtractableResponse<Response> 지하철_노선_등록되어_있음;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        지하철_노선_등록되어_있음 = 지하철_노선_등록되어_있음(lineRequest);
        신분당선 = 지하철_노선_등록되어_있음.as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 관리한다.")
    @Test
    void manageLineSection() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then
        ExtractableResponse<Response> addResponse = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(addResponse);
        지하철_노선에_지하철역_순서_정렬됨(addResponse, Arrays.asList(강남역, 양재역, 광교역));

        // when
        ExtractableResponse<Response> searchResponse = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(searchResponse);
        지하철_노선_목록_포함됨(searchResponse, Collections.singletonList(지하철_노선_등록되어_있음));

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 광교역));

        // when
        ExtractableResponse<Response> searchResponse2 = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(searchResponse2);
        지하철_노선_목록_포함됨(searchResponse2, Collections.singletonList(지하철_노선_등록되어_있음));
    }

    @DisplayName("지하철 구간 예외를 관리한다.")
    @Test
    void manageExceptionLineSection() {
        // when
        ExtractableResponse<Response> addDuplicateResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(addDuplicateResponse);

        // when
        ExtractableResponse<Response> addNotExistResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(addNotExistResponse);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
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

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
