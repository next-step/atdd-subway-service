package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

	/**
	 *              거리 4
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * 거리 3                     거리 10
	 * |                        |
	 * 남부터미널역  --- *3호선* --- 양재
	 *                거리 2
	 */

	private StationResponse 강남역;
	private StationResponse 남부터미널역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 노원역;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
		노원역 = StationAcceptanceTest.지하철역_등록되어_있음("노원역").as(StationResponse.class);

		신분당선 = PathAcceptanceSupport.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = PathAcceptanceSupport.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 4);
		삼호선 = PathAcceptanceSupport.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

		PathAcceptanceSupport.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("경로를 조회한다.")
	@Test
	void calculatePath() {
		ExtractableResponse<Response> 경로조회결과 = PathAcceptanceSupport.지하철_경로_조회_요청(강남역, 남부터미널역);

		PathAcceptanceSupport.지하철_경로_조회됨(경로조회결과, Arrays.asList("강남역", "교대역", "남부터미널역"));
	}

	@DisplayName("경로 조회시 잘못된 역을 입력하면 실패한다.")
	@Test
	void calculatePath_Failed() {
		ExtractableResponse<Response> 경로조회_노선미포함 = PathAcceptanceSupport.지하철_경로_조회_요청(강남역, 노원역);
		PathAcceptanceSupport.지하철_경로_조회_실패함(경로조회_노선미포함);

		StationResponse 존재하지않는역 = new StationResponse(-11L, "", null, null);
		ExtractableResponse<Response> 경로조회_미존재역 = PathAcceptanceSupport.지하철_경로_조회_요청(강남역, 존재하지않는역);
		PathAcceptanceSupport.지하철_경로_조회_실패함(경로조회_미존재역);
	}
}
