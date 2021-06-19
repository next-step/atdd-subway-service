package nextstep.subway.line.acceptance;

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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

// Feature
@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선;
    private LineRequest 구신분당선;

    // Background
    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given: 지하철 역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        // And: 지하철 노선 등록되어 있음
        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        구신분당선 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성됨(생성된_신분당선);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> 생성된_신분당선 = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성_실패됨(생성된_신분당선);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> 등록되어있는_신분당선 = 지하철_노선_등록되어_있음(신분당선);
        ExtractableResponse<Response> 등록되어있는_구신분당선 = 지하철_노선_등록되어_있음(구신분당선);

        // when
        ExtractableResponse<Response> 조회된_노선_목록 = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(조회된_노선_목록);
        지하철_노선_목록_포함됨(조회된_노선_목록, Arrays.asList(등록되어있는_신분당선, 등록되어있는_구신분당선));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> 등록되어있는_신분당선 = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> 조회된_노선_목록 = 지하철_노선_목록_조회_요청(등록되어있는_신분당선);

        // then
        지하철_노선_응답됨(조회된_노선_목록, 등록되어있는_신분당선);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String name = "신분당선";
        ExtractableResponse<Response> 등록되어있는_신분당선 = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> 구신분당선으로_수정된_신분당선 = 지하철_노선_수정_요청(등록되어있는_신분당선, 구신분당선);

        // then
        지하철_노선_수정됨(구신분당선으로_수정된_신분당선);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> 등록되어있는_신분당선 = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> 제거된_신분당선 = 지하철_노선_제거_요청(등록되어있는_신분당선);

        // then
        지하철_노선_삭제됨(제거된_신분당선);
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

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
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
}
