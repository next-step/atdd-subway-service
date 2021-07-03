package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_조회_요청;
import static nextstep.subway.utils.LineSectionRestAssuredUtils.*;

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

    @Test
    @DisplayName("Scenario: 지하철 구간을 관리")
    void manageStationSection() {
        ExtractableResponse<Response> createFailResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);
        지하철_노선에_지하철역_등록_실패됨(createFailResponse);

        ExtractableResponse<Response> createSuccessResponse = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
        지하철_노선에_지하철역_등록됨(createSuccessResponse);

        ExtractableResponse<Response> createSearchResponse = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(createSearchResponse, Arrays.asList(강남역, 양재역, 광교역));

        ExtractableResponse<Response> removeSuccessResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        지하철_노선에_지하철역_제외됨(removeSuccessResponse);

        ExtractableResponse<Response> removeSearchResponse = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(removeSearchResponse, Arrays.asList(양재역, 광교역));

        ExtractableResponse<Response> removeFailResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
        지하철_노선에_지하철역_제외_실패됨(removeFailResponse);
    }
}
