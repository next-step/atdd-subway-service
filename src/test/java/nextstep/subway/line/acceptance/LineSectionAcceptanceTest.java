package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 구간 관련 기능")
public
class LineSectionAcceptanceTest extends AcceptanceTest {
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
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
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



    public Executable 지하철_노선에_지하철역_등록_요청_및_성공_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response =  지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록됨(response);
        };
    }

    private Executable 지하철_노선에_지하철역_등록_요청_및_실패_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response =  지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록_실패됨(response);
        };
    }

    private Executable 지하철_노선_조회_요청_및_확인(LineResponse response, List<StationResponse> 노선_내_정렬된_역) {
        return () -> {
            ExtractableResponse<Response> getResponse = LineAcceptanceTest.지하철_노선_조회_요청(response);

            지하철_노선_조회됨(getResponse);

            지하철_노선에_지하철역_순서_정렬됨(getResponse, 노선_내_정렬된_역);
        };
    }

    private Executable 지하철_노선에_지하철역_제외_요청_및_성공_확인(LineResponse line, StationResponse station) {
        return () -> {
            ExtractableResponse<Response> deleteResponse = this.지하철_노선에_지하철역_제외_요청(line, station);

            지하철_노선에_지하철역_제외됨(deleteResponse);
        };
    }

    private Executable 지하철_노선에_지하철역_제외_요청_및_실패_확인(LineResponse line, StationResponse station) {
        return () -> {
            ExtractableResponse<Response> deleteResponse = this.지하철_노선에_지하철역_제외_요청(line, station);

            지하철_노선에_지하철역_제외_실패됨(deleteResponse);
        };
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    private void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    
    private void 지하철_노선_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    
    private void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    private void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
