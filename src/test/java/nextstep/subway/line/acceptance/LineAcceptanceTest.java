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
    private LineRequest lineRequest3;
    private LineRequest lineRequest4;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 500);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15, 300);
        lineRequest3 = new LineRequest("1호선", "bg-red-600", 강남역.getId(), 광교역.getId(), 7, 0);
        lineRequest4 = new LineRequest("2호선", "bg-red-600", 강남역.getId(), 광교역.getId(), 13, 0);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청(createResponse);

        // then
        지하철_노선_응답됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String name = "신분당선";
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createResponse, lineRequest2);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    /*
    Feature: 지하철 노선 관련 기능

    Background
        Given 지하철역 등록되어 있음

    Scenario: 지하철 노선을 관리 (생성 후 삭제)
        When 지하철 노선 등록 요청
        Then 지하철 노선 등록됨
        When 지하철 노선 삭제 요청
        Then 지하철 노선 삭제됨
        When 지하철 노선 목록 조회 요청
        Then 추가된 지하철 노선은 포함되고 삭제된 지하철 노선은 미포함됨
    */
    @Test
    @DisplayName("지하철 노선을 관리 (생성 후 삭제)")
    void all_1() {
        // When 지하철 노선 등록 요청
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(lineRequest1);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(lineRequest2);
        // Then 지하철 노선 등록됨
        지하철_노선_생성됨(createResponse1);
        지하철_노선_생성됨(createResponse2);

        // When 지하철 노선 삭제 요청
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(createResponse1);
        // Then 지하철 노선 삭제됨
        지하철_노선_삭제됨(deleteResponse);

        // When 지하철 노선 목록 조회 요청
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();
        // Then 추가된 지하철 노선은 포함되고 삭제된 지하철 노선은 미포함됨
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(createResponse2, createResponse2));
        지하철_노선_목록_미포함됨(response, Arrays.asList(createResponse1));
    }

    /*
    Feature: 지하철 노선 관련 기능

    Background
        Given 지하철역 등록되어 있음

    Scenario: 지하철 노선을 관리 (생성 후 수정)
        When 지하철 노선 등록 요청
        Then 지하철 노선 등록됨
        When 존재하는 이름으로 지하철 노선 등록 요청
        Then 지하철 노선이 등록되지 않음
        When 지하철 노선 수정 요청
        Then 지하철 노선 정보가 수정됨
    */
    @Test
    @DisplayName("지하철 노선을 관리 (생성 후 수정)")
    void all_2() {
        // When 지하철 노선 등록 요청
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(lineRequest1);
        // Then 지하철 노선 등록됨
        지하철_노선_생성됨(createResponse1);

        // When 존재하는 이름으로 지하철 노선 등록 요청
        ExtractableResponse<Response> failCreateResponse = 지하철_노선_생성_요청(lineRequest1);
        // Then 지하철 노선이 등록되지 않음
        지하철_노선_생성_실패됨(failCreateResponse);

        // When 지하철 노선 수정 요청
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(createResponse1, lineRequest4);
        // Then 지하철 노선 정보가 수정됨
        지하철_노선_수정됨(updateResponse);
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

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", id)
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

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    public static void 지하철_노선_정보_비교(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.as(LineResponse.class).getName()).isEqualTo(createdResponse.as(LineResponse.class).getName());
        assertThat(response.as(LineResponse.class).getColor()).isEqualTo(createdResponse.as(LineResponse.class).getColor());
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

    private void 지하철_노선_목록_미포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).doesNotContainAnyElementsOf(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
