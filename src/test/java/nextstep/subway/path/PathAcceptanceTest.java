package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.BaseTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends BaseTest {
	public static final long 존재하지않는_역_ID = 999L;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private LineResponse 칠호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private StationResponse 부평구청역;
	private StationResponse 장암역;

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

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
		부평구청역 = StationAcceptanceTest.지하철역_등록되어_있음("부평구청역").as(StationResponse.class);
		장암역 = StationAcceptanceTest.지하철역_등록되어_있음("장암역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 500);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 0);
		칠호선 = 지하철_노선_등록되어_있음("칠호선", "bg-red-600", 부평구청역, 장암역, 100, 0);

		LineSectionAcceptanceTest.지하철_구간을_등록_요청(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("경로 검색 화면: 출발역, 도착역을 지정해서 조회하면 최단 경로 상에 포함된 역과 거리정보를 볼 수 있다.")
	@Test
	void shortestPath() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(강남역, 남부터미널역);

		// then
		최단_경로_검색_정상_조회됨(response);
		최단_경로_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 남부터미널역));
		최단_경로_요금_조회됨(response, 2250);
	}

	@DisplayName("경로 검색 화면: 동일한 출발역, 도착역을 지정해서 조회하면 실패한다.")
	@Test
	void shortestPathSameStation() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(강남역, 강남역);

		// then
		최단_경로_검색_실패됨(response);
	}

	@DisplayName("경로 검색 화면: 노선이 연결되어 있지 않은 역을 지정해서 조회하면 실패한다.")
	@Test
	void shortestPathNotConnected() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(남부터미널역, 부평구청역);

		// then
		최단_경로_검색_실패됨(response);
	}

	@DisplayName("경로 검색 화면: 존재하지 않는 출발역을 조회하면 실패한다.")
	@Test
	void shortestPathWrongStartStation() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청_by_id(존재하지않는_역_ID, 남부터미널역.getId());

		// then
		최단_경로_검색_실패됨(response);
	}

	@DisplayName("경로 검색 화면: 존재하지 않는 도착역을 조회하면 실패한다.")
	@Test
	void shortestPathWrongDestStation() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청_by_id(남부터미널역.getId(), 존재하지않는_역_ID);

		// then
		최단_경로_검색_실패됨(response);
	}

	private void 최단_경로_검색_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 최단_경로_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
		PathResponse path = response.as(PathResponse.class);
		List<Long> stationIds = path.getStations().stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		List<Long> expectedStationIds = expectedStations.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);

	}

	private void 최단_경로_요금_조회됨(ExtractableResponse<Response> response, int fee) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getFee()).isEqualTo(fee);
	}

	private void 최단_경로_검색_정상_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 최단_경로_검색_요청(StationResponse sourceStation, StationResponse targetStation) {
		return 최단_경로_검색_요청_by_id(sourceStation.getId(), targetStation.getId());
	}

	private ExtractableResponse<Response> 최단_경로_검색_요청_by_id(Long sourceStationId, Long targetStationId) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(String.format("/paths?source=%d&target=%d", sourceStationId, targetStationId))
			.then().log().all()
			.extract();
	}

	private LineResponse 지하철_노선_등록되어_있음(
		String lineName,
		String color,
		StationResponse upStation,
		StationResponse downStation,
		int distance,
		int extraFee) {

		LineRequest lineRequest = LineRequest.of(lineName, color, upStation.getId(), downStation.getId(), distance, extraFee);
		return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

	}
}
