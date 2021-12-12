package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
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

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

		지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

	}

	@DisplayName("최단 경로를 조회한다")
	@Test
	void findPath() {
		// when
		ExtractableResponse<Response> response = 최단경로_조회_요청함(양재역, 교대역);

		// then
		최단경로_조회_응답함(response);
		최단경로_조회_경로_포함됨(response, Arrays.asList(양재역, 남부터미널역, 교대역), 5);

	}

	private void 최단경로_조회_경로_포함됨(ExtractableResponse<Response> response,
		List<StationResponse> expectedStations, int expectedDistance) {

		PathResponse path = response.as(PathResponse.class);

		List<Long> expectedIds = expectedStations.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		List<Long> resultIds = path.getStations().stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertAll(
			() -> assertThat(path.getDistance()).isEqualTo(expectedDistance),
			() -> assertThat(resultIds).containsAll(expectedIds)
		);
	}

	private void 최단경로_조회_응답함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 최단경로_조회_요청함(StationResponse source, StationResponse target) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
			.then().log().all()
			.extract();
	}

	private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse station1,
		StationResponse station2, int distance) {
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, station1, station2, distance);
	}

	private LineResponse 지하철_노선_등록되어_있음(String lineName, String lineColor, StationResponse station1,
		StationResponse station2, int distance) {
		LineRequest lineRequest = new LineRequest(lineName, lineColor, station1.getId(), station2.getId(), distance);
		return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
	}

}
