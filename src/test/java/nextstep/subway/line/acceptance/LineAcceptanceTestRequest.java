package nextstep.subway.line.acceptance;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.request.AcceptanceTestRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTestRequest {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청_및_검증(LineRequest params) {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        지하철_노선_생성됨(response);

        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest params) {
        return 지하철_노선_생성_요청(params);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return 지하철_노선_목록_조회_요청("/lines");
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return 지하철_노선_목록_조회_요청(uri);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return post(
                Given.builder()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .build()
                , When.builder()
                        .uri("/lines")
                        .build()
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String uri) {
        return get(
                Given.builder()
                        .accept(ContentType.JSON)
                        .build()
                , When.builder()
                        .uri(uri)
                        .build()
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return get(
                Given.builder()
                        .accept(ContentType.JSON)
                        .build()
                , When.builder()
                        .uri("/lines/{lineId}")
                        .pathParams(response.getId())
                        .build()
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, LineRequest params) {
        String uri = response.header("Location");

        return put(
                Given.builder()
                        .contentType(ContentType.JSON)
                        .body(params)
                        .build()
                , When.builder()
                        .uri(uri)
                        .build()
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return delete(
                Given.builder().build()
                , When.builder().uri(uri).build()
        );
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
