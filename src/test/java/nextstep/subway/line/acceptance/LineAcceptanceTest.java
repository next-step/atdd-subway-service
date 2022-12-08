package nextstep.subway.line.acceptance;

import static nextstep.subway.station.StationAcceptanceTest.*;
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
import nextstep.subway.amount.domain.Amount;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.LineId;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.StationId;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationId 강남역;
    private StationId 광교역;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_ID_추출(지하철역_등록되어_있음("강남역"));
        광교역 = 지하철역_ID_추출(지하철역_등록되어_있음("광교역"));
    }

    /**
     * Scenario1: 지하철 노선 관리
     * When 지하철 노선 생성 요청
     * Then 지하철 노선 생성
     * When 지하철 노선 생성 요청
     * Then 지하철 노선 생성
     * When 지하철 노선 목록 조회 요청
     * Then 지하철 노선 목록 응답됨
     * and 등록된 지하철역 지하철 노선 목록 포함됨
     * When 지하철 노선 조회 요청
     * Then 지하철 노선 응답됨
     * When 지하철 노선 수정 요청
     * Then 지하철 노선 수정됨
     * When 지하철 노선 제거 요청
     * Then 지하철 노선 삭제됨
     */
    @Test
    @DisplayName("지하철 노선 관리")
    public void scenario1() {
        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답_신분당선 = 지하철_노선_생성_요청(지하철_노선_생성_요청_신분당선());

        // then
        지하철_노선_생성됨(지하철_노선_생성_응답_신분당선);

        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답_구신분당선 = 지하철_노선_생성_요청(지하철_노선_생성_요청_구신분당선());

        // then
        지하철_노선_생성됨(지하철_노선_생성_응답_구신분당선);

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(지하철_노선_목록_조회_응답);
        지하철_노선_목록_포함됨(지하철_노선_목록_조회_응답, Arrays.asList(지하철_노선_생성_응답_신분당선, 지하철_노선_생성_응답_구신분당선));

        // when
        LineId 신분당선 = 지하철_노선_ID_추출(지하철_노선_생성_응답_신분당선);
        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회_요청(신분당선);

        // then
        지하철_노선_응답됨(지하철_노선_조회_응답);

        // when
        ExtractableResponse<Response> 지하철_노선_수정_응답 = 지하철_노선_수정_요청(지하철_노선_생성_응답_신분당선,
            지하철_노선_수정_요청_파라미터("변경신분당선", "bg-red-700"));

        // then
        지하철_노선_수정됨(지하철_노선_수정_응답);

        // when
        ExtractableResponse<Response> 지하철_노선_제거_응답 = 지하철_노선_제거_요청(지하철_노선_생성_응답_신분당선);

        // then
        지하철_노선_삭제됨(지하철_노선_제거_응답);
    }

    /**
     * 지하철 노선 관리 실패
     * When 지하철 노선 생성 요청
     * Then 지하철 노선 생성
     * When 기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성 요청
     * Then 지하철 노선 생성 실패됨
     */
    @Test
    void manageLine_failed() {
        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답_신분당선 = 지하철_노선_생성_요청(지하철_노선_생성_요청_신분당선());

        // then
        지하철_노선_생성됨(지하철_노선_생성_응답_신분당선);

        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답_실패 = 지하철_노선_생성_요청(지하철_노선_생성_요청_신분당선());

        // then
        지하철_노선_생성_실패됨(지하철_노선_생성_응답_실패);
    }

    /**
     * 지하철 노선 관리 실패
     * When 추가요금이 있는 지하철 노선 생성 요청
     * Then 지하철 노선 생성 성공
     * When 지하철 노선 조회
     * Then 지하철 노선 조회 성공
     * And 지하철 요금 확인 성공
     */
    @Test
    void additionalAmountLine() {
        // when
        Amount 추가요금 = Amount.from(200L);
        ExtractableResponse<Response> 추가요금_지하철_노선_생성_응답 = 지하철_노선_생성_요청(지하철_노선_생성_요청_추가요금(추가요금));
        // then
        지하철_노선_생성됨(추가요금_지하철_노선_생성_응답);
        // when
        LineId 추가요금_노선 = 지하철_노선_ID_추출(추가요금_지하철_노선_생성_응답);
        ExtractableResponse<Response> 지하처_노선_조회 = 지하철_노선_조회_요청(추가요금_노선);
        // and
        추가요금_확인됨(지하처_노선_조회, 추가요금);
    }

    private void 추가요금_확인됨(ExtractableResponse<Response> response, Amount amount) {
        assertThat(response.jsonPath().getLong("amount")).isEqualTo(amount.value());
    }

    private Map<String, String> 지하철_노선_생성_요청_추가요금(Amount amount) {
        return 추가요금이_있는_지하철_노선_생성_요청_파라미터("구신분당선", "bg-red-600", 강남역, 광교역, Distance.from(15), amount);
    }

    private Map<String, String> 지하철_노선_생성_요청_구신분당선() {
        return 지하철_노선_생성_요청_파라미터("구신분당선", "bg-red-600", 강남역, 광교역, Distance.from(15));
    }

    private Map<String, String> 지하철_노선_생성_요청_신분당선() {
        return 지하철_노선_생성_요청_파라미터("신분당선", "bg-red-600", 강남역, 광교역, Distance.from(10));
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> params) {
        return 지하철_노선_생성_요청(params);
    }

    public static Map<String, String> 지하철_노선_생성_요청_파라미터(String name, String color, StationId upStationId,
        StationId downStationId, Distance distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.getString());
        params.put("downStationId", downStationId.getString());
        params.put("distance", Integer.toString(distance.value()));
        return params;
    }

    public static Map<String, String> 추가요금이_있는_지하철_노선_생성_요청_파라미터(String name, String color, StationId upStationId,
        StationId downStationId, Distance distance, Amount amount) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.getString());
        params.put("downStationId", downStationId.getString());
        params.put("distance", Integer.toString(distance.value()));
        params.put("amount", Long.toString(amount.value()));
        return params;
    }

    private static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().
            extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return 지하철_노선_목록_조회_요청("/lines");
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String uri) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineId lineId) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}", lineId.getString())
            .then().log().all()
            .extract();
    }

    private Map<String, String> 지하철_노선_수정_요청_파라미터(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response,
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

    private ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
            .given().log().all()
            .when().delete(uri)
            .then().log().all()
            .extract();
    }

    private void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
        List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
            .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
            .map(LineResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static LineId 지하철_노선_ID_추출(ExtractableResponse<Response> response) {
        return LineId.from(response.jsonPath().getLong("id"));
    }
}
