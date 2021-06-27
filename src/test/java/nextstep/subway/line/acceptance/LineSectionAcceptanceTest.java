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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("")
    @Test
    void 통합인수테스트_구간을_추가_하고_싶은데_할_수가_없네() {
//        Background (setUp)
//        Given 지하철역 등록되어 있음
//        And 지하철 노선 등록되어 있음
//        And 지하철 노선에 지하철역 등록되어 있음

        // When 중복되는 구간을 추가 요청
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);
        // Then 추가 실패
        지하철_노선에_지하철역_등록_실패됨(response);

        // When 중복인가? 해서 등록되지 않은 역들을 추가 요청
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);
        // Then 추가 실패
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("없는역을 삭제 시도, 노선에 역이 두개 밖에 없는데 삭제 시도")
    @Test
    void 통합인수테스트_삭제를_하고_싶은데_할_수가_없네() {
//        Background (setUp)
//        Given 지하철역 등록되어 있음
//        And 지하철 노선 등록되어 있음
//        And 지하철 노선에 지하철역 등록되어 있음

        // When 잘못 눌러서 노선에 없는 역을 삭제 요청
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 정자역);
        // Then 삭제에 실패
        지하철_노선에_지하철역_제외_실패됨(removeResponse);

        // When 지우고 싶은 역은 잘 골랐는데, 삭제 요청 했지만 노선에 역이 두개 밖에 없음
        removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        // Then 삭제에 실패
        지하철_노선에_지하철역_제외_실패됨(removeResponse);

    }

    @Test
    void 통합인수테스트_지하철에_구간추가_후에_역을_삭제하고_전체조회해서_확인한다() {
//        Background (setUp)
//        Given 지하철역 등록되어 있음
//        And 지하철 노선 등록되어 있음
//        And 지하철 노선에 지하철역 등록되어 있음

//        When 지하철 구간 등록 요청
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
//        Then 지하철 구간 등록됨
        지하철_노선에_지하철역_등록됨(response);

//        When 지하철 노선에 순서가 얽히는 역을 하나 더 추가한다
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

//        When 지하철 노선에 등록된 역 목록 조회 요청
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

//        Then 등록한 지하철 구간이 반영된 역 목록이 정렬되어 조회됨
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));

//        When 지하철 구간 삭제 요청
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);
//        Then 지하철 구간 삭제됨
        지하철_노선에_지하철역_제외됨(removeResponse);

//        When 지하철 노선에 등록된 역 목록 조회 요청
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
//        Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역));

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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
