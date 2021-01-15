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
import nextstep.subway.AcceptanceTest;
import nextstep.subway.common.dto.ErrorResponse;
import nextstep.subway.common.exception.DuplicateSourceAndTargetException;
import nextstep.subway.common.exception.NotConnectedLineException;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("최단 경로 조회")
	@Test
	void findShortestPath() {
		ExtractableResponse<Response> response = 최단경로_조회(교대역, 양재역);

		최단경로_조회_확인(response, 교대역, 남부터미널역, 양재역);
	}

	@DisplayName("출발역과 도착역이 같은 경우")
	@Test
	void sameSourceAndTarget() {
		ExtractableResponse<Response> response = 최단경로_조회(교대역, 교대역);

		출발역과_도착역_동일_에러(response);
	}

	@DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
	@Test
	void sourceStationAndTargetStationNotConnectedException() {
		StationResponse 서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
		ExtractableResponse<Response> response = 최단경로_조회(교대역, 서울역);

		출발역과_도착역_연결되어있지_않음(response);
	}

	@DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
	@Test
	void findNotExistStation() {
		StationResponse 서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
		ExtractableResponse<Response> response = 최단경로_조회(교대역, 서울역);

		출발역과_도착역_연결되어있지_않음(response);
	}

	private void 출발역과_도착역_연결되어있지_않음(ExtractableResponse<Response> response) {
		String errorCode = response.jsonPath().getObject(".", ErrorResponse.class).getErrorCode();
		assertThat(errorCode).isEqualTo(NotConnectedLineException.ERROR_CODE);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 출발역과_도착역_동일_에러(ExtractableResponse<Response> response) {
		String errorCode = response.jsonPath().getObject(".", ErrorResponse.class).getErrorCode();
		assertThat(errorCode).isEqualTo(DuplicateSourceAndTargetException.ERROR_CODE);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 최단경로_조회_확인(ExtractableResponse<Response> response, StationResponse... stationResponses) {
		List<Long> actualStationIds = response.jsonPath().getList("stations", PathStationResponse.class).stream()
			.map(PathStationResponse::getId)
			.collect(Collectors.toList());

		List<Long> expectedStationIds = Arrays.stream(stationResponses)
			.map(stationResponse -> stationResponse.getId())
			.collect(Collectors.toList());

		assertThat(actualStationIds).containsAll(expectedStationIds);
	}

	private ExtractableResponse<Response> 최단경로_조회(StationResponse sourceStation, StationResponse targetStation) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source=" + sourceStation.getId() + " &target=" + targetStation.getId())
			.then().log().all().extract();
		return response;

	}

	public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
		StationResponse downStation, int distance) {
		LineRequest lineRequest = LineRequest.builder()
			.name(name)
			.color(color)
			.upStationId(upStation.getId())
			.downStationId(downStation.getId())
			.distance(distance)
			.build();
		return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
	}

}
