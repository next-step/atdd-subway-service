package nextstep.subway.line.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static nextstep.subway.line.domain.LineSectorTestSnippet.*;
import static nextstep.subway.line.domain.LineTestSnippet.지하철_노선_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 구간 관련 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 노선을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageLine() {
        return Stream.of(
                dynamicTest("지하철 노선에 역 등록 요청 및 성공 확인(신분당선, 강남역 - 양재역)", 지하철_노선에_지하철역_등록_요청_및_성공_확인(신분당선, 강남역, 양재역, 3)),
                dynamicTest("지하철 노선과 이에 속한 정렬된 역 확인(강남역 - 양재역 - 광교역)", 지하철_노선_조회_요청_및_확인(신분당선, asList(강남역, 양재역, 광교역))),
                dynamicTest("지하철_노선에_지하철역_제외_요청 및 성공 확인(신분당선, 양재역)", 지하철_노선에_지하철역_제외_요청_및_성공_확인(신분당선, 양재역)),
                dynamicTest("지하철 노선과 이에 속한 정렬된 역 확인(강남역 - 양재역 - 광교역)", 지하철_노선_조회_요청_및_확인(신분당선, asList(강남역, 광교역)))
        );
    }

    @DisplayName("지하철 구간을 등록한다.")
    @TestFactory
    Stream<DynamicTest> addLineSection() {
        // given
        List<StationResponse> 노선_내_정렬된_역 = asList(강남역, 양재역, 광교역);

        return Stream.of(
                dynamicTest("지하철 노선에 역 등록 요청 및 성공 확인(신분당선, 강남역 - 양재역)", 지하철_노선에_지하철역_등록_요청_및_성공_확인(신분당선, 강남역, 양재역, 3)),
                dynamicTest("지하철 노선과 이에 속한 정렬된 역 확인(강남역 - 양재역 - 광교역)", 지하철_노선_조회_요청_및_확인(신분당선, 노선_내_정렬된_역))
        );
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @TestFactory
    Stream<DynamicTest> addLineSection2() {
        // given
        List<StationResponse> 노선_내_정렬된_역 = asList(정자역, 강남역, 양재역, 광교역);

        // when, then
        return Stream.of(
                dynamicTest("지하철 노선에 역 등록 요청 및 성공 확인(신분당선, 강남역 - 양재역)", 지하철_노선에_지하철역_등록_요청_및_성공_확인(신분당선, 강남역, 양재역, 2)),
                dynamicTest("지하철 노선에 역 등록 요청 및 성공 확인(신분당선, 정자역 - 강남역)", 지하철_노선에_지하철역_등록_요청_및_성공_확인(신분당선, 정자역, 강남역, 5)),
                dynamicTest("지하철 노선과 이에 속한 정렬된 역 확인(신분당선, 정자 - 강남 - 양재 - 광교)", 지하철_노선_조회_요청_및_확인(신분당선, 노선_내_정렬된_역))
        );
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @TestFactory
    Stream<DynamicTest> addLineSectionWithSameStation() {
        return Stream.of(
                dynamicTest("지하철 노선에 역 등록 요청 및 성공 확인(신분당선, 강남역 - 양재역)", 지하철_노선에_지하철역_등록_요청_및_성공_확인(신분당선, 강남역, 양재역, 2)),
                dynamicTest("지하철 노선에 이미 등록되어 있는 역 등록 요청 및 성공 확인(신분당선, 강남역 - 광교역)", 지하철_노선에_지하철역_등록_요청_및_실패_확인(신분당선, 강남역, 광교역, 3)),
                dynamicTest("지하철 노선에 이미 등록되어 있는 역 등록 요청 및 성공 확인(신분당선, 강남역 - 광교역)", 지하철_노선에_지하철역_등록_요청_및_실패_확인(신분당선, 강남역, 양재역, 3))
        );
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @TestFactory
    Stream<DynamicTest> addLineSectionWithNoStation() {
        return Stream.of(
                dynamicTest("지하철 노선에 연결되어 있지 않는 역 등록 요청 및 실패 확인(신분당선, 정자역 - 양재역)", 지하철_노선에_지하철역_등록_요청_및_실패_확인(신분당선, 정자역, 양재역, 3))
        );
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @TestFactory
    Stream<DynamicTest> removeLineSection1() {
        // given
        List<StationResponse> 노선_내_정렬된_역 = asList(강남역, 정자역, 광교역);

        return Stream.of(
                dynamicTest("지하철 노선에 역 등록 요청 및 성공 확인(신분당선, 강남역 - 양재역)", 지하철_노선에_지하철역_등록_요청_및_성공_확인(신분당선, 강남역, 양재역, 2)),
                dynamicTest("지하철 노선에 역 등록 요청 및 성공 확인(신분당선, 양재역 - 정자역)", 지하철_노선에_지하철역_등록_요청_및_성공_확인(신분당선, 양재역, 정자역, 2)),
                dynamicTest("지하철_노선에_지하철역_제외_요청 및 성공 확인(신분당선, 양재역)", 지하철_노선에_지하철역_제외_요청_및_성공_확인(신분당선, 양재역)),
                dynamicTest("지하철 노선과 이에 속한 정렬된 역 확인(신분당선, 강남역 - 정자역)", 지하철_노선_조회_요청_및_확인(신분당선, 노선_내_정렬된_역))

        );
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @TestFactory
    void removeLineSection2() {
        dynamicTest("지하철_노선에_지하철역_제외_요청 및 실패 확인(신분당선, 양재역)", 지하철_노선에_지하철역_제외_요청_및_실패_확인(신분당선, 강남역));
    }
}
