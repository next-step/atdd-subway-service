package nextstep.subway.path.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTestRequest.지하철_노선_생성_요청_및_검증;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestRequest.지하철_노선에_지하철역_등록_요청_및_확인;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestRequest.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.path.acceptance.PathAcceptanceTestRequest.지하철_최단거리_요청_및_실패;
import static nextstep.subway.path.acceptance.PathAcceptanceTestRequest.지하철_최단거리_요청_및_확인;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = 지하철_노선_생성_요청_및_검증(new LineRequest("신분당선", "빨간색", 강남역.getId(), 양재역.getId(), 3))
                .as(LineResponse.class);
        이호선 = 지하철_노선_생성_요청_및_검증(new LineRequest("2호선", "초록색", 강남역.getId(), 정자역.getId(), 7))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_생성_요청_및_검증(new LineRequest("3호선", "주황색", 강남역.getId(), 광교역.getId(), 2))
                .as(LineResponse.class);
        사호선 = 지하철_노선_생성_요청_및_검증(new LineRequest("4호선", "보라색", 강남역.getId(), 광교역.getId(), 20))
                .as(LineResponse.class);
    }

    @TestFactory
    @DisplayName("최단거리를 검색한다")
    Stream<DynamicTest> 최단거리를_검색한다() {
        return Stream.of(
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("신분당선에 정자역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(신분당선, 강남역, 양재역, 정자역)),
                dynamicTest("2호선에 양재역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(이호선, 양재역, 정자역, 5)),
                dynamicTest("2호선에 양재역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(이호선, 강남역, 양재역, 정자역)),
                dynamicTest("3호선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(삼호선, 광교역, 정자역, 1)),
                dynamicTest("3호선에 정자역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(삼호선, 강남역, 광교역, 정자역)),
                dynamicTest("강남역과 정자역 최단거리를 확인한다", 지하철_최단거리_요청_및_확인(강남역, 정자역, Arrays.asList(강남역, 광교역, 정자역), 3))
        );
    }

    @TestFactory
    @DisplayName("환승을 하여 최단거리를 검색한다")
    Stream<DynamicTest> 환승을_하여_최단거리를_검색한다() {
        // 신분당선 -> 강남역 - 양재역 - 정자역
        // 2호선 -> 강남역 - 정자역 - 광교역
        // 신분당 + 2호선 => 10
        // 4호선 -> 강남역 - 양재역 - 정자역 - 광교역
        // 4호선 => 20
        return Stream.of(
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("신분당선에 정자역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(신분당선, 강남역, 양재역, 정자역)),
                dynamicTest("2호선에 광교역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(이호선, 정자역, 광교역, 5)),
                dynamicTest("2호선에 양재역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(이호선, 강남역, 정자역, 광교역)),
                dynamicTest("4호선에 양재역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(사호선, 강남역, 양재역, 5)),
                dynamicTest("4호선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(사호선, 양재역, 정자역, 5)),
                dynamicTest("4호선에 양재역 추가를 확인한다", 지하철_노선에_지하철역_순서_정렬됨(사호선, 강남역, 양재역, 정자역, 광교역)),
                dynamicTest("강남역과 광교역 최단거리를 확인한다", 지하철_최단거리_요청_및_확인(강남역, 광교역, Arrays.asList(강남역, 양재역, 정자역, 광교역), 10))
        );
    }

    @TestFactory
    @DisplayName("같은 역끼리의 최단거리는 찾지 못한다")
    Stream<DynamicTest> 같은_역끼리의_최단거리는_찾지_못한다() {
        return Stream.of(
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("정자역과 정자역을 찾는다", 지하철_최단거리_요청_및_실패(정자역, 정자역))
        );
    }

    @TestFactory
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않을경우 실패한다")
    Stream<DynamicTest> 출발역과_도착역이_연결이_되어_있지_않을경우_실패한다() {
        return Stream.of(
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("정자역과 광교역 찾는다", 지하철_최단거리_요청_및_실패(정자역, 광교역))
        );
    }

    @TestFactory
    @DisplayName("존재하지 않는 출발역이나 도착역으로 조회시 실패한다")
    Stream<DynamicTest> 존재하지_않는_출발역이나_도착역으로_조회시_실패한다() {
        StationResponse 쿄잉역 = new StationResponse(1000L, "쿄잉역", LocalDateTime.now(), LocalDateTime.now());
        return Stream.of(
                dynamicTest("신분당선에 정자역을 추가한다", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("정자역과 쿄잉역을 찾는다", 지하철_최단거리_요청_및_실패(정자역, 쿄잉역)),
                dynamicTest("쿄잉역과 정자역 찾는다", 지하철_최단거리_요청_및_실패(쿄잉역, 정자역))
        );
    }
}
