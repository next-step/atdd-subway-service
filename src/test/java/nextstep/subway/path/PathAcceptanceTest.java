package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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
	private StationResponse 인천역;

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
		인천역 = StationAcceptanceTest.지하철역_등록되어_있음("인천역").as(StationResponse.class);

		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10);
		삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("지하철역 교대역 -> 양재역 최단경로를 구한다.")
	@Test
	void findShortPathTest() {
		// given //when
		ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(교대역, 양재역);

		// then
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(PathResponse.class).getStations()).isNotNull(),
			() -> assertThat(response.as(PathResponse.class).getStations()).hasSize(3),
			() -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(5)
		);
	}

	@DisplayName("지하철역 같은역으로 최단경로를 구한다.")
	@Test
	void sameStationPathTest() {
		// given //when
		ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(교대역, 교대역);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	@DisplayName("지하철역 연결된 경로가 없는 경로를 구한다.")
	@Test
	void notExistPathTest() {
		// given //when
		ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(교대역, 인천역);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	@DisplayName("존재하지 않는 지하철로 경로를 구한다.")
	@Test
	void notExistStationTest() {
		// given
		StationResponse 주안역 = new StationResponse(99L, "주안역", LocalDateTime.now());

		// when
		ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(교대역, 주안역);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private ExtractableResponse<Response> 지하철_노선_경로탐색_요청(StationResponse source, StationResponse target) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
			.then().log().all().extract();
		return response;
	}
}
