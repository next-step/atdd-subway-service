package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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

	private LineAcceptanceTest lineAcceptanceTest = new LineAcceptanceTest();
	private StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();
	private LineSectionAcceptanceTest lineSectionAcceptanceTest = new LineSectionAcceptanceTest();
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
	 * *3호선*                  *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선*  ---   양재
	 */
	@BeforeEach
	void pathSetUp() {
		super.setUp();

		강남역 = stationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = stationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = stationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = stationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = lineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 500))
			.as(LineResponse.class);
		이호선 = lineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 700))
			.as(LineResponse.class);
		삼호선 = lineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 1000))
			.as(LineResponse.class);
		lineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3);
	}

	@DisplayName("최단 경로 조회하기")
	@Test
	void 최단_경로_조회하기() {
		ExtractableResponse<Response> response = 최단_경로_조회_요청하기(강남역.getId(), 남부터미널역.getId());
		assertThat(response.body().jsonPath().getDouble("distance")).isEqualTo(12);
		assertThat(response.body().jsonPath().getList("stations").size()).isEqualTo(3);
		assertThat(response.body().jsonPath().getList("fee").size()).isEqualTo(1350);
	}

	@DisplayName("최단 경로 조회하기 - 출발역과 도착역이 같은 경우(에러 발생)")
	@Test
	void 최단_경로_조회하기_출발역과_도착역이_같은_경우() {
		ExtractableResponse<Response> response = 최단_경로_조회_요청하기(강남역.getId(), 강남역.getId());
		최단_경로_조회하기_실패(response);

	}

	@DisplayName("최단 경로 조회하기 - 출발역과 도착역이 연결이 되어 있지 않은 경우(에러 발생)")
	@Test
	void 최단_경로_조회하기_출발역과_도착역이_연결이_되어_있지_않은_경우() {
		StationResponse 서울역 = stationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
		StationResponse 신도림역 = stationAcceptanceTest.지하철역_등록되어_있음("신도림역").as(StationResponse.class);
		LineResponse 일호선 = lineAcceptanceTest.지하철_노선_등록되어_있음(
			new LineRequest("일호선", "bg-blue-600", 서울역.getId(), 신도림역.getId(), 5, 100)).as(LineResponse.class);

		ExtractableResponse<Response> response = 최단_경로_조회_요청하기(강남역.getId(), 서울역.getId());
		최단_경로_조회하기_실패(response);
	}

	@DisplayName("최단 경로 조회하기 - 존재하지 않은 출발역이나 도착역을 조회 할 경우(에러 발생)")
	@Test
	void 최단_경로_조회하기_존재하지_않은_출발역이나_도착역을_조회_할_경우() {
		ExtractableResponse<Response> response = 최단_경로_조회_요청하기(9L, 10L);
		최단_경로_조회하기_실패(response);
	}

	private ExtractableResponse<Response> 최단_경로_조회_요청하기(Long source, Long target) {
		return get("/paths?source=" + source + "&target=" + target);
	}

	private void 최단_경로_조회하기_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

}
