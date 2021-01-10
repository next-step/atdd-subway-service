package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceSupport;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

	private static final int 기본요금 = 1250;
	private static final int 신분당선_요금 = 1000;
	private static final int 이호선_요금 = 0;
	private static final int 삼호선_요금 = 200;
	private static final int 할인_공제액 = 350;
	private static final double 할인율_청소년 = 0.2;
	private static final double 할인율_어린이 = 0.5;
	private static final int 금액부과거리_10KM초과 = 5;
	private static final int 금액부과거리_50KM초과 = 8;
	private static final int 부과단위당_금액 = 100;
	private StationResponse 강남역;
	private StationResponse 남부터미널역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 노원역;
	private StationResponse 건대입구역;
	private StationResponse 시청역;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;

	/**
	 * [지하철 노선도]
	 *
	 *             거리 4                  거리 20                 거리 34
	 * 교대역    --- *2호선* ---   강남역  --- *2호선* --- 건대입구역 --- *2호선 --- 시청역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * 거리 3                     거리 10
	 * |                        |
	 * 남부터미널역  --- *3호선* --- 양재
	 * 거리 4
	 */

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
		노원역 = StationAcceptanceTest.지하철역_등록되어_있음("노원역").as(StationResponse.class);
		건대입구역 = StationAcceptanceTest.지하철역_등록되어_있음("건대입구역").as(StationResponse.class);
		시청역 = StationAcceptanceTest.지하철역_등록되어_있음("시청역").as(StationResponse.class);

		신분당선 = PathAcceptanceSupport.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 신분당선_요금);
		이호선 = PathAcceptanceSupport.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 4, 이호선_요금);
		삼호선 = PathAcceptanceSupport.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 7, 삼호선_요금);

		PathAcceptanceSupport.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
		PathAcceptanceSupport.지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 건대입구역, 20);
		PathAcceptanceSupport.지하철_노선에_지하철역_등록되어_있음(이호선, 건대입구역, 시청역, 34);
	}

	@DisplayName("경로, 금액, 거리를 조회한다. 할인혜택 미적용")
	@Test
	void calculatePath_할인혜택미적용() {
		ExtractableResponse<Response> 경로조회결과 = PathAcceptanceSupport.지하철_경로_조회_요청(강남역, 남부터미널역);

		PathAcceptanceSupport.지하철_경로_조회됨(경로조회결과,
				Arrays.asList("강남역", "교대역", "남부터미널역"),
				7,
				기본요금 + 삼호선_요금);
	}

	@DisplayName("경로, 금액, 거리를 조회한다. 할인혜택 미적용. 10km 초과하여 추가요금 적용")
	@Test
	void calculatePath_할인혜택미적용_10km초과() {
		ExtractableResponse<Response> 경로조회결과 = PathAcceptanceSupport.지하철_경로_조회_요청(교대역, 건대입구역);

		final int 예상거리 = 24;
		final int 추가요금 = Math.floorDiv(예상거리, 금액부과거리_10KM초과) * 부과단위당_금액;
		PathAcceptanceSupport.지하철_경로_조회됨(경로조회결과,
				Arrays.asList("교대역", "강남역", "건대입구역"),
				예상거리,
				기본요금 + 이호선_요금 + 추가요금);
	}

	@DisplayName("경로, 금액, 거리를 조회한다. 할인혜택 미적용. 50km 초과하여 추가요금 적용")
	@Test
	void calculatePath_할인혜택미적용_50km초과() {
		ExtractableResponse<Response> 경로조회결과 = PathAcceptanceSupport.지하철_경로_조회_요청(교대역, 시청역);

		final int 예상거리 = 58;
		final int 추가요금 = Math.floorDiv(예상거리, 금액부과거리_50KM초과) * 부과단위당_금액;
		PathAcceptanceSupport.지하철_경로_조회됨(경로조회결과,
				Arrays.asList("교대역", "강남역", "건대입구역", "시청역"),
				예상거리,
				기본요금 + 이호선_요금 + 추가요금);
	}

	@DisplayName("경로, 금액, 거리를 조회한다. 청소년 할인")
	@Test
	void calculatePath_청소년할인() {
		// given
		MemberAcceptanceSupport.회원_생성을_요청("e", "p", 17);
		String 청소년 = MemberAcceptanceSupport.회원_로그인_요청("e", "p");
		ExtractableResponse<Response> 경로조회결과 = PathAcceptanceSupport.지하철_경로_조회_요청_회원용(청소년, 강남역, 남부터미널역);
		final int 할인액 = (int) (Math.floor(기본요금 + 삼호선_요금 - 할인_공제액) * 할인율_청소년);

		// when then
		PathAcceptanceSupport.지하철_경로_조회됨(경로조회결과,
				Arrays.asList("강남역", "교대역", "남부터미널역"),
				7,
				기본요금 + 삼호선_요금 - 할인액);
	}

	@DisplayName("경로, 금액, 거리를 조회한다. 청소년 할인. 50km 초과하여 추가요금 적용")
	@Test
	void calculatePath_청소년할인_50km초과() {
		MemberAcceptanceSupport.회원_생성을_요청("e", "p", 17);
		String 청소년 = MemberAcceptanceSupport.회원_로그인_요청("e", "p");
		ExtractableResponse<Response> 경로조회결과 = PathAcceptanceSupport.지하철_경로_조회_요청_회원용(청소년, 교대역, 시청역);

		final int 예상거리 = 58;
		final int 추가요금 = Math.floorDiv(예상거리, 금액부과거리_50KM초과) * 부과단위당_금액;
		final int 할인액 = (int) (Math.floor(기본요금 + 이호선_요금 + 추가요금 - 할인_공제액) * 할인율_청소년);
		PathAcceptanceSupport.지하철_경로_조회됨(경로조회결과,
				Arrays.asList("교대역", "강남역", "건대입구역", "시청역"),
				예상거리,
				기본요금 + 이호선_요금 + 추가요금 - 할인액);
	}

	@DisplayName("경로, 금액, 거리를 조회한다. 어린이 할인")
	@Test
	void calculatePath_어린이할인() {
		// given
		MemberAcceptanceSupport.회원_생성을_요청("e", "p", 8);
		String 청소년 = MemberAcceptanceSupport.회원_로그인_요청("e", "p");
		ExtractableResponse<Response> 경로조회결과 = PathAcceptanceSupport.지하철_경로_조회_요청_회원용(청소년, 강남역, 남부터미널역);
		final int 할인액 = (int) (Math.floor(기본요금 + 삼호선_요금 - 할인_공제액) * 할인율_어린이);

		// when then
		PathAcceptanceSupport.지하철_경로_조회됨(경로조회결과,
				Arrays.asList("강남역", "교대역", "남부터미널역"),
				7,
				기본요금 + 삼호선_요금 - 할인액);
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
