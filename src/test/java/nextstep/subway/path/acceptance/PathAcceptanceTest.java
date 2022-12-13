package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선*10 ---   강남역
     * |                             |
     * *3호선*3                     *신분당선*10
     * |                             |
     * 남부터미널역  --- *3호선*2 ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    /*
    Feature: 지하철 경로 조회 관련 기능

    Background
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음

    Scenario: 지하철 경로 검색(Happy Case)

    When 교대역-양재역 최단경로 조회
    Then 교대역-남부터미널역-양재역 조회
    When 강남역-남부터미널역 최단경로 조회
    Then 강남역-양재역-남부터미널역 최단 경로 조회
    */
    @Test
    @DisplayName("지하철 경로를 조회한다.(Happy Case)")
    void manageSearchLinePathHappyCase() {
        // when
        ExtractableResponse<Response> 교대역_양재역_경로_조회 = 경로_조회_요청(교대역, 양재역);

        // then
        경로_조회됨(교대역_양재역_경로_조회);
        경로_조회_졍렬됨(교대역_양재역_경로_조회, Arrays.asList(교대역, 남부터미널역, 양재역));

        // when
        ExtractableResponse<Response> 강남역_남부터미널역_경로_조회 = 경로_조회_요청(강남역, 남부터미널역);

        // then
        경로_조회됨(강남역_남부터미널역_경로_조회);
        경로_조회_졍렬됨(강남역_남부터미널역_경로_조회, Arrays.asList(강남역, 양재역, 남부터미널역));

    }


    /*
    Feature: 지하철 경로 조회 관련 기능

    Background
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음

    Scenario: 지하철 경로 검색(Bad Case)

    When 교대역-교대역 최단경로 조회
    Then 최단경로 검색 실패
    When 출발역과 도착역이 연결되어 있지 않은 최단경로 조회(천안역, 교대역 최단경로 조회)
    Then 최단경로 검색 실패
    When 존재하지 않은 출발역이나 도착역을 최단경로 조회
    Then 최단경로 검색 실패
    */
    @Test
    @DisplayName("지하철 경로를 조회한다.(Bad Case)")
    void manageSearchLinePathBadCase() {
        // given
        StationResponse 천안역 = 지하철역_등록되어_있음("천안역").as(StationResponse.class);
        StationResponse 온양온천역 = 지하철역_등록되어_있음("온양온천역").as(StationResponse.class);
        LineResponse 일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-red-600", 천안역.getId(), 온양온천역.getId(), 10)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> 교대역_교대역_조회 = 경로_조회_요청(교대역, 교대역);

        // then
        경로_조회_실패됨(교대역_교대역_조회);

        // when
        ExtractableResponse<Response> 천안역_교대역_조회 = 경로_조회_요청(천안역, 교대역);

        // then
        경로_조회_실패됨(천안역_교대역_조회);

        // when
        ExtractableResponse<Response> 미존재역_교대역_조회 = 경로_조회_요청(new StationResponse(0L, "미존재역", LocalDateTime.now(), LocalDateTime.now()), 교대역);

        // then
        경로_조회_실패됨(미존재역_교대역_조회);

    }

    @Test
    @DisplayName("지하철 경로를 조회한다.")
    void manageFindPath() {

        // when
        ExtractableResponse<Response> 강남역_남부터미널역_경로_조회 = 경로_조회_요청(강남역, 남부터미널역);

        // then
        경로_조회됨(강남역_남부터미널역_경로_조회);
        경로_조회_졍렬됨(강남역_남부터미널역_경로_조회, Arrays.asList(강남역, 양재역, 남부터미널역));

    }

    @Test
    @DisplayName("지하철 경로를 실패한다. - 출발역과 도착역이 동일한 경우")
    void manageFindSameStation() {

        // when
        ExtractableResponse<Response> 교대역_교대역_조회 = 경로_조회_요청(교대역, 교대역);

        // then
        경로_조회_실패됨(교대역_교대역_조회);

    }

    @Test
    @DisplayName("지하철 경로를 실패한다. - 출발역과 도착역이 연결되어 있지 않아 경로를 찾을 수 없음.")
    void manageFindNoPath() {

        // given
        StationResponse 천안역 = 지하철역_등록되어_있음("천안역").as(StationResponse.class);
        StationResponse 온양온천역 = 지하철역_등록되어_있음("온양온천역").as(StationResponse.class);
        LineResponse 일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-red-600", 천안역.getId(), 온양온천역.getId(), 10)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> 천안역_교대역_조회 = 경로_조회_요청(천안역, 교대역);

        // then
        경로_조회_실패됨(천안역_교대역_조회);

    }

    @Test
    @DisplayName("지하철 경로를 실패한다. - 존재하지 않은 지하철역이 있음.")
    void manageFindNoStation() {
        // when
        ExtractableResponse<Response> 미존재역_교대역_조회 = 경로_조회_요청(new StationResponse(0L, "미존재역", LocalDateTime.now(), LocalDateTime.now()), 교대역);

        // then
        경로_조회_실패됨(미존재역_교대역_조회);

    }

    private void 경로_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 경로_조회_졍렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> stationIds = pathResponse.getStationResponseList().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private ExtractableResponse<Response> 경로_조회_요청(StationResponse startStation, StationResponse endStation) {
        return RestAssured
                .given().log().all()
                .when().get(String.format("/paths?source=%d&target=%d", startStation.getId(), endStation.getId()))
                .then().log().all()
                .extract();
    }

    private void 경로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
