package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given 지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
    }

    @Test
    @DisplayName("지하철 노선 관리")
    public void scenario() {
        // when 지하철 노선 생성 요청
        Map<String, String> createLineParams1 = 지하철_노선_생성_요청_파라미터1();
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_생성_요청(createLineParams1);

        // then 지하철 노선 생성
        지하철_노선_생성됨(createLineResponse1);

        // when 기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성 요청
        Map<String, String> alreadyExistsCreateLineParams = 지하철_노선_생성_요청_파라미터1();
        ExtractableResponse<Response> createLineFailedResponse = 지하철_노선_생성_요청(alreadyExistsCreateLineParams);

        // then 지하철 노선 생성 실패됨
        지하철_노선_생성_실패됨(createLineFailedResponse);

        // when 기존에 존재하지 않는 지하철 노선 이름으로 지하철 노선 생성 요청
        Map<String, String> createLineParams2 = 지하철_노선_생성_요청_파라미터2();
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_생성_요청(createLineParams2);

        // then 지하철 노선 생성
        지하철_노선_생성됨(createLineResponse2);

        // when 지하철 노선 목록 조회 요청
        ExtractableResponse<Response> getLinesResponse = 지하철_노선_목록_조회_요청();

        // then 지하철 노선 목록 응답됨
        // and 등록된 지하철역 지하철 노선 목록 포함됨
        지하철_노선_목록_응답됨(getLinesResponse);
        지하철_노선_목록_포함됨(getLinesResponse, Arrays.asList(createLineResponse1, createLineResponse2));

        // when 지하철 노선 조회 요청
        ExtractableResponse<Response> getLineResponse = 지하철_노선_목록_조회_요청(createLineResponse1);

        // then 지하철 노선 응답됨
        지하철_노선_응답됨(getLineResponse);

        // when 지하철 노선 수정 요청
        Map<String, String> params = 지하철_노선_수정_요청_파라미터("변경신분당선", "bg-red-700");
        ExtractableResponse<Response> updateLineResponse = 지하철_노선_수정_요청(createLineResponse1, params);

        // then 지하철 노선 수정됨
        지하철_노선_수정됨(updateLineResponse);

        // when 지하철 노선 제거 요청
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createLineResponse1);

        // then 지하철 노선 삭제됨
        지하철_노선_삭제됨(response);
    }

    private Map<String, String> 지하철_노선_생성_요청_파라미터2() {
        return 지하철_노선_생성_요청_파라미터("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    private Map<String, String> 지하철_노선_생성_요청_파라미터1() {
        return 지하철_노선_생성_요청_파라미터("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> params) {
        return 지하철_노선_생성_요청(params);
    }

    public static Map<String, String> 지하철_노선_생성_요청_파라미터(String name, String color, Long upStationId, Long downStationId,
        Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());
        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
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

    public static Map<String, String> 지하철_노선_수정_요청_파라미터(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response,
        Map<String, String> params) {
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

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
        List<ExtractableResponse<Response>> createdResponses) {
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
