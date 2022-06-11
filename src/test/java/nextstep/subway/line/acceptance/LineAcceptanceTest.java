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
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    /**
     * Feature: 지하철 노선 관련 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *
     *   Scenario: 지하철 노선을 관리
     *     When 지하철 노선 신분당선 등록 요청
     *     Then 지하철 노선 신분당선 등록됨
     *     When 지하철 노선 구신분당선 등록 요청
     *     Then 지하철 노선 구신분당선 등록됨
     *     When 지하철 노선 신분당선 조회 요청
     *     Then 지하철 노선 신분당선 조회됨
     *     When 지하철 노선 목록 조회 요청
     *     Then 등록한 신분당선, 구신분당선 목록이 조회됨
     *     When 지하철 노선 신분당선 삭제 요청
     *     Then 지하철 노선 신분당선 삭제됨
     *     When 지하철 노선 구신분당선 수정 요청
     *     Then 지하철 노선 구신분당선 수정됨
     *     When 지하철 노선 목록 조회 요청
     *     Then 삭제한 신분당선, 수정한 구신분당선 반영된 목록이 조회됨
     */
    @DisplayName("지하철 노선을 관리한다.")
    @Test
    void 지하철_노선_관리_정상_시나리오() {
        ExtractableResponse<Response> 신분당선_생성 = 지하철_노선_생성_요청(lineRequest1);
        지하철_노선_생성됨(신분당선_생성);

        ExtractableResponse<Response> 구신분당선_생성 = 지하철_노선_생성_요청(lineRequest2);
        지하철_노선_생성됨(구신분당선_생성);

        ExtractableResponse<Response> 신분당선_조회 = 지하철_노선_목록_조회_요청(신분당선_생성);
        지하철_노선_응답됨(신분당선_조회, 신분당선_생성);

        ExtractableResponse<Response> 노선목록_조회 = 지하철_노선_목록_조회_요청();
        지하철_노선_목록_응답됨(노선목록_조회);
        지하철_노선_목록_포함됨(노선목록_조회, Arrays.asList(신분당선_생성, 구신분당선_생성));

        ExtractableResponse<Response> 신분당선_제거 = 지하철_노선_제거_요청(신분당선_생성);
        지하철_노선_삭제됨(신분당선_제거);

        LineRequest newLineRequest = new LineRequest("새로운노선", "newColor", 강남역.getId(), 광교역.getId(), 15);
        ExtractableResponse<Response> 구신분당선_수정 = 지하철_노선_수정_요청(구신분당선_생성, newLineRequest);
        지하철_노선_수정됨(구신분당선_수정);

        ExtractableResponse<Response> 노선목록_조회2 = 지하철_노선_목록_조회_요청();
        지하철_노선_목록_응답됨(노선목록_조회2);
        지하철_노선_목록_포함됨(노선목록_조회2, Arrays.asList(구신분당선_생성));
    }

    /**
     * Feature: 지하철 노선 관련 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     Given 지하철 노선 신분당선 등록되어 있음
     *
     *   Scenario: 지하철 노선을 관리
     *     When 지하철 노선 신분당선 등록 재요청
     *     Then 지하철 노선 신분당선 등록 실패
     */
    @DisplayName("지하철 노선을 관리 실패한다.")
    @Test
    void 지하철_노선_관리_비정상_시나리오() {
        ExtractableResponse<Response> 신분당선_생성 = 지하철_노선_등록되어_있음(lineRequest1);

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);
        지하철_노선_생성_실패됨(response);
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
}
