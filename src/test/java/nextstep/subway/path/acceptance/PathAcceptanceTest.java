package nextstep.subway.path.acceptance;

import static nextstep.subway.common.ServiceApiFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestApiFixture;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
	private long 신분당선_ID;
	private long 이호선_ID;
	private long 삼호선_ID;
	private long 강남역_ID;
	private long 양재역_ID;
	private long 교대역_ID;
	private long 남부터미널역_ID;

	/**
	 * 교대역 ------- *2호선(10)* ------- 강남역
	 * |                        		|
	 * *3호선(3)*              		*신분당선(10)*
	 * |                        		|
	 * 남부터미널역 ---- *3호선(2)* ---- 양재역
	 */
	@BeforeAll
	public void setUp() {
		super.setUp();

		강남역_ID = createStationId("강남역");
		양재역_ID = createStationId("양재역");
		교대역_ID = createStationId("교대역");
		남부터미널역_ID = createStationId("남부터미널역");

		신분당선_ID = createLineId("신분당선", "bg-red-600", 강남역_ID, 양재역_ID, 10);
		이호선_ID = createLineId("이호선", "bg-red-600", 교대역_ID, 강남역_ID, 10);
		삼호선_ID = createLineId("삼호선", "bg-red-600", 교대역_ID, 양재역_ID, 5);

		postSections(삼호선_ID, sectionRequest(교대역_ID, 남부터미널역_ID, 3));
	}

	// [outside->in] happy case 만 고려
	@Test
	void findShortestPath() {
		final ExtractableResponse<Response> response = getPaths(강남역_ID, 남부터미널역_ID);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		final PathResponse pathResponse = response.as(PathResponse.class);
		final List<Long> actualStationIds = pathResponse.getStations()
			.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());
		assertThat(actualStationIds).containsExactly(강남역_ID, 양재역_ID, 남부터미널역_ID);
		assertThat(pathResponse.getDistance()).isEqualTo(12);
	}

	private ExtractableResponse<Response> getPaths(long source, long target) {
		return RestApiFixture.response(RestApiFixture.request()
			.queryParam("source", source)
			.queryParam("target", target)
			.get("/paths")
		);
	}
}
