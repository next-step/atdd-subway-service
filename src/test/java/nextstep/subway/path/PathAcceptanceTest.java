package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
	private static StationResponse 교대역;
	private static StationResponse 강남역;
	private static StationResponse 선릉역;
	private static StationResponse 양재역;
	private static StationResponse 도곡역;
	private static StationResponse 천안역;

	@BeforeEach
	public void setUp() {
		super.setUp();
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		도곡역 = StationAcceptanceTest.지하철역_등록되어_있음("도곡역").as(StationResponse.class);
		천안역 = StationAcceptanceTest.지하철역_등록되어_있음("천안역").as(StationResponse.class);

		LineResponse 이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
			new LineRequest("이호선", "green", 교대역.getId(), 선릉역.getId(), 20)).as(LineResponse.class);
		LineResponse 삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
			new LineRequest("삼호선", "orange", 교대역.getId(), 도곡역.getId(), 11)).as(LineResponse.class);
		LineResponse 신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
			new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 1)).as(LineResponse.class);
		LineResponse 수인분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
			new LineRequest("수인분당선", "yellow", 선릉역.getId(), 도곡역.getId(), 10)).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 강남역, 10);
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 양재역, 1);

		/**
		 * 교대역 - *2호선* - 강남역 --- 선릉역
		 *   |                |         |
		 *   |           *신분당선*   *수인분당선*
		 *   |               |          |
		 *   └ㅡ   *3호선*   양재역  --- 도곡역
		 */
	}

	@Test
	@DisplayName("지하철 노선 경로 조회 성공")
	public void findPathSuccessTest() {
		//given
		//when
		ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청("/paths?source=1&target=3");

		//then
		지하철_노선_경로_조회_성공(response);
		지하철_노선_경로_조회_응답데이터_검증_성공(response);
	}

	@Test
	@DisplayName("지하철 노선 경로조회 출발역과 도착역이 동일해서 실패")
	public void findPathFailStartEqualsEndTest() {
		//given
		//when
		ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청("/paths?source=1&target=1");

		//then
		지하철_노선_경로_조회_실패(response);
	}

	@Test
	@DisplayName("지하철 노선 경로조회 출발역과 도착역이 존재하지 않을 경우 실패")
	public void findPathFailNoneStationsTest() {
		//given
		//when
		ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청("/paths?source=1&target=8");

		//then
		지하철_노선_경로_조회_실패(response);
	}

	@Test
	@DisplayName("지하철 노선 경로조회 출발역과 도착역이 연결되지 않은 경우 실패")
	public void findPathFailNoneSectionsTest() {
		//given
		//when
		ExtractableResponse<Response> response = 지하철_노선_경로_조회_요청("/paths?source=1&target=6");

		//then
		지하철_노선_경로_조회_실패(response);
	}

	private void 지하철_노선_경로_조회_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static void 지하철_노선_경로_조회_응답데이터_검증_성공(ExtractableResponse<Response> response) {
		PathResponse pathResponse = response.as(PathResponse.class);
		assertThat(pathResponse.getStations()).containsExactly(교대역, 양재역, 강남역, 선릉역);
		assertThat(pathResponse.getDistance()).isEqualTo(12);
	}

	public static void 지하철_노선_경로_조회_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static ExtractableResponse<Response> 지하철_노선_경로_조회_요청(String uri) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(uri)
			.then().log().all()
			.extract();
	}

}
