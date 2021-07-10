package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.StationAcceptanceTest;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     */
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


    /**
     * Scenario: 지하철 구간을 관리
     * When 지하철 구간 등록 요청
     * Then 지하철 구간 등록됨
     * When 지하철 노선에 등록된 역 목록 조회 요청
     * Then 등록한 지하철 구간이 반영된 역 목록에 포함됨
     * When 지하철 구간 삭제 요청
     * Then 지하철 구간 삭제됨
     * When 지하철 노선에 등록된 역 목록 조회 요청
     * Then 조회된 역 목록에서 삭제한 지하철 구간의 역이 포함되지 않음
     */
    @Test
    @DisplayName("지하철 구간을 관리")
    void managed_section() {
        // When
        ExtractableResponse<Response> 지하철_구간_등록_결과 = 지하철_구간_등록_요청(신분당선.getId(),
                new SectionRequest(강남역.getId(), 양재역.getId(), 3));
        // Then
        지하철_구간_등록됨(지하철_구간_등록_결과);

        // When
        ExtractableResponse<Response> 지하철_노선에_등록된_역_목록_조회_결과 = 지하철_노선에_등록된_역_목록_조회(신분당선.getId());
        // Then
        등록한_지하철_구간이_반영된_역_목록에_포함됨(지하철_노선에_등록된_역_목록_조회_결과, Arrays.asList(강남역, 양재역));

        // When
        ExtractableResponse<Response> 구간삭제_요청_결과 = 지하철_구간_삭제_요청(신분당선.getId(), 양재역.getId());
        // Then
        지하철_구간_삭제됨(구간삭제_요청_결과);

        // When
        ExtractableResponse<Response> 삭제된_구간이_반영된_노선의_역_목록_조회_결과 = 지하철_노선에_등록된_역_목록_조회(신분당선.getId());
        // Then
        조회된_역_목록에서_삭제한_지하철_구간의_역이_포함되지_않음(삭제된_구간이_반영된_노선의_역_목록_조회_결과, 양재역);
    }

    /**
     * Scenario: 지하철 구간 관리 실패
     * When 이미 등록된 지하철 구간 등록 요청
     * Then 이미 등록된 지하철 구간 등록 실패됨
     * And 요청 실패 메시지 확인됨
     * When 연결할 수 없는 지하철 구간 등록 요청
     * Then 연결할 수 없는 지하철 구간 등록 요청 실패됨
     * And 요청 실패 메시지 확인됨
     * When 마지막 남은 구간 삭제요청
     * Then 마지막 남은 구간 삭제요청 실패됨
     * And 요청 실패 메시지 확인됨
     * When 기존 구간의 거리를 넘는 구간 등록 요청1
     * Then 기존 구간의 거리를 넘는 구간 등록 요청 실패됨
     * And 요청 실패 메시지 확인됨
     * When 기존 구간의 거리를 넘는 구간 등록 요청2
     * Then 기존 구간의 거리를 넘는 구간 등록 요청 실패됨
     * And 요청 실패 메시지 확인됨
     */
    @Test
    @DisplayName("지하철 구간 관리 실패")
    void fail_scenario() {
        // when
        ExtractableResponse<Response> 등록되어_있는_구간등록_요청결과 = 이미_등록된_지하철_구간_등록_요청(신분당선.getId(), new SectionRequest(강남역.getId(), 광교역.getId(), 3));
        // then
        이미_등록된_지하철_구간_등록_실패됨(등록되어_있는_구간등록_요청결과);
        요청_실패_메시지_확인됨(등록되어_있는_구간등록_요청결과, "이미 등록된 구간 입니다.");

        // when
        ExtractableResponse<Response> 연결할_수_없는_구간등록_요청결과 = 연결할_수_없는_지하철_구간_등록_요청(신분당선.getId(), new SectionRequest(정자역.getId(), 양재역.getId(), 3));
        // then
        연결할_수_없는_지하철_구간_등록_요청_실패됨(연결할_수_없는_구간등록_요청결과);
        요청_실패_메시지_확인됨(연결할_수_없는_구간등록_요청결과, "등록할 수 없는 구간 입니다.");

        // when
        ExtractableResponse<Response> 마지막_구간_삭제요청_결과 = 마지막_남은_구간_삭제요청(신분당선.getId(), 강남역.getId());
        // then
        마지막_남은_구간_삭제요청_실패됨(마지막_구간_삭제요청_결과);
        요청_실패_메시지_확인됨(마지막_구간_삭제요청_결과, "지울 수 있는 구간이 없습니다.");

        // when
        ExtractableResponse<Response> 기존_구간의_거리를_넘는_구간등록_요청결과1 = 기존_구간의_거리를_넘는_구간_등록_요청1(신분당선.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 12));
        // then
        기존_구간의_거리를_넘는_구간_등록_요청_실패됨(기존_구간의_거리를_넘는_구간등록_요청결과1);
        요청_실패_메시지_확인됨(기존_구간의_거리를_넘는_구간등록_요청결과1, "역과 역 사이의 거리보다 좁은 거리를 입력해주세요");

        // when
        ExtractableResponse<Response> 기존_구간의_거리를_넘는_구간등록_요청결과2 = 기존_구간의_거리를_넘는_구간_등록_요청1(신분당선.getId(), new SectionRequest(정자역.getId(), 광교역.getId(), 12));
        // then
        기존_구간의_거리를_넘는_구간_등록_요청_실패됨(기존_구간의_거리를_넘는_구간등록_요청결과2);
        요청_실패_메시지_확인됨(기존_구간의_거리를_넘는_구간등록_요청결과2, "역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    private void 기존_구간의_거리를_넘는_구간_등록_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 기존_구간의_거리를_넘는_구간_등록_요청1(Long lineId, SectionRequest sectionRequest) {
        return 지하철_구간_등록_요청(lineId, sectionRequest);
    }

    private void 마지막_남은_구간_삭제요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 마지막_남은_구간_삭제요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when()
                .accept(MediaType.ALL_VALUE)
                .delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();
    }

    private void 연결할_수_없는_지하철_구간_등록_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 연결할_수_없는_지하철_구간_등록_요청(Long lineId, SectionRequest sectionRequest) {
        return 지하철_구간_등록_요청(lineId, sectionRequest);
    }

    private void 요청_실패_메시지_확인됨(ExtractableResponse<Response> response, String userErrorMessage) {
        String errorMessage = response.jsonPath().getObject("errorMessage", String.class);
        assertThat(errorMessage).isEqualTo(userErrorMessage);
    }

    private void 이미_등록된_지하철_구간_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 이미_등록된_지하철_구간_등록_요청(Long lineId, SectionRequest sectionRequest) {
        return 지하철_구간_등록_요청(lineId, sectionRequest);
    }

    private void 조회된_역_목록에서_삭제한_지하철_구간의_역이_포함되지_않음(ExtractableResponse<Response> response, StationResponse stationResponse) {
        List<String> list = new ArrayList<>(response.jsonPath().getList("stations.name", String.class));
        assertThat(list.contains(stationResponse.getName())).isFalse();
    }

    private void 등록한_지하철_구간이_반영된_역_목록에_포함됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses) {
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        List<String> collect = stationResponses.stream().map(StationResponse::getName).collect(Collectors.toList());
        assertThat(list.containsAll(collect)).isTrue();
    }

    private ExtractableResponse<Response> 지하철_노선에_등록된_역_목록_조회(Long lineId) {
        return LineAcceptanceTest.지하철_노선_목록_조회_요청(lineId);
    }

    private ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when()
                .accept(MediaType.ALL_VALUE)
                .delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .when()
                .body(sectionRequest)
                .accept(MediaType.ALL_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private void 지하철_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
