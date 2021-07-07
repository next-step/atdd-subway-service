package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재
	 */
	@DisplayName("최단 경로 조회")
	@Test
	void 최단_경로_조회() {
		//given
		StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		StationResponse 양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		StationResponse 교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		StationResponse 남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		LineResponse 신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
		LineResponse 이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
		LineResponse 삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

		// when
		ExtractableResponse<Response> response = 최단_경로_조회_요청(양재역, 교대역);

		// then
		최단_경로_조회_응답됨(response);
		최단_경로_조회_검증_역목록(response);
		최단_경로_조회_검증_거리(response);
		최단_경로_조회_검증_요금(response);
	}

	private void 최단_경로_조회_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 최단_경로_조회_검증_요금(ExtractableResponse<Response> response) {
		int fare = response.jsonPath().getInt("fare");
		assertThat(fare).isEqualTo(5_000);
	}

	private void 최단_경로_조회_검증_거리(ExtractableResponse<Response> response) {
		int distance = response.jsonPath().getInt("distance");
		assertThat(distance).isEqualTo(5);
	}

	private void 최단_경로_조회_검증_역목록(ExtractableResponse<Response> response) {
		List<StationResponse> stationResponses = response.jsonPath().getList("stations", StationResponse.class);
		assertThat(stationResponses).extracting("name").containsExactly("교대역", "남부터미널역", "양재역");
	}

	private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse 양재역, StationResponse 교대역) {
		return RestAssured
			.given().log().all()
			.when().get("/paths?source={source}&target={target}", 교대역.getId(), 양재역.getId())
			.then().log().all().extract();
	}

}
