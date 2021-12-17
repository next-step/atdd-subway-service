package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Test
	@DisplayName("최단 경로를 조회한다.")
	void findPath() {
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 남부터미널역, 5);
		지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 3);

		ExtractableResponse<Response> response = 최단_경로_요청(교대역, 양재역);

		최단_경로_요청_성공(response);
		최단_경로_맞는지_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역));
	}

	private void 최단_경로_맞는지_확인(ExtractableResponse<Response> response, List<StationResponse> expected) {
		assertThat(response.jsonPath().getList(".stations", StationResponse.class)).isEqualTo(expected);
	}

	private void 최단_경로_요청_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 최단_경로_요청(StationResponse station1, StationResponse station2) {
		Map<String, String> findPathQueries = new HashMap<>();
		findPathQueries.put("source", station1.getId().toString());
		findPathQueries.put("target", station2.getId().toString());

		return RestAssured.given().log().all()
			.queryParams(findPathQueries)
			.when().log().all()
			.get("/paths")
			.then().log().all()
			.extract();
	}

	private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation,
		int distance) {
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
	}

	private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
		StationResponse downStation, int distance) {
		LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
		return LineAcceptanceTest.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
	}
}
