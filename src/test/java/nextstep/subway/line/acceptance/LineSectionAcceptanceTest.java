package nextstep.subway.line.acceptance;

import static nextstep.subway.utils.LineSectionAcceptanceTestUtils.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("지하철 구간 관련 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 신사역;
    private StationResponse 신논현역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        신사역 = StationAcceptanceTest.지하철역_등록되어_있음("신사역").as(StationResponse.class);
        신논현역 = StationAcceptanceTest.지하철역_등록되어_있음("신논현역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("새로운 역 등록시 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 12})
    void addStationBySameAndGraterThenDistance(int distance) {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 정자역, distance);
        ExtractableResponse<Response> response1 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 정자역, distance);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
        지하철_노선에_지하철역_등록_실패됨(response1);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        // then
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));
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

    @DisplayName("노선 내 존재하지 않는 역은 제거할 수 없다.")
    @Test
    void deleteStationNotInSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_제외_요청(신분당선, 신논현역);

        // when
        지하철_노선에_지하철역_제외_실패됨(response);
    }

    @Test
    @DisplayName("노선 내 구간이 하나만 존재할 경우 역을 제거할 수 없다.")
    void deleteStationInOneSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);

        // when
        지하철_노선에_지하철역_제외_실패됨(response);
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

    /**
     *  Feature: 지하철 구간 관련 기능
     *
     *  Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *
     *  Scenario: 지하철 구간을 관리
     *     When 지하철 구간 등록 요청
     *     Then 지하철 구간 등록됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
     *     When 지하철 구간 삭제 요청
     *     Then 지하철 구간 삭제됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
     */
    @TestFactory
    @DisplayName("지하철 구간 관련 기능 통합 인수 테스트")
    Collection<DynamicTest> lineSectionAcceptance() {
        return Arrays.asList(
            dynamicTest("새로운 구간 등록시 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 정자역, 10);
                ExtractableResponse<Response> response1 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 정자역, 11);

                // then
                지하철_노선에_지하철역_등록_실패됨(response);
                지하철_노선에_지하철역_등록_실패됨(response1);
            }),
            dynamicTest("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

                // then
                지하철_노선에_지하철역_등록_실패됨(response);
            }),
            dynamicTest("노선에 지하철 구간들을 등록한다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 4);
                ExtractableResponse<Response> response1 = 지하철_노선에_지하철역_등록_요청(신분당선, 신사역, 강남역, 7);
                ExtractableResponse<Response> response2 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 5);

                // then
                지하철_노선에_지하철역_등록됨(response);
                지하철_노선에_지하철역_등록됨(response1);
                지하철_노선에_지하철역_등록됨(response2);
            }),
            dynamicTest("지하철 노선에 이미 등록되어있는 역을 등록한다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 5);

                // then
                지하철_노선에_지하철역_등록_실패됨(response);
            }),
            dynamicTest("지하철 노선을 조회하면 등록된 순서대로 정렬되어 반환된다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);

                // then
                지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(신사역, 강남역, 양재역, 정자역, 광교역));
            }),
            dynamicTest("노선 내 존재하지 않는 역은 제거할 수 없다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_제외_요청(신분당선, 신논현역);

                // when
                지하철_노선에_지하철역_제외_실패됨(response);
            }),
            dynamicTest("노선에 등록된 역들을 제거한다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선에_지하철역_제외_요청(신분당선, 신사역);
                ExtractableResponse<Response> response1 = 지하철_노선에_지하철역_제외_요청(신분당선, 정자역);
                ExtractableResponse<Response> response2 = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);

                // then
                지하철_노선에_지하철역_제외됨(response);
                지하철_노선에_지하철역_제외됨(response1);
                지하철_노선에_지하철역_제외됨(response2);
            }),
            dynamicTest("노선 내 구간이 하나만 존재할 경우 역을 제거할 수 없다.", () -> {
                // when
                ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

                // when
                지하철_노선에_지하철역_제외_실패됨(removeResponse);
            }),
            dynamicTest("지하철 노선을 조회하면 삭제된 역은 존재하지 않는다.", () -> {
                // when
                ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);

                // then
                지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역));
            })
        );
    }
}
