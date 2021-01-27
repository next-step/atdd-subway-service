package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private LineResponse 구호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private StationResponse 당산역;
	private StationResponse 여의도역;
	private String accessToken;

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
		당산역 = StationAcceptanceTest.지하철역_등록되어_있음("당산역").as(StationResponse.class);
		여의도역 = StationAcceptanceTest.지하철역_등록되어_있음("여의도역").as(StationResponse.class);

		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900).as(LineResponse.class);
		이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 0).as(LineResponse.class);
		삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 0).as(LineResponse.class);
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
		구호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("구호선", "bg-red-600", 당산역, 여의도역, 5, 0).as(LineResponse.class);

		MemberAcceptanceTest.회원_등록되어_있음("test@gmail.com", "test123", 10);
		accessToken = AuthAcceptanceTest.로그인_되어_있음("test@gmail.com", "test123");
	}

	@DisplayName("최단 경로를 조회 한다.")
	@Test
	void findShortestPath() {
		Long sourceId = 남부터미널역.getId();
		Long targetId = 강남역.getId();

		ExtractableResponse<Response> response = 최단_거리_경로_조회_요청(accessToken, sourceId, targetId);

		최단_거리_경로_조회됨(response);
		최단_거리_경로_검증됨(response);
	}

	@DisplayName("출발역과 도착역이 같은 경우 500 에러가 발생한다.")
	@Test
	void findShortestPathWhenSameSourceAndTarget() {
		Long sourceId = 강남역.getId();
		Long targetId = 강남역.getId();

		ExtractableResponse<Response> response = 최단_거리_경로_조회_요청(accessToken, sourceId, targetId);

		최단_거리_경로_조회_실패(response);
	}

	@DisplayName("출발역과 도착역이 연결 되어 있지 않은 경우 400 에러가 발생한다.")
	@Test
	void findShortestPathWhenUnconnectedSourceAndTarget() {
		Long sourceId = 강남역.getId();
		Long targetId = 당산역.getId();

		ExtractableResponse<Response> response = 최단_거리_경로_조회_요청(accessToken, sourceId, targetId);

		최단_거리_경로_조회_실패(response);
	}

	@DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우 400 에러가 발생한다.")
	@Test
	void findShortestPathWhenNoneExistentSourceOrTarget() {
		Long sourceId = 강남역.getId();
		Long targetId = 10L;

		ExtractableResponse<Response> response = 최단_거리_경로_조회_요청(accessToken, sourceId, targetId);

		최단_거리_경로_조회_실패(response);
	}

	public static ExtractableResponse<Response> 최단_거리_경로_조회_요청(String accessToken, Long sourceId, Long targetId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.queryParam("source", sourceId)
			.queryParam("target", targetId)
			.when().get("/paths")
			.then().log().all()
			.extract();
	}

	public static void 최단_거리_경로_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public void 최단_거리_경로_검증됨(ExtractableResponse<Response> response) {
		PathResponse pathResponse = response.as(PathResponse.class);
		assertThat(pathResponse.getStations()).hasSize(3).contains(남부터미널역, 양재역, 강남역);
		assertThat(pathResponse.getDistance()).isEqualTo(12);
		assertThat(pathResponse.getFare()).isEqualTo(950);
	}

	public static void 최단_거리_경로_조회_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
