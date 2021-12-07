package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

/**
 *  교대역 --------3-----------강남역 --6---역삼역 --5---선릉역 (2호선)
 *     --\                   |
 *        -5-                |
 *           남부터미널역       2
 *                --         |
 *                  \-2-     |
 *                      \-- 양재역 (3호선)
 *                           |
 *                           |
 *                           |
 *                           4
 *                           |
 *                           |
 *                           |
 *  중앙역--2--한대앞역      양재시민의숲역
 * (4호선)                  (신분당선)
 */
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
	private StationResponse 교대역;
	private StationResponse 강남역;
	private StationResponse 역삼역;
	private StationResponse 선릉역;
	private StationResponse 남부터미널역;
	private StationResponse 양재역;
	private StationResponse 양재시민의숲역;
	private StationResponse 중앙역;
	private StationResponse 한대앞역;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private LineResponse 사호선;

	@BeforeEach
	public void setUp() {
		super.setUp();

		// Background
		교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
		선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
		남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
		양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		양재시민의숲역 = 지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);
		중앙역 = 지하철역_등록되어_있음("중앙역").as(StationResponse.class);
		한대앞역 = 지하철역_등록되어_있음("한대앞역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 2);
		이호선 = 지하철_노선_등록되어_있음("2호선", "bg-green-600", 교대역, 강남역, 3);
		삼호선 = 지하철_노선_등록되어_있음("3호선", "bg-orange-600", 교대역, 남부터미널역, 5);
		사호선 = 지하철_노선_등록되어_있음("4호선", "bg-blue-600", 중앙역, 한대앞역, 2);

		지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 양재시민의숲역, 4);
		지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 6);
		지하철_노선에_지하철역_등록되어_있음(이호선, 역삼역, 선릉역, 5);
		지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 2);
	}

	@DisplayName("경로를 찾는다.")
	@Test
	void findPath() {
		// Scenario
		ExtractableResponse<Response> 지하철_경로_조회_요청 = 지하철_경로_조회_요청(양재시민의숲역, 선릉역);
		지하철_경로_조회됨(지하철_경로_조회_요청);
		지하철_경로에_지하철역_순서_정렬됨(지하철_경로_조회_요청, Arrays.asList(양재시민의숲역, 양재역, 강남역, 역삼역, 선릉역));
		지하철_경로에_거리가_조회됨(지하철_경로_조회_요청, 17);
		지하철_경로에_요금이_조회됨(지하철_경로_조회_요청, 1450);
	}

	@DisplayName("출발역과 도착역이 같은 경우 경로를 찾을 수 없다.")
	@Test
	void findPathFailOnSame() {
		// Scenario
		지하철_경로_조회_실패됨(지하철_경로_조회_요청(강남역, 강남역), HttpStatus.BAD_REQUEST);
	}

	@DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 경로를 찾을 수 없다.")
	@Test
	void findPathFailOnNotConnected() {
		// Scenario
		지하철_경로_조회_실패됨(지하철_경로_조회_요청(강남역, 한대앞역), HttpStatus.BAD_REQUEST);
	}

	@DisplayName("출발역이나 도착역이 존재하지 않는 경우 경로를 찾을 수 없다.")
	@Test
	void findPathFailOnNotExist() {
		// Scenario
		지하철_경로_조회_실패됨(지하철_경로_조회_요청(강남역.getId(), UNKNOWN_ID), HttpStatus.BAD_REQUEST);
	}

	public static ExtractableResponse<Response> 지하철_경로_조회_요청(
		StationResponse sourceStation,
		StationResponse targetStation
	) {
		return 지하철_경로_조회_요청(sourceStation.getId(), targetStation.getId());
	}

	public static ExtractableResponse<Response> 지하철_경로_조회_요청(
		Long sourceStationId,
		Long targetStationId
	) {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("source", sourceStationId);
		queryParams.put("target", targetStationId);

		return get("/paths", new HashMap<>(), queryParams);
	}

	private void 지하철_경로_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 지하철_경로에_지하철역_순서_정렬됨(
		ExtractableResponse<Response> response,
		List<StationResponse> expectedStations
	) {
		PathResponse path = response.as(PathResponse.class);

		List<Long> actualIds = path.getStations()
			.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());
		List<Long> expectedIds = expectedStations.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(actualIds).containsExactlyElementsOf(expectedIds);
	}

	private void 지하철_경로에_거리가_조회됨(
		ExtractableResponse<Response> response,
		int expectedDistance
	) {
		PathResponse path = response.as(PathResponse.class);
		int actualDistance = path.getDistance();

		assertThat(actualDistance).isEqualTo(expectedDistance);
	}

	private void 지하철_경로에_요금이_조회됨(
		ExtractableResponse<Response> response,
		int expectedFare
	) {
		PathResponse path = response.as(PathResponse.class);
		int actualFare = path.getFare();

		assertThat(actualFare).isEqualTo(expectedFare);
	}

	private void 지하철_경로_조회_실패됨(ExtractableResponse<Response> response, HttpStatus expectedHttpStatus) {
		assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
	}
}
