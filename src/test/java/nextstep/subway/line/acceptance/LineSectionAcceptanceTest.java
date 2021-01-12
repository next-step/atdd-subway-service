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
import static nextstep.subway.line.acceptance.LineSectionAcceptanceSupport.*;

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

		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 0);
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
	}

	@DisplayName("구간 등록시 잘못된 역을 지정할 경우 실패한다.")
	@Test
	void addLineSection_Failed() {
		// 이미 등록되어 있는 역 등록
		final ExtractableResponse<Response> 강남_광교_등록결과 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 10);
		지하철_노선에_지하철역_등록_실패됨(강남_광교_등록결과);

		// 역 둘 다 노선에 없을 때 등록
		final ExtractableResponse<Response> 정자_양재_등록결과 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 10);
		지하철_노선에_지하철역_등록_실패됨(정자_양재_등록결과);

		// 결국 아무 역이 등록되지 않고 처음 그대로인 상태가 된다
		final ExtractableResponse<Response> 신분당선_조회 = 지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_순서_정렬됨(신분당선_조회, Arrays.asList(강남역, 광교역));
	}

	@DisplayName("구간 등록, 삭제를 한다.")
	@Test
	void manageLineSection() {
		// 정자 - 강남 - 양재 - 광교 가 되도록 등록
		final ExtractableResponse<Response> 강남_양재_등록결과 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
		final ExtractableResponse<Response> 정자_강남_등록결과 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);
		지하철_노선에_지하철역_등록됨(강남_양재_등록결과);
		지하철_노선에_지하철역_등록됨(정자_강남_등록결과);
		지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(정자역, 강남역, 양재역, 광교역));

		// 정자 - 강남 만 남도록 구간을 제거한다
		final ExtractableResponse<Response> 양재역_삭제결과 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
		final ExtractableResponse<Response> 광교역_삭제결과 = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);
		지하철_노선에_지하철역_제외됨(양재역_삭제결과);
		지하철_노선에_지하철역_제외됨(광교역_삭제결과);
		지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(정자역, 강남역));

		// 2개 남았을때 구간 제거하면 실패
		final ExtractableResponse<Response> 강남역_삭제결과 = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
		지하철_노선에_지하철역_제외_실패됨(강남역_삭제결과);
		지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(정자역, 강남역));
	}
}
