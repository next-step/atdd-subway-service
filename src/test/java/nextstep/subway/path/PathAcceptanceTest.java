package nextstep.subway.path;

import static nextstep.subway.line.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.line.step.LineSectionAcceptanceStep.지하철_역_못찾음;
import static nextstep.subway.path.step.PathAcceptanceStep.지하철_역_최단_경로_포함됨;
import static nextstep.subway.path.step.PathAcceptanceStep.지하철_최단_경로_실패됨;
import static nextstep.subway.path.step.PathAcceptanceStep.지하철_최단_경로_조회;
import static nextstep.subway.path.step.PathAcceptanceStep.지하철_최단_경로_조회됨;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---      강남역
     * |                                |
     * *3호선*                      *신분당선*
     * |                                |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        지하철_노선_등록되어_있음(new LineCreateRequest("신분당선", "bg-red-600",
            new SectionRequest(강남역.getId(), 양재역.getId(), 10)));
        지하철_노선_등록되어_있음(new LineCreateRequest("이호선", "bg-red-600",
            new SectionRequest(교대역.getId(), 강남역.getId(), 10)));

        LineResponse 삼호선 = 지하철_노선_등록되어_있음(new LineCreateRequest("삼호선", "bg-red-600",
            new SectionRequest(교대역.getId(), 양재역.getId(), 5)));
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("지하철 최단 경로 조회")
    void paths() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 양재역.getId());

        // then
        assertAll(
            () -> 지하철_최단_경로_조회됨(response),
            () -> 지하철_역_최단_경로_포함됨(response, Arrays.asList(교대역, 남부터미널역, 양재역), 5)
        );
    }

    @Test
    @DisplayName("출발역과 도착역은 달라야 함")
    void paths_sameSourceAndTargetStation_400() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 교대역.getId());

        // then
        지하철_최단_경로_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역은 지하철 노선들에 존재해야 함")
    void paths_sourceAndTargetStationAreNotConnected_400() {
        // given
        StationResponse 반포역 = 지하철역_등록되어_있음("반포역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 반포역.getId());

        // then
        지하철_최단_경로_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역은 반드시 존재해야 함")
    void paths_notExistSourceStation_404() {
        // given, when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(Long.MAX_VALUE, 교대역.getId());

        // then
        지하철_역_못찾음(response);
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값으로 경로를 조회할 수 없다.")
    @DisplayName("출발역과 도착역의 id 값은 필수")
    @CsvSource({"1,", ",1"})
    void paths_nullSourceOrTargetId_400(Long sourceId, Long targetId) {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(sourceId, targetId);

        // then
        지하철_최단_경로_실패됨(response);
    }
}
