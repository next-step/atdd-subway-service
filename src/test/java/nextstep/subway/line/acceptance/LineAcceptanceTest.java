package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 양재역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private ExtractableResponse<Response> lineExtractableResponse1;
    private ExtractableResponse<Response> lineExtractableResponse2;
    private LineResponse 신분당선;
    private LineResponse 구신분당선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);

        lineExtractableResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        lineExtractableResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        신분당선 = lineExtractableResponse1.as(LineResponse.class);
        구신분당선 = lineExtractableResponse2.as(LineResponse.class);
    }

    @DisplayName("지하철 구간 관리 기능")
    @Test
    void manageLineFunction() {
        // when
        ExtractableResponse<Response> lineSectionExtractableResponse = 지하철_구간_등록_요청(신분당선, 강남역, 양재역, 2);

        // then
        지하철_구간_등록됨(lineSectionExtractableResponse);

        // when
        ExtractableResponse<Response> lineExtractableResponse = 지하철_노선에_등록된_역_목록_조회_요청(lineExtractableResponse1);

        // then
        등록한_지하철_구간이_반영된_역_목록이_조회됨(lineExtractableResponse, 강남역, 양재역);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_구간_삭제요청(신분당선, 양재역);

        // then
        지하철_구간_삭제됨(removeResponse);

        // when
        ExtractableResponse<Response> removeStationLineExtractableResponse = 지하철_노선에_등록된_역_목록_조회_요청(lineExtractableResponse1);

        // then
        삭제한_지하철_구간이_반영된_역_목록이_조회됨(removeStationLineExtractableResponse, 양재역);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // then
        지하철_노선_생성됨(lineExtractableResponse1);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineExtractableResponse1, lineExtractableResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청(lineExtractableResponse1);

        // then
        지하철_노선_응답됨(response, lineExtractableResponse1);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineExtractableResponse2, lineRequest2);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineExtractableResponse1);

        // then
        지하철_노선_삭제됨(response);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest params) {
        return 지하철_노선_생성_요청(params);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return 지하철_노선_목록_조회_요청("/lines");
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return 지하철_노선_목록_조회_요청(uri);
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, LineRequest params) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 지하철_노선에_등록된_역_목록_조회_요청(ExtractableResponse<Response> response) {
        return 지하철_노선_목록_조회_요청(response);
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static void 지하철_구간_등록됨(ExtractableResponse<Response> response) {
        지하철_노선_목록_응답됨(response);
    }

    private void 삭제한_지하철_구간이_반영된_역_목록이_조회됨(ExtractableResponse<Response> response, StationResponse removedStation) {
        assertThat(response.jsonPath().getList("stations", StationResponse.class)).doesNotContain(removedStation);
    }

    private void 지하철_구간_삭제됨(ExtractableResponse<Response> response) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_제외됨(response);

    }

    public static ExtractableResponse<Response> 지하철_구간_삭제요청(LineResponse line, StationResponse station) {
        return LineSectionAcceptanceTest.지하철_노선에_지하철역_제외_요청(line, station);
    }

    public static void 등록한_지하철_구간이_반영된_역_목록이_조회됨(ExtractableResponse<Response> response, StationResponse upStation, StationResponse downStation) {
        assertThat(response.jsonPath().getList("stations", StationResponse.class)).contains(upStation, downStation);

    }
}
