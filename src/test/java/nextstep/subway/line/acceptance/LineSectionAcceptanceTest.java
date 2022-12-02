package nextstep.subway.line.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private Long 신분당선;
    private Long 강남역;
    private Long 양재역;
    private Long 정자역;
    private Long 광교역;

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_ID_추출(지하철역_등록되어_있음("강남역"));
        양재역 = 지하철역_ID_추출(지하철역_등록되어_있음("양재역"));
        정자역 = 지하철역_ID_추출(지하철역_등록되어_있음("정자역"));
        광교역 = 지하철역_ID_추출(지하철역_등록되어_있음("광교역"));

        Map<String, String> lineRequest = LineAcceptanceTest.지하철_노선_생성_요청_파라미터("신분당선", "bg-red-600", 강남역,
            광교역, 10);
        신분당선 = 지하철_노선_ID_추출(지하철_노선_등록되어_있음(lineRequest));
    }

    /**
     * Scenario1: 지하철 구간 추가/삭제
     * When 지하철 구간 등록 요청
     * Then 지하철 구간 등록됨
     * When 지하철 구간 추가 등록 요청
     * Then 지하철 구간 등록됨
     * When 지하철 노선에 등록된 역 목록 조회 요청
     * Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
     * When 지하철 구간 삭제 요청
     * Then 지하철 구간 삭제됨
     * When 지하철 노선에 등록된 역 목록 조회 요청
     * Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     */
    @DisplayName("지하철 구간 추가/삭제")
    @Test
    void scenario1() {
        // when
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_응답);

        // when
        ExtractableResponse<Response> 지하철_노선에_지하철역_추가_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        // then
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_추가_등록_응답);

        // when
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then
        지하철_노선에_지하철역_등록됨(지하철_노선_조회_응답);
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답, Arrays.asList(정자역, 강남역, 양재역, 광교역));

        // when
        ExtractableResponse<Response> 지하철_노선_지하철역_제외_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(지하철_노선_지하철역_제외_응답);

        // when
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 광교역));
    }

    /**
     * Scenario2: 지하철 구간 추가/삭제 실패
     * When 지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외 요청
     * Then 제외 요청 실패
     * When 지하철 노선에 이미 등록되어있는 역을 등록 요청
     * Then 등록 요청 실패
     * When 지하철 노선에 등록되지 않은 역을 기준으로 등록
     * Then 등록 요청 실패
     * When 지하철 노선에 등록된 역 목록 조회 요청
     * Then 이전과 똑같은 역 목록이 조회됨
     */
    @DisplayName("지하철 구간 추가/삭제 실패")
    @Test
    void scenario2() {
        // when
        ExtractableResponse<Response> 지하철_노선에_지하철역_제외_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(지하철_노선에_지하철역_제외_응답);

        // when
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(지하철_노선에_지하철역_등록_응답);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);

        // when
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답, Arrays.asList(강남역, 광교역));
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(Long line, Long upStation,
        Long downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation, downStation, distance);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line)
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
        List<Long> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStations);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(Long line, Long station) {
        return RestAssured
            .given().log().all()
            .when().delete("/lines/{lineId}/sections?stationId={stationId}", line, station)
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
