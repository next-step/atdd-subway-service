package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.StationAcceptanceTest;
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
	 * 교대역    --- *2호선*(10) ---   강남역
	 * |                        |
	 * *3호선(3)*                   *신분당선(10)*
	 * |                        |
	 * 남부터미널역  --- *3호선*(3) ---   양재
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

		지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("최단 경로 조회")
	@Test
	void findShortestPath(){
		ExtractableResponse<Response> response = 최단경로_조회(교대역, 양재역);

		최단경로_응답(response, 교대역, 남부터미널역, 양재역);
	}


	private ExtractableResponse<Response> 최단경로_조회(StationResponse upStation, StationResponse downStation) {
		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/paths?source={sourceId}&target={targetId}",upStation.getId(), downStation.getId())
			.then().log().all().extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		return response;
	}

	private void 최단경로_응답(ExtractableResponse<Response> response, StationResponse... stationResponses) {

		List<Long> pathStationIds = response.jsonPath().getList("stations", PathStationResponse.class).stream()
			.map(PathStationResponse::getId)
			.collect(Collectors.toList());

		List<Long> actualStationIds = Arrays.stream(stationResponses)
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(pathStationIds).containsAll(actualStationIds);
	}


	private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation,
		int distance) {
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
	}

	private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
		StationResponse downStation,
		int distance) {
		LineRequest request = LineRequest.builder()
			.name(name)
			.color(color)
			.upStationId(upStation.getId())
			.downStationId(downStation.getId())
			.distance(distance)
			.build();

		return LineAcceptanceTest.지하철_노선_등록되어_있음(request).as(LineResponse.class);
	}
}
