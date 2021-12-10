package nextstep.subway.line.acceptance;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 관리한다.")
    @Test
    void manageLineSection() {
        // when
        ExtractableResponse<Response> createResponse1 = LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록됨(createResponse1);

        // when
        ExtractableResponse<Response> createResponse2 = LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 5);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록됨(createResponse2);

        // when
        ExtractableResponse<Response> getResponse1 = LineAcceptanceTestHelper.지하철_노선_조회_요청(신분당선);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_순서_정렬됨(getResponse1, Arrays.asList(강남역, 양재역, 정자역, 광교역));

        // when
        ExtractableResponse<Response> removeResponse = LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_제외됨(removeResponse);

        // when
        ExtractableResponse<Response> getResponse2 = LineAcceptanceTestHelper.지하철_노선_조회_요청(신분당선);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_순서_정렬됨(getResponse2, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        LineSectionAcceptanceTestHelper.지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }
}
