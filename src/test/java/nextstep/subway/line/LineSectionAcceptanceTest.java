package nextstep.subway.line;

import static nextstep.subway.line.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록_실패됨;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_제외_실패됨;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_제외_요청;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_제외됨;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_역_못찾음;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineCreateRequest lineRequest = new LineCreateRequest("신분당선", "bg-red-600",
            new SectionRequest(강남역.getId(), 광교역.getId(), 10));
        신분당선 = 지하철_노선_등록되어_있음(lineRequest);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then
        assertAll(
            () -> 지하철_노선에_지하철역_등록됨(response),
            () -> 지하철_노선에_지하철역_순서_정렬됨(신분당선, Arrays.asList(강남역, 양재역, 광교역))
        );
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection_inAnyOrder() {
        // when
        ExtractableResponse<Response> response1 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        ExtractableResponse<Response> response2 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        // then
        assertAll(
            () -> 지하철_노선에_지하철역_등록됨(response1),
            () -> 지하철_노선에_지하철역_등록됨(response2),
            () -> 지하철_노선에_지하철역_순서_정렬됨(신분당선, Arrays.asList(정자역, 강남역, 양재역, 광교역))
        );
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSection_sameStation_400() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSection_notExistStation_400() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @Nested
    @DisplayName("지하철 노선의 역 삭제")
    class SectionDeleteAcceptanceTest {

        @BeforeEach
        void setUp() {
            지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
            지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 2);
        }

        @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
        @Test
        void removeLineSection() {
            // when
            ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(
                신분당선.getId(), 강남역.getId());

            // then
            assertAll(
                () -> 지하철_노선에_지하철역_제외됨(removeResponse),
                () -> 지하철_노선에_지하철역_순서_정렬됨(신분당선, Arrays.asList(양재역, 정자역, 광교역))
            );
        }

        @Test
        @DisplayName("지하철 노선에 존재하지 않는 역을 삭제한다.")
        void removeLineSection_notExistStationInLine() {
            // given
            StationResponse 반포역 = 지하철역_등록되어_있음("반포").as(StationResponse.class);

            // when
            ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(
                신분당선.getId(), 반포역.getId());

            // then
            지하철_노선에_지하철역_제외됨(removeResponse);
        }

        @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
        @Test
        void removeLineSection_lastSection_400() {
            // given
            지하철_노선에_지하철역_제외_요청(신분당선.getId(), 광교역.getId());
            지하철_노선에_지하철역_제외_요청(신분당선.getId(), 강남역.getId());

            // when
            ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(
                신분당선.getId(), 양재역.getId());

            // then
            지하철_노선에_지하철역_제외_실패됨(removeResponse);
        }

        @Test
        @DisplayName("삭제할 지하철 역이 없는 상태로 삭제한다.")
        void removeLineSection_nullStation_400() {
            // given, when
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_제외_요청(
                신분당선.getId(), null);

            // then
            지하철_노선에_지하철역_제외_실패됨(response);
        }

        @Test
        @DisplayName("존재하지 않는 역을 삭제한다.")
        void removeLineSection_notExistStation_404() {
            // given, when
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_제외_요청(
                신분당선.getId(), Long.MAX_VALUE);

            // then
            지하철_역_못찾음(response);
        }
    }
}
