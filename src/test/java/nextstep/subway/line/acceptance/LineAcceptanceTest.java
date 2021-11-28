package nextstep.subway.line.acceptance;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_응답됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_조회_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_목록_포함됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_못찾음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_삭제됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_생성_실패됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_생성됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_수정_실패됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_수정_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_수정됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_응답됨;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_제거_요청;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.step.LineAcceptanceStep;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineCreateRequest 신분당선_생성_파라미터;
    private LineCreateRequest 구신분당선_생성_파라미터;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선_생성_파라미터 = new LineCreateRequest("신분당선", "bg-red-600",
            new SectionRequest(강남역.getId(), 광교역.getId(), 10));
        구신분당선_생성_파라미터 = new LineCreateRequest("구신분당선", "bg-red-600",
            new SectionRequest(강남역.getId(), 광교역.getId(), 15));
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_생성_파라미터);

        // then
        지하철_노선_생성됨(response,
            신분당선_생성_파라미터.getName(),
            신분당선_생성_파라미터.getColor(),
            Arrays.asList(강남역, 광교역));
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine_duplicateName_400() {
        // given
        지하철_노선_등록되어_있음(신분당선_생성_파라미터);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_생성_파라미터);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값으로 지하철 노선을 생성할 수 없다.")
    @DisplayName("이름, 색상, 상행역 id, 하행역 id, 거리는 지하철 노선 생성에 필수")
    @CsvSource({",color,1,2,10", "name,,1,2,10",
        "name,color,,2,10", "name,color,1,,10", "name,color,1,2,0"})
    void createLine_emptyParameter_400(String name, String color,
        Long upStationId, Long downStationId, Integer distance) {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(
            new LineCreateRequest(name, color,
                new SectionRequest(upStationId, downStationId, distance)));

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse createResponse1 = 지하철_노선_등록되어_있음(신분당선_생성_파라미터);
        LineResponse createResponse2 = 지하철_노선_등록되어_있음(구신분당선_생성_파라미터);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertAll(
            () -> 지하철_노선_목록_응답됨(response),
            () -> 지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2))
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse createResponse = 지하철_노선_등록되어_있음(신분당선_생성_파라미터);

        // when
        ExtractableResponse<Response> response = LineAcceptanceStep.지하철_노선_조회_요청(createResponse);

        // then
        지하철_노선_응답됨(response, createResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String updatedName = "구신분당선";
        String updatedColor = "bg-red-600";
        LineResponse createdResponse = 지하철_노선_등록되어_있음(신분당선_생성_파라미터);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createdResponse.getId(),
            new LineUpdateRequest(updatedName, updatedColor));

        // then
        지하철_노선_수정됨(response, createdResponse, updatedName, updatedColor);
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값으로 노선을 수정할 수 없다.")
    @DisplayName("수정하려는 이름 또는 색상은 필수다.")
    @CsvSource({",color", "name,"})
    void updateLine_emptyNameOrColor_400(String updatedName, String updatedColor) {
        // given
        LineResponse createdLine = 지하철_노선_등록되어_있음(신분당선_생성_파라미터);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(
            createdLine.getId(), new LineUpdateRequest(updatedName, updatedColor));

        // then
        지하철_노선_수정_실패됨(response);
    }

    @Test
    @DisplayName("기존에 존재하는 노선 이름으로 변경할 수 없다.")
    void updateLine_duplicationName_400() {
        // given
        지하철_노선_등록되어_있음(신분당선_생성_파라미터);
        LineResponse createdLine = 지하철_노선_등록되어_있음(구신분당선_생성_파라미터);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(
            createdLine.getId(), new LineUpdateRequest(신분당선_생성_파라미터.getName(), "any"));

        // then
        지하철_노선_수정_실패됨(response);
    }

    @DisplayName("수정하려는 지하철 노선을 반드시 존재해야 한다.")
    @Test
    void updateLine_notExistsLine_404() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(Long.MAX_VALUE,
            new LineUpdateRequest("any", "any"));

        // then
        지하철_노선_못찾음(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse createLine = 지하철_노선_등록되어_있음(신분당선_생성_파라미터);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createLine.getId());

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("제거하려는 지하철 노선은 반드시 존재해야 한다.")
    @Test
    void deleteLine_notExistsLine_400() {
        // given, when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(Long.MAX_VALUE);

        // then
        지하철_노선_못찾음(response);
    }
}
