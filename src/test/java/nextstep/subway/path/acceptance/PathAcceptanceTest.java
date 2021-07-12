package nextstep.subway.path.acceptance;

import static nextstep.subway.path.acceptance.PathTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionTestMethod;
import nextstep.subway.line.acceptance.LineTestMethod;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("최단 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 20);
		LineRequest lineRequest2 = new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 8);
		LineRequest lineRequest3 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 10);

		신분당선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);
		이호선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest2).as(LineResponse.class);
		삼호선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest3).as(LineResponse.class);

		LineSectionTestMethod.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 6);
	}

	@DisplayName("미환승역에서 환승역으로 최단거리 시나리오")
	@Test
	void findShortestPathFromNotTransferToTransferScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음
		// And : 지하철 노선 등록되어 있음
		// And : 지하철 노선에 지하철역 등록되어 있음

		// Scenario : 미환승역에서 환승역으로 최단거리 조회
		// When : 미환승역에서 환승역으로 최단거리 조회
		ExtractableResponse<Response> shortestPathResponse1 = findPath(남부터미널역.getId(), 강남역.getId());
		// Then : 해당 역 리스트 리턴
		PathResponse path1 = shortestPathResponse1.as(PathResponse.class);
		List<Long> stationIds1 = getIds(path1.getStations());
		List<Long> expectedStationIds = getIds(Arrays.asList(남부터미널역, 교대역, 강남역));

		assertThat(stationIds1).containsExactlyElementsOf(expectedStationIds);
		assertThat(path1.getDistance()).isEqualTo(14);
		assertThat(path1.getFare()).isEqualTo(1350);
	}

	@DisplayName("환승역에서 미환승역으로 최단거리 시나리오")
	@Test
	void findShortestPathFromTransferToNotTransferScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음
		// And : 지하철 노선 등록되어 있음
		// And : 지하철 노선에 지하철역 등록되어 있음

		// Scenario : 환승역에서 미환승역으로 최단거리조회
		// When : 환승역에서 미환승역으로 최단거리 조회
		ExtractableResponse<Response> shortestPathResponse2 = findPath(강남역.getId(), 남부터미널역.getId());

		// Then : 해당 역 리스트 리턴
		PathResponse path2 = shortestPathResponse2.as(PathResponse.class);
		List<Long> stationIds2 = getIds(path2.getStations());
		List<Long> expectedStationIds2 = getIds(Arrays.asList(강남역, 교대역, 남부터미널역));

		assertThat(stationIds2).containsExactlyElementsOf(expectedStationIds2);
		assertThat(path2.getDistance()).isEqualTo(14);
		assertThat(path2.getFare()).isEqualTo(1350);
	}

	@DisplayName("환승역에서 환승역으로 최단거리 시나리오")
	@Test
	void findShortestPathFromTransferToTransferScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음
		// And : 지하철 노선 등록되어 있음
		// And : 지하철 노선에 지하철역 등록되어 있음

		// Scenario : 환승역에서 환승역으로 최단거리 조회
		// When : 환승역에서 환승역으로 최단거리 조회
		ExtractableResponse<Response> shortestPathResponse3 = findPath(교대역.getId(), 양재역.getId());
		// Then : 해당 역 리스트 리턴
		PathResponse path3 = shortestPathResponse3.as(PathResponse.class);
		List<Long> stationIds3 = getIds(path3.getStations());
		List<Long> expectedStationIds3 = getIds(Arrays.asList(교대역, 남부터미널역, 양재역));

		assertThat(stationIds3).containsExactlyElementsOf(expectedStationIds3);
		assertThat(path3.getDistance()).isEqualTo(10);
		assertThat(path3.getFare()).isEqualTo(1250);
	}

	@DisplayName("최단거리와 역방향 조회 시나리오")
	@Test
	void findReverseShortestPathScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음
		// And : 지하철 노선 등록되어 있음
		// And : 지하철 노선에 지하철역 등록되어 있음

		// Scenario : 최단거리의 역방향의 최단거리 조회
		// When : 최단거리의 역방향의 최단거리 조회
		ExtractableResponse<Response> shortestPathResponse4 = findPath(교대역.getId(), 양재역.getId());
		ExtractableResponse<Response> shortestPathResponse5 = findPath(양재역.getId(), 교대역.getId());

		// Then : 각각의 최단거리는 같음, 각각의 최단경로는 같음
		PathResponse path4 = shortestPathResponse4.as(PathResponse.class);
		PathResponse path5 = shortestPathResponse5.as(PathResponse.class);

		assertThat(path4.getDistance()).isEqualTo(path5.getDistance());
	}

	@DisplayName("최단 경로 조회 시 오류 시나리오")
	@Test
	void findShortestPathErrorScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음
		// And : 지하철 노선 등록되어 있음
		// And : 지하철 노선에 지하철역 등록되어 있음

		// Scenario : 최단 경로 조회 시 오류 시나리오
		// When : 출발역과 도착역이 같은 경우
		ExtractableResponse<Response> shortestPathResponse1 = findPath(남부터미널역.getId(), 남부터미널역.getId());
		// Then : 에러 발생
		assertThat(shortestPathResponse1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 출발역과 도착역이 연결되어 있지 않음
		StationResponse 신촌역 = StationAcceptanceTest.지하철역_등록되어_있음("신촌역").as(StationResponse.class);
		ExtractableResponse<Response> shortestPathResponse2 = findPath(남부터미널역.getId(), 신촌역.getId());

		// Then : 에러 발생
		assertThat(shortestPathResponse2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 도착역이 존재하지 않음
		ExtractableResponse<Response> shortestPathResponse3 = findPath(남부터미널역.getId(), -1L);
		// Then : 에러 발생
		assertThat(shortestPathResponse3.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 출발역이 존재하지 않음
		ExtractableResponse<Response> shortestPathResponse4 = findPath(-1L, 남부터미널역.getId());
		// Then : 에러 발생
		assertThat(shortestPathResponse4.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
