package nextstep.subway.path.acceptance;

import static java.time.LocalDateTime.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private StationResponse 목동역;
	private StationResponse 오목교역;

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
		목동역 = 지하철역_등록되어_있음("목동역").as(StationResponse.class);
		오목교역 = 지하철역_등록되어_있음("오목교역").as(StationResponse.class);

		지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 1)).as(LineResponse.class);
		지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 1)).as(LineResponse.class);
		지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
		지하철_노선_등록되어_있음(new LineRequest("오호선", "bg-red-600", 목동역.getId(), 오목교역.getId(), 5)).as(LineResponse.class);
	}

	@DisplayName("지하철경로 최단 경로를 조회한다.")
	@Test
	void findPathTest() {
		// when
		ExtractableResponse<Response> response = 지하철경로_최단경로_조회(교대역.getId(), 양재역.getId());

		// then
		지하철경로_최단경로_조회됨(response);
		지하철경로_최단경로가_포함됨(response, 교대역.getId(), 강남역.getId(), 양재역.getId());
		지하철경로_최단경로의_거리가_포함됨(response, 2);
		지하철경로_최단경로의_요금이_포함됨(response, 1250);
	}

	@DisplayName("지하철경로 출발역과 도착역이 같은 경우를 조회한다")
	@Test
	void findPathWhenSourceEqualsTargetTest() {
		// when
		ExtractableResponse<Response> response = 지하철경로_최단경로_조회(교대역.getId(), 교대역.getId());

		// then
		지하철경로_최단경로_실패함(response);
	}

	@DisplayName("지하철경로 출발역과 도착역이 연결이 안된 경우를 조회한다")
	@Test
	void findPathWhenNotConnectedBetweenSourceAndTargetTest() {
		// when
		ExtractableResponse<Response> response = 지하철경로_최단경로_조회(교대역.getId(), 오목교역.getId());

		// then
		지하철경로_최단경로_실패함(response);
	}

	@DisplayName("지하철경로 출발역 또는 도착역이 존재하지 않은 경우를 조회한다")
	@Test
	void findPathWhenNowExistsSourceOrTargetTest() {
		// given
		StationResponse 목동역 = new StationResponse(10L, "목동역", now(), now());

		// when
		ExtractableResponse<Response> response = 지하철경로_최단경로_조회(교대역.getId(), 목동역.getId());

		// then
		지하철경로_최단경로_실패함(response);
	}
	
	private ExtractableResponse<Response> 지하철경로_최단경로_조회(Long srcStationId, Long desStationId) {
		return RestAssured
			.given().log().all()
			.when().get("/paths?source=" + srcStationId + "&target=" + desStationId)
			.then().log().all().extract();
	}

	private void 지하철경로_최단경로_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 지하철경로_최단경로가_포함됨(ExtractableResponse<Response> response, Long... expectedStationIds) {
		List<Long> resultStationIds = new ArrayList<>(response.jsonPath().getList("stations.id", Long.class));

		assertThat(resultStationIds).containsExactly(expectedStationIds);
	}

	private void 지하철경로_최단경로의_거리가_포함됨(ExtractableResponse<Response> response, int expectedDistance) {
		int resultDistance = response.jsonPath().getInt("distance");

		assertThat(resultDistance).isEqualTo(expectedDistance);
	}

	private void 지하철경로_최단경로의_요금이_포함됨(ExtractableResponse<Response> response, int expectedFare) {
		int resultFare = response.jsonPath().getInt("fare");

		assertThat(resultFare).isEqualTo(expectedFare);
	}

	private void 지하철경로_최단경로_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
