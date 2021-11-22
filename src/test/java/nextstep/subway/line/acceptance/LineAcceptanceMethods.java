package nextstep.subway.line.acceptance;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.utils.StreamUtils;

public class LineAcceptanceMethods {
    public static final String LOCATION_HEADER_NAME = "Location";
    private static final String LINE_URL_PATH = "/lines";
    private static final String SLASH_SIGN = "/";

    public LineAcceptanceMethods() {}

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return post(LINE_URL_PATH, lineRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER_NAME);
        return 지하철_노선_목록_조회_요청(uri);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return 지하철_노선_목록_조회_요청(LINE_URL_PATH);
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String uri) {
        return get(uri);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response,
                                                                  LineRequest lineRequest) {
        String uri = response.header("Location");
        return put(uri, lineRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return delete(uri);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(LINE_URL_PATH + SLASH_SIGN + lineId)
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION_HEADER_NAME)).isNotBlank();
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

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
                                           List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = StreamUtils.mapToList(createdResponses,
                                                           it -> Long.parseLong(it.header(LOCATION_HEADER_NAME)
                                                                                  .split(SLASH_SIGN)[2]));
        List<Long> resultLineIds = StreamUtils.mapToList(response.jsonPath().getList(".", LineResponse.class),
                                                         LineResponse::getId);

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
