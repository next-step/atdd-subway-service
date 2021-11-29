package nextstep.subway.line.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.groups.Tuple;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public final class LineAcceptanceStep {

    public static LineResponse 지하철_노선_등록되어_있음(LineCreateRequest params) {
        return 지하철_노선_생성_요청(params)
            .as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineCreateRequest params) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}", response.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, LineUpdateRequest params) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(Long id) {
        return RestAssured.given().log().all()
            .when().delete("lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_생성됨(
        ExtractableResponse<Response> response, String expectedName, String expectedColor,
        List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.header("Location")).isNotBlank(),
            () -> assertThat(line.getId()).isNotNull(),
            () -> assertThat(line.getName()).isEqualTo(expectedName),
            () -> assertThat(line.getColor()).isEqualTo(expectedColor),
            () -> assertThat(line.getCreatedDate()).isNotNull(),
            () -> assertThat(line.getModifiedDate()).isNotNull(),
            () -> assertThat(line.getStations())
                .hasSize(2)
                .extracting(StationResponse::getId, StationResponse::getName)
                .containsExactly(
                    expectedStations.stream()
                        .map(station -> tuple(station.getId(), station.getName()))
                        .toArray(Tuple[]::new)
                )
        );
    }

    public static void 지하철_노선_응답됨(
        ExtractableResponse<Response> response, LineResponse createdLine) {
        LineResponse line = response.as(LineResponse.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(line.getId()).isEqualTo(createdLine.getId()),
            () -> assertThat(line.getName()).isEqualTo(createdLine.getName()),
            () -> assertThat(line.getColor()).isEqualTo(createdLine.getColor())
        );
    }

    public static void 지하철_노선_수정_실패됨(ExtractableResponse<Response> response) {
        잘못된_요청(response);
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        잘못된_요청(response);
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
        List<LineResponse> expectedLines) {
        List<LineResponse> lineResponses = response.as(new TypeRef<List<LineResponse>>() {
        });
        assertThat(lineResponses)
            .extracting(LineResponse::getId, LineResponse::getName, LineResponse::getColor)
            .containsExactly(
                expectedLines.stream()
                    .map(line -> tuple(line.getId(), line.getName(), line.getColor()))
                    .toArray(Tuple[]::new)
            );
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response,
        LineResponse createdLine, String expectedName, String expectedColor) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(지하철_노선_조회_요청(createdLine)
                .as(LineResponse.class))
                .extracting(LineResponse::getName, LineResponse::getColor)
                .containsExactly(expectedName, expectedColor)
        );
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_못찾음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private static void 잘못된_요청(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
