package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_등록_실패됨;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_제외_실패됨;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_제외_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_제외됨;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptance.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @TestFactory
    Stream<DynamicTest> addLineSection() {
        return Stream.of(
            dynamicTest("지하철 노선에 지하철역을 등록하면 정상적으로 등록 된다", () -> {
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
                지하철_노선에_지하철역_등록됨(response);
            }),
            dynamicTest("등록한 지하철 노선을 조회후에 정상적으로 조회 되는지 검증한다", () -> {
                ExtractableResponse<Response> response = LineAcceptance.지하철_노선_조회_요청(신분당선);
                지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));
            })
        );
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @TestFactory
    Stream<DynamicTest> addLineSection2() {
        return Stream.of(
            dynamicTest("지하철 노선에 지하철역을 순서에 상관없이 등록하면 정상적으로 등록된다", () -> {
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
                ExtractableResponse<Response> response2 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);
                
                지하철_노선에_지하철역_등록됨(response);
                지하철_노선에_지하철역_등록됨(response2);
            }),
            dynamicTest("지하철 노선을 조회하면 등록에 상관없이 지하철 역의 순서가 정렬된다", () -> {
                ExtractableResponse<Response> response = LineAcceptance.지하철_노선_조회_요청(신분당선);
                지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));
            })
        );
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @TestFactory
    Stream<DynamicTest> removeLineSection1() {
        return Stream.of(
            dynamicTest("지하철역 제외를 위해 지하철역들을 등록한다", () -> {
                지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
                지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 2);
            }),
            dynamicTest("지하쳘역을 해당 노선에서 제외시키면 정상적으로 제외가 된다", () -> {
                ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
                지하철_노선에_지하철역_제외됨(removeResponse);
            }),
            dynamicTest("노선을 조회하면 제외한 지하철역이 제외되어 조회된다", () -> {
                ExtractableResponse<Response> response = LineAcceptance.지하철_노선_조회_요청(신분당선);
                지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 정자역, 광교역));
            })
        );
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }
}
