package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.PathStationResponse;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
	 * 교대역    ---   *2호선* (10)  ---   강남역
	 * |                                  |
	 * *3호선* (3)                     *신분당선* (10)
	 * |                                  |
	 * 남부터미널역  ---  *3호선* (2)   ---   양재
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
		이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
		삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("최단 경로를 조회한다.")
	@Test
	void addLineSection() {
		//given
		List<StationResponse> expectedStations = Arrays.asList(교대역, 남부터미널역, 양재역);
		Long expectedDistance = 3L + 2;

		// when
		ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(new PathRequest(교대역.getId(), 양재역.getId()));

		// then
		지하철_최단_경로_조회됨(response);
		지하철_최단_경로에_지하철역_포함됨(response, expectedStations);
		지하철_최단_경로_거리_계산됨(response, expectedDistance);
	}

	private ExtractableResponse<Response> 지하철_최단_경로_조회_요청(PathRequest request) {
		return RestAssured.given().log().all()
		        .contentType(MediaType.APPLICATION_JSON_VALUE)
		        .when().get(String.format("/paths?source=%d&target=%d", request.getSource(), request.getTarget()))
		        .then().log().all().extract();
	}

	private void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 지하철_최단_경로에_지하철역_포함됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
		PathResponse path = response.as(PathResponse.class);
		List<Long> stationIds = path.getStations().stream()
			.map(PathStationResponse::getId)
			.collect(Collectors.toList());

		List<Long> expectedStationIds = expectedStations.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
	}

	private void 지하철_최단_경로_거리_계산됨(ExtractableResponse<Response> response, Long expectedDistance) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getDistance()).isEqualTo(expectedDistance);
	}
}
