package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.testfactory.LineAcceptanceTestFactory.*;
import static nextstep.subway.line.acceptance.testfactory.LineSectionTestFactory.*;
import static nextstep.subway.station.testfactory.StationAcceptanceTestFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.ui.PathController;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

	private static final String BASE_PATH="/paths";

	@MockBean
	private PathController pathController;

	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;

	/**
	 * 교대역    --- *2호선*(10) ------   강남역
	 * |                           |
	 * *3호선*(3)                *신분당선*(10)
	 * |                          |
	 * 남부터미널역  --- *3호선*(2) ---   양재
	 */

	@BeforeEach
	public void setUp() {
		super.setUp();

		// given
		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

		지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("두 지하철 역 사이의 최단거리 경로 조회")
	@Test
	void getShortestPath() {

		//given
		when(pathController.findShortestPath(any(),any())).thenReturn(ResponseEntity.ok(
			PathResponse.of(5, new ArrayList<>(Arrays.asList(교대역,남부터미널역,양재역)))));

		// when
		PathResponse response = 최단거리_경로_요청(교대역,양재역).as(PathResponse.class);

		// then
		assertThat(response.getDistance()).isEqualTo(5);
		assertThat(response.getStations()).containsExactlyElementsOf(Arrays.asList(교대역,남부터미널역,양재역));
	}

	public ExtractableResponse<Response> 최단거리_경로_요청(StationResponse departStation, StationResponse arriveStation) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(BASE_PATH+"?source={departStationId}&target={arriveStationId}", departStation.getId(),arriveStation.getId())
			.then().log().all()
			.extract();
	}
}
