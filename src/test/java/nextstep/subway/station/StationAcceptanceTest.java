package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("시나리오1: 지하철 역을 관리한다.")
    @Test
    void manageStationTest() {
        // when
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);
        // then
        지하철역_생성됨(createResponse);

        // when
        ExtractableResponse<Response> foundResponse = 지하철역_목록_조회_요청();
        // then
        지하철역_목록_응답됨(foundResponse);
        지하철역_목록에_포함되어_있음(foundResponse, Collections.singletonList(createResponse));

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(createResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("시나리오2: 실수로 같은 지하철 역을 두번 등록한다.")
    @Test
    void registerStationTwiceTest() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);
        지하철역_생성됨(createResponse);

        // when
        ExtractableResponse<Response> secondCreateResponse = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성_실패됨(secondCreateResponse);
    }

    @DisplayName("시나리오3: 실수로 지하철 노선에 등록된 지하철 역을 삭제한다.")
    @Test
    void tryDeleteStationInLineSectionTest() {
        // given
        StationResponse stationResponse1 = 지하철역_생성_요청(강남역).as(StationResponse.class);
        StationResponse stationResponse2 = 지하철역_생성_요청(역삼역).as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("2호선", "초록색", stationResponse1.getId(), stationResponse2.getId(), 5)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse1.getId());

        // then
        지하철역_제거_실패(response);
    }

    @DisplayName("시나리오4: 실수로 등록한 적 없는 지하철 역을 삭제한다.")
    @Test
    void tryDeleteNotExistStationTest() {
        Long notExistStationId = 1000L;

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(notExistStationId);

        // then
        지하철역_제거_실패(response);
    }

    public static ExtractableResponse<Response> 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(Long stationId) {
        String uri = "/stations/" + stationId;

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철역_제거_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록에_포함되어_있음(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedStationIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultStationIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    public static void 지하철_노선에서_삭제해도_역은_남아있음(ExtractableResponse<Response> response, StationResponse stationResponse) {
        Long expectedStationId = stationResponse.getId();

        List<Long> resultStationIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultStationIds).contains(expectedStationId);
    }

    public static void 지하철_역_목록에_포함되지_않음(Long notExistStationId1, Long notExistStationId2) {
        ExtractableResponse<Response> stationResponse = 지하철역_목록_조회_요청();
        List<StationResponse> stations = stationResponse.jsonPath().getList(".", StationResponse.class);
        List<Long> stationIds = stations.stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(stationIds).doesNotContain(notExistStationId1, notExistStationId2);
    }
}
