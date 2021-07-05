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

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 900);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @Test
    void 지하철_구간_추가_및_조회_기능() {
        // when: 정자역, 양재역 구간을 등록 요청한다.
        ExtractableResponse<Response> 정자역_양재역_노선_등록 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);
        // then: 등록되지 않은 역구간으로 인해 등록에 실패한다.
        지하철_노선에_지하철역_등록_실패됨(정자역_양재역_노선_등록);
        // when: 신분당선 구간에 강남역, 양재역 구간을 등록 요청 한다.
        ExtractableResponse<Response> 강남역_양재역_노선_등록 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        // then: 신분당선 구간에 강남역, 양재역 구간이 등록된다.
        지하철_노선에_지하철역_등록됨(강남역_양재역_노선_등록);
        // when: 신분당선 구간에 정자역, 강남역 구간을 등록 요청 한다.
        ExtractableResponse<Response> 정자역_강남역_노선_등록 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);
        // then: 신분당선 구간에 정자역, 강남역 구간이 등록된다.
        지하철_노선에_지하철역_등록됨(정자역_강남역_노선_등록);
        // when: 신분당선 구간에 정자역, 강남역 구간을 등록 요청 한다.
        ExtractableResponse<Response> 정자역_강남역_노선_등록_실패 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);
        // then: 이미 등록된 구간이라 등록에 실패한다.
        지하철_노선에_지하철역_등록_실패됨(정자역_강남역_노선_등록_실패);
        // when: 지하철 노선을 조회한다.
        ExtractableResponse<Response> 신분당선_노선_정보 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then: 지하철 노선에 지하철역이 강남역, 양재역, 광교역 순서로 정렬되어 있다.
        지하철_노선에_지하철역_순서_정렬됨(신분당선_노선_정보, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @Test
    void 지하철_구간_삭제_기능() {
        // given: 신분당선 구간에 강남역 - 양재역 - 광교역 구간이 등록되어 있다.
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        // when: 강남역을 노선에서 제외 요청 한다.
        ExtractableResponse<Response> 강남역_제거 = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        // then: 강남역이 지하철역이 노선에서 제외된다.
        지하철_노선에_지하철역_제외됨(강남역_제거);
        // when: 양재역을 노선에서 제외 요청 한다.
        ExtractableResponse<Response> 양재역_제거 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        // then: 양재역이 노선에서 제외가 실패한다.
        지하철_노선에_지하철역_제외_실패됨(양재역_제거);
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
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
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
