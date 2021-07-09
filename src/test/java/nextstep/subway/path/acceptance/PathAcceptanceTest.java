package nextstep.subway.path.acceptance;

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

		LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		LineRequest lineRequest2 = new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 4);
		LineRequest lineRequest3 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5);

		신분당선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);
		이호선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest2).as(LineResponse.class);
		삼호선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest3).as(LineResponse.class);

		LineSectionTestMethod.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
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
		ExtractableResponse<Response> shortestPathResponse1 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 남부터미널역.getId() + "&target=" + 강남역.getId())
			.then().log().all()
			.extract();
		// Then : 해당 역 리스트 리턴
		PathResponse path1 = shortestPathResponse1.as(PathResponse.class);
		List<String> stationNames = path1.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		List<String> expectedStationNames = Arrays.asList(남부터미널역, 교대역, 강남역).stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
		assertThat(path1.getDistance()).isEqualTo(7);
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
		ExtractableResponse<Response> shortestPathResponse2 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 강남역.getId() + "&target=" + 남부터미널역.getId())
			.then().log().all()
			.extract();
		// Then : 해당 역 리스트 리턴
		PathResponse path2 = shortestPathResponse2.as(PathResponse.class);
		List<Long> stationIds2 = path2.getStations().stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		List<Long> expectedStationIds2 = Arrays.asList(강남역, 교대역, 남부터미널역).stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		assertThat(stationIds2).containsExactlyElementsOf(expectedStationIds2);
		assertThat(path2.getDistance()).isEqualTo(7);
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
		ExtractableResponse<Response> shortestPathResponse3 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 교대역.getId() + "&target=" + 양재역.getId())
			.then().log().all()
			.extract();
		// Then : 해당 역 리스트 리턴
		PathResponse path3 = shortestPathResponse3.as(PathResponse.class);
		List<Long> stationIds3 = path3.getStations().stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		List<Long> expectedStationIds3 = Arrays.asList(양재역, 남부터미널역, 교대역).stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		assertThat(stationIds3).containsExactlyElementsOf(expectedStationIds3);
		assertThat(path3.getDistance()).isEqualTo(5);
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
		ExtractableResponse<Response> shortestPathResponse4 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 교대역.getId() + "&target=" + 양재역.getId())
			.then().log().all()
			.extract();
		ExtractableResponse<Response> shortestPathResponse5 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 양재역.getId() + "&target=" + 교대역.getId())
			.then().log().all()
			.extract();
		// Then : 각각의 최단거리는 같음, 각각의 최단경로는 같음
		PathResponse path4 = shortestPathResponse4.as(PathResponse.class);
		List<Long> stationIds4 = path4.getStations().stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		PathResponse path5 = shortestPathResponse5.as(PathResponse.class);
		List<Long> stationIds5 = path5.getStations().stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		assertThat(stationIds4).containsExactlyElementsOf(stationIds5);
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
		ExtractableResponse<Response> shortestPathResponse1 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 남부터미널역.getId() + "&target=" + 남부터미널역.getId())
			.then().log().all()
			.extract();
		// Then : 에러 발생
		assertThat(shortestPathResponse1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 출발역과 도착역이 연결되어 있지 않음
		StationResponse 신촌역 = StationAcceptanceTest.지하철역_등록되어_있음("신촌역").as(StationResponse.class);
		ExtractableResponse<Response> shortestPathResponse2 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 남부터미널역.getId() + "&target=" + 신촌역.getId())
			.then().log().all()
			.extract();
		// Then : 에러 발생
		assertThat(shortestPathResponse2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 출발역이 존재하지 않음
		ExtractableResponse<Response> shortestPathResponse3 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + 남부터미널역.getId() + "&target=-1")
			.then().log().all()
			.extract();
		// Then : 에러 발생
		assertThat(shortestPathResponse3.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 도착역이 존재하지 않음
		ExtractableResponse<Response> shortestPathResponse4 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=-1&target=" + 남부터미널역.getId())
			.then().log().all()
			.extract();
		// Then : 에러 발생
		assertThat(shortestPathResponse4.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
