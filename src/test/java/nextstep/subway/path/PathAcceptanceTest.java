package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionTestAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponseDto;
import nextstep.subway.station.StationAcceptanceTest;
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
	private StationResponse 고매역;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
		고매역 = StationAcceptanceTest.지하철역_등록되어_있음("고매역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

		지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("지하철 최적경로를 조회힌다.")
	@Test
	void paths() {
		//given
		Map<String, Long> params = new HashMap<>();
		params.put("source", 강남역.getId());
		params.put("target", 남부터미널역.getId());

		//when
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .params(params)
			  .when().get("/paths")
			  .then().log().all()
			  .extract();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		PathResponseDto responseBody = response.body().as(PathResponseDto.class);
		assertThat(responseBody.getStations()).hasSize(3);
		assertThat(responseBody.getDistance()).isEqualTo(12);
	}

	@DisplayName("구간에 등록되지 않은 지하철 최적경로를 조회힌다.")
	@Test
	void pathsWithStationNotInSection() {
		//given
		Map<String, Long> params = new HashMap<>();
		params.put("source", 강남역.getId());
		params.put("target", 고매역.getId());

		//when
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .params(params)
			  .when().get("/paths")
			  .then().log().all()
			  .extract();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
		  StationResponse downStation, int distance) {
		ExtractableResponse<Response> response = LineAcceptanceTest
			  .지하철_노선_등록되어_있음(new LineRequest(name, color, upStation.getId(),
					downStation.getId(), distance));

		return response.body().as(LineResponse.class);
	}

	private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation,
		  StationResponse downStation, int distance) {
		LineSectionTestAcceptanceTest
			  .지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
	}
}

