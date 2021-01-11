package nextstep.subway.path;

import org.assertj.core.api.Assertions;
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

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5);

		지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역);
	}

	@DisplayName("지하철역 경로조회: 교대역 -> 양재역")
	@Test
	void findShortPathTest() {
		// given //when
		ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(교대역, 양재역);

		// then
		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 지하철_노선_경로탐색_요청(StationResponse source, StationResponse target) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
			.then().log().all().extract();
		return response;
	}

	private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation) {
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, 3);
	}

	private LineResponse 지하철_노선_등록되어_있음(String line, String color, Long upId, Long downId, int distance) {

		return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(line, color, upId, downId, distance))
			.as(LineResponse.class);
	}
}
