package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineSectionStaticAcceptance.*;
import static nextstep.subway.line.acceptance.LineStaticAcceptance.*;
import static nextstep.subway.path.PathStaticAcceptance.*;
import static nextstep.subway.station.StationsStaticAcceptance.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.dto.StationResponse;

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
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(
			LineResponse.class);
		이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
		삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 남부터미널역.getId(), 5)).as(
			LineResponse.class);

		지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 4);
	}

	@DisplayName("지하철역 사이의 최단경로를 조회한다.")
	@Test
	void getShortestPath() {
		// given
		PathRequest 강남역_남부터미널역_최단경로_조회_요청값 = 최단경로_조회_요청(강남역.getId(), 남부터미널역.getId());

		// when
		ExtractableResponse<Response> response = 지하철역_사이의_최단경로를_조회한다(강남역_남부터미널역_최단경로_조회_요청값);

		// then
		지하철역_사이의_최단경로가_조회됨(response, 14);
		지하철역_사이의_경로가_조회됨(response, 강남역, 양재역, 남부터미널역);
	}

	@DisplayName("출발역과 도착역이 같은 경우 최단경로를 조회한다.")
	@Test
	void getShortestPathSameSourceTarget() {
		// given
		PathRequest 강남역_강남역_최단경로_조회_요청값 = 최단경로_조회_요청(강남역.getId(), 강남역.getId());

		// when역
		ExtractableResponse<Response> response = 지하철역_사이의_최단경로를_조회한다(강남역_강남역_최단경로_조회_요청값);

		// then
		지하철역_사이의_경로_조회가_실패됨(response);
	}

	@DisplayName("출발역과 도착역이 연결이 되어있지 않은 경로를 조회한다.")
	@Test
	void getNotConnectPathTargetSource() {
		// given
		StationResponse 서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
		PathRequest 강남역_서울역_최단경로_조회_요청값 = 최단경로_조회_요청(강남역.getId(), 서울역.getId());

		// when
		ExtractableResponse<Response> response = 지하철역_사이의_최단경로를_조회한다(강남역_서울역_최단경로_조회_요청값);

		// then
		지하철역_사이의_경로_조회가_실패됨(response);
	}

	@DisplayName("존재하지 않은 출발역이나 도착역으로 경로를 조회한다.")
	@Test
	void getPathNullTargetSource() {
		// given
		PathRequest 존재하지_않는역_최단경로_조회_요청값 = 최단경로_조회_요청(100L, 200L);

		// when
		ExtractableResponse<Response> response = 지하철역_사이의_최단경로를_조회한다(존재하지_않는역_최단경로_조회_요청값);

		// then
		지하철역_사이의_경로_조회가_실패됨(response);
	}
}
