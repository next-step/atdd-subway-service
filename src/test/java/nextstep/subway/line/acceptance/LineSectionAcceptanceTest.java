package nextstep.subway.line.acceptance;

import static nextstep.subway.line.acceptance.LineRestAssured.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineRestAssured.지하철_노선_조회_요청;
import static nextstep.subway.line.acceptance.LineSectionRestAssured.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionRestAssured.지하철_노선에_지하철역_제외_요청;
import static nextstep.subway.station.acceptance.StationRestAssured.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 신사역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        신사역 = 지하철역_등록되어_있음("신사역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("노선에 존재하는 구간 사이에 새로운 역을 등록한다.")
    @Test
    void addStationBetweenLine() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 정자역));
    }

    @DisplayName("새로운 역을 상행 종점으로 등록한다.")
    @Test
    void addStationInFrontOfUpStation() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 신사역, 강남역, 3);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(신사역, 강남역, 정자역));
    }

    @DisplayName("새로운 역을 하행 종점으로 등록한다.")
    @Test
    void addStationAfterDownStation() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 3);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("기존 역 사이 거리보다 크거나 같은 거리의 역을 노선에 등록한다.")
    @ParameterizedTest(name = "기존 역 사이 거리(10)보다 {0}은 크거나 같으므로 구간이 생성되지 않는다.")
    @ValueSource(ints = {10, 12, 25})
    void addSectionWhichHasEqualOrLongerDistance(int currentDistance) {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, currentDistance);

        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSectionMultipleTime() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 신사역, 강남역, 5);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(신사역, 강남역, 양재역, 정자역));
    }

    @DisplayName("노선에 기등록된 역들의 구간을 등록하면 새로운 구간이 생성되지 않는다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 정자역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("노선에 등록되지 않은 역들의 구간을 등록하면 새로운 구간이 생성되지 않는다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 신사역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선 중간에 등록된 지하철역을 제외한다.")
    @Test
    void deleteStationInMiddle() {
        // given
        지하철_노선에_지하철역_등록_요청(신분당선, 신사역, 강남역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(신사역, 강남역, 정자역));
    }

    @DisplayName("노선의 상행 종점을 제거하면 노선에서 해당 역이 제거되고 상행 종점이 바뀐다.")
    @Test
    void deleteStationWhichIsUpStation() {
        // given
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(양재역, 정자역));
    }

    @DisplayName("노선의 하행 종점을 제거하면 노선에서 해당 역이 제거되고 하행 종점이 바뀐다.")
    @Test
    void deleteStationWhichIsDownStation() {
        // given
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 정자역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역));
    }

    @DisplayName("노선에 구간이 1개이면 해당 구간에 포함된 역을 노선에서 제거할 수 없다.")
    @Test
    void deleteStationWhenLineHasOneSection() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }

//    @DisplayName("노선에 등록되지 않은 역을 제거하면 노선에서 역이 제거되지 않는다.")
//    @Test
//    void deleteStationNotInLine() {
//        // given
//        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
//
//        // when
//        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 신사역);
//
//        // then
//        지하철_노선에_지하철역_제외_실패됨(removeResponse);
//    }

    private static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
