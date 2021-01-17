package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


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
	 * (4)
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선* (5)             *신분당선*  (10)
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재
	 * (3)
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = this.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
		이호선 = this.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-400", 교대역.getId(), 강남역.getId(), 10));
		삼호선 = this.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-yellow-600", 교대역.getId(), 양재역.getId(), 5));


		SectionRequest sectionRequest = new SectionRequest(교대역.getId(), 남부터미널역.getId(), 3);
		ExtractableResponse<Response> responseExtractableResponse = 지하철_노선에_지하철역_등록되어_있음(삼호선.getId(), sectionRequest);


	}

	private static LineResponse 지하철_노선_등록되어_있음(LineRequest params) {
		return 지하철_노선_생성_요청(params);
	}

	private static LineResponse 지하철_노선_생성_요청(LineRequest params) {
		ExtractableResponse<Response> responseExtractableResponse = RestAssured
				.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(params)
				.when().post("/lines")
				.then().log().all().
						extract();

		LineResponse lineResponse = responseExtractableResponse.jsonPath().getObject(".", LineResponse.class);
		return lineResponse;
	}


	private ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(Long lineId, SectionRequest sectionRequest) {
		return RestAssured
				.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(sectionRequest)
				.when().post("/lines/{lineId}/sections", lineId)
				.then().log().all().
						extract();

	}

	@Test
	@DisplayName("최단 경로 조회")
	public void findShortestPath() {
		//http://0.0.0.0:8081/paths/?source=1&target=4
		ExtractableResponse<Response> response = RestAssured
				.given().log().all()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get(String.format("/paths?source=%d&target=%d", 1, 4))
				.then().log().all()
				.extract();

		PathResponse pathResponse = response.jsonPath().getObject(".", PathResponse.class);
		assertThat(pathResponse.getStations().size()).isEqualTo(3);
	}

	//		출발역과 도착역이 같은 경우
//		출발역과 도착역이 연결이 되어 있지 않은 경우
//		존재하지 않은 출발역이나 도착역을 조회 할 경우

}

