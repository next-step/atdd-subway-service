package nextstep.subway.line.acceptance;

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
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given 지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        // and 지하철 노선 등록되어 있음
        // and 지하철 노선에 지하철역 등록되어 있음
        Map<String, String> lineRequest = LineAcceptanceTest.지하철_노선_생성_요청_파라미터("신분당선", "bg-red-600", 강남역.getId(),
            광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간 추가/삭제")
    @Test
    void scenario1() {
        // when 지하철 구간 등록 요청
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then 지하철 구간 등록됨
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_응답);

        // when 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_응답1 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_등록됨(지하철_노선_조회_응답1);
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답1, Arrays.asList(강남역, 양재역, 광교역));

        // when 지하철 구간 추가 등록 요청
        ExtractableResponse<Response> 지하철_노선에_지하철역_추가_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        // then 지하철 구간 등록됨
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_추가_등록_응답);

        // when 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_응답2 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_등록됨(지하철_노선_조회_응답2);
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답2, Arrays.asList(정자역, 강남역, 양재역, 광교역));

        // when 지하철 구간 삭제 요청
        ExtractableResponse<Response> 지하철_노선_지하철역_제외_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then 지하철 구간 삭제됨
        지하철_노선에_지하철역_제외됨(지하철_노선_지하철역_제외_응답);

        // when 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 광교역));
    }

    @DisplayName("지하철 구간 추가/삭제 실패")
    @Test
    void scenario2() {
        // when 지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외 요청
        ExtractableResponse<Response> 지하철_노선에_지하철역_제외_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then 제외 요청 실패
        지하철_노선에_지하철역_제외_실패됨(지하철_노선에_지하철역_제외_응답);

        // when 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_응답1 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then 이전과 똑같은 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답1, Arrays.asList(강남역, 광교역));

        // when 지하철 노선에 이미 등록되어있는 역을 등록 요청
        ExtractableResponse<Response> 지하철_노선에_지하철역_등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then 등록 요청 실패
        지하철_노선에_지하철역_등록_실패됨(지하철_노선에_지하철역_등록_응답);

        // when 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_응답2 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then 이전과 똑같은 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답2, Arrays.asList(강남역, 광교역));

        // when 지하철 노선에 등록되지 않은 역을 기준으로 등록
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then 등록 요청 실패
        지하철_노선에_지하철역_등록_실패됨(response);

        // when 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철_노선_조회_응답3 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // then 이전과 똑같은 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_응답3, Arrays.asList(강남역, 광교역));
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation,
        StationResponse downStation, int distance) {
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

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
        List<StationResponse> expectedStations) {
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
