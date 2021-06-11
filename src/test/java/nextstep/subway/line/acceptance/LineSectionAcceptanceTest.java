package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_목록_응답됨;
import static org.assertj.core.api.Assertions.assertThat;
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
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @TestFactory
    @DisplayName("지하철 구간을 등록한다")
    Stream<DynamicTest> 지하철_구간을_등록한다() {
        return Stream.of(
                dynamicTest("노선에 역 등록 요청 및 확인", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 강남역, 양재역, 3)),
                dynamicTest("역이 순서 정렬이 됨", 지하철_노선에_지하철역_순서_정렬됨(신분당선, 강남역, 양재역, 광교역))
        );
    }

    @TestFactory
    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    Stream<DynamicTest> 지하철_노선에_여러개의_역을_순서_상관_없이_등록한다() {
        return Stream.of(
                dynamicTest("노선에 역 등록 요청 및 확인", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 강남역, 양재역, 3)),
                dynamicTest("노선에 역 등록 요청 및 확인", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 정자역, 강남역, 5)),
                dynamicTest("역이 순서 정렬이 됨", 지하철_노선에_지하철역_순서_정렬됨(신분당선, 정자역, 강남역, 양재역, 광교역))
        );
    }

    @TestFactory
    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    Stream<DynamicTest> 지하철_노선에_이미_등록되어있는_역을_등록한다() {
        return Stream.of(
                dynamicTest("노선에 역 등록 요청 및 확인", 지하철_노선에_지하철역_등록_요청_및_확인(신분당선, 강남역, 양재역, 3)),
                dynamicTest("이미 있는 역을 추가하면 실패 강남역 - 양재역", 지하철_노선에_지하철역_등록_요멍_및_실패_확인(신분당선, 강남역, 양재역, 3)),
                dynamicTest("이미 있는 역을 추가하면 실패 양재역 - 광교역", 지하철_노선에_지하철역_등록_요멍_및_실패_확인(신분당선, 양재역, 광교역, 3))
        );
    }

    @TestFactory
    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    Stream<DynamicTest> 지하철_노선에_등록되지_않은_역을_기준으로_등록한다() {
        return Stream.of(
                dynamicTest("이미 있는 역을 추가하면 실패 양재역 - 정자역", 지하철_노선에_지하철역_등록_요멍_및_실패_확인(신분당선, 양재역, 정자역, 3))
        );
    }

    public static Executable 지하철_노선에_지하철역_등록_요청_및_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록됨(response);
        };
    }

    public static Executable 지하철_노선에_지하철역_순서_정렬됨(LineResponse line, StationResponse ...stationResponses) {
        return () -> {
            ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(line);

            지하철_노선_목록_응답됨(response);

            지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(stationResponses));
        };
    }

    public static Executable 지하철_노선에_지하철역_등록_요멍_및_실패_확인(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return () -> {
            ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);

            지하철_노선에_지하철역_등록_실패됨(response);
        };
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
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

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
