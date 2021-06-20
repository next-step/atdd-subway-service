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

	@DisplayName("지하철 구간 등록 및 삭제 시나리오")
	@Test
	void lineSectionAddAndRemoveScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음
		// And : 지하철 노선에 지하철역 등록되어 있음

		// Scenario : 지하철 구간 등록 및 삭제 시나리오
		// When : 지하철 구간 등록 요청
		ExtractableResponse<Response> lineSectionAddResponse1 = LineSectionTestMethod.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
		ExtractableResponse<Response> lineSectionAddResponse2 = LineSectionTestMethod.지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);
		// Then : 지하철 구간 등록됨
		LineSectionTestMethod.지하철_노선에_지하철역_등록됨(lineSectionAddResponse1);
		LineSectionTestMethod.지하철_노선에_지하철역_등록됨(lineSectionAddResponse2);
		// When : 지하철 노선에 등록된 역 목록 조회 요청
		ExtractableResponse<Response> findLineResponse1 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		// Then : 등록한 지하철 구간이 반영된 역 목록이 조회됨
		LineSectionTestMethod.지하철_노선에_지하철역_등록됨(findLineResponse1);
		LineSectionTestMethod.지하철_노선에_지하철역_순서_정렬됨(findLineResponse1, Arrays.asList(정자역, 강남역, 양재역, 광교역));
		// When : 지하철 구간 삭제 요청
		ExtractableResponse<Response> removeResponse = LineSectionTestMethod.지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
		// Then : 지하철 구간 삭제됨
		LineSectionTestMethod.지하철_노선에_지하철역_제외됨(removeResponse);
		// When : 지하철 노선에 등록된 역 목록 조회 요청
		ExtractableResponse<Response> findLineResponse2 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		LineSectionTestMethod.지하철_노선에_지하철역_순서_정렬됨(findLineResponse2, Arrays.asList(정자역, 강남역, 광교역));
	}

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = LineSectionTestMethod.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then
		LineSectionTestMethod.지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = LineSectionTestMethod.지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then
		LineSectionTestMethod.지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = LineSectionTestMethod.지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
		LineSectionTestMethod.지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }
}
