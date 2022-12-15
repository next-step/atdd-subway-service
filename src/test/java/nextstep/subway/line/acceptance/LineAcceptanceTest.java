package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.rest.LineRestAssured;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.rest.StationRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineCreateRequest 신분당선_생성_요청;
    private LineCreateRequest 구신분당선_생성_요청;
    private LineUpdateRequest 신분당선_수정_요청;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationRestAssured.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationRestAssured.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선_생성_요청 = new LineCreateRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 700);
        구신분당선_생성_요청 = new LineCreateRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15, 0);
        신분당선_수정_요청 = new LineUpdateRequest("구신분당선", "bg-red-600", 0);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = LineRestAssured.지하철_노선_생성_요청(신분당선_생성_요청);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        LineRestAssured.지하철_노선_등록되어_있음(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = LineRestAssured.지하철_노선_생성_요청(신분당선_생성_요청);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = LineRestAssured.지하철_노선_등록되어_있음(신분당선_생성_요청);
        ExtractableResponse<Response> createResponse2 = LineRestAssured.지하철_노선_등록되어_있음(구신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = LineRestAssured.지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = LineRestAssured.지하철_노선_등록되어_있음(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = LineRestAssured.지하철_노선_목록_조회_요청(createResponse);

        // then
        지하철_노선_응답됨(response, createResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String name = "신분당선";
        ExtractableResponse<Response> createResponse = LineRestAssured.지하철_노선_등록되어_있음(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = LineRestAssured.지하철_노선_수정_요청(createResponse, 신분당선_수정_요청);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = LineRestAssured.지하철_노선_등록되어_있음(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = LineRestAssured.지하철_노선_제거_요청(createResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    private static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 지하철_노선_응답됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    private static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
