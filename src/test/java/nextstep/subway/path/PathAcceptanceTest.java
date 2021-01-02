package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.BaseTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends BaseTest {
	public static final String 어른_이메일 = "adult@test.com";
	public static final String 어른_패스워드 = "adult1234";
	public static final String 청소년_이메일 = "youth@test.com";
	public static final String 청소년_패스워드 = "youth1234";
	public static final String 어린이_이메일 = "child@test.com";
	public static final String 어린이_패스워드 = "child1234";

	public static final long 존재하지않는_역_ID = 999L;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private LineResponse 칠호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private StationResponse 고속버스터미널역;
	private StationResponse 대화역;
	private StationResponse 부평구청역;
	private StationResponse 장암역;
	private StationResponse 역삼역;
	private String 어른_토큰;
	private String 청소년_토큰;
	private String 어린이_토큰;

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
		고속버스터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("고속버스터미널역").as(StationResponse.class);
		대화역 = StationAcceptanceTest.지하철역_등록되어_있음("대화역").as(StationResponse.class);
		부평구청역 = StationAcceptanceTest.지하철역_등록되어_있음("부평구청역").as(StationResponse.class);
		장암역 = StationAcceptanceTest.지하철역_등록되어_있음("장암역").as(StationResponse.class);
		역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 500);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 0);
		칠호선 = 지하철_노선_등록되어_있음("칠호선", "bg-red-600", 부평구청역, 장암역, 100, 0);

		LineSectionAcceptanceTest.지하철_구간을_등록_요청(이호선, 강남역, 역삼역, 5);
		LineSectionAcceptanceTest.지하철_구간을_등록_요청(삼호선, 교대역, 남부터미널역, 3);
		LineSectionAcceptanceTest.지하철_구간을_등록_요청(삼호선, 고속버스터미널역, 교대역, 12);
		LineSectionAcceptanceTest.지하철_구간을_등록_요청(삼호선, 대화역, 고속버스터미널역, 59);

		MemberAcceptanceTest.회원_생성을_요청(어른_이메일, 어른_패스워드, 30);
		MemberAcceptanceTest.회원_생성을_요청(청소년_이메일, 청소년_패스워드, 18);
		MemberAcceptanceTest.회원_생성을_요청(어린이_이메일, 어린이_패스워드, 12);
		어른_토큰 = AuthAcceptanceTest.로그인_요청(어른_이메일, 어른_패스워드).as(TokenResponse.class).getAccessToken();
		청소년_토큰 = AuthAcceptanceTest.로그인_요청(청소년_이메일, 청소년_패스워드).as(TokenResponse.class).getAccessToken();
		어린이_토큰 = AuthAcceptanceTest.로그인_요청(어린이_이메일, 어린이_패스워드).as(TokenResponse.class).getAccessToken();
	}

	@DisplayName("경로 검색 화면: 출발역, 도착역을 지정해서 조회하면 최단 경로 상에 포함된 역과 거리정보를 볼 수 있다.")
	@Test
	void shortestPath() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(강남역, 남부터미널역);

		// then
		최단_경로_검색_정상_조회됨(response);
		최단_경로_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 남부터미널역));
		최단_거리_조회됨(response, 12);
		최단_경로_요금_조회됨(response, 2250);
	}

	@DisplayName("경로 검색 화면: 동일한 출발역, 도착역을 지정해서 조회하면 실패한다.")
	@Test
	void shortestPathSameStation() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(강남역, 강남역);

		// then
		최단_경로_검색_실패됨(response);
	}

	@DisplayName("경로 검색 화면: 노선이 연결되어 있지 않은 역을 지정해서 조회하면 실패한다.")
	@Test
	void shortestPathNotConnected() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(남부터미널역, 부평구청역);

		// then
		최단_경로_검색_실패됨(response);
	}

	@DisplayName("경로 검색 화면: 존재하지 않는 출발역을 조회하면 실패한다.")
	@Test
	void shortestPathWrongStartStation() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청_by_id(존재하지않는_역_ID, 남부터미널역.getId());

		// then
		최단_경로_검색_실패됨(response);
	}

	@DisplayName("경로 검색 화면: 존재하지 않는 도착역을 조회하면 실패한다.")
	@Test
	void shortestPathWrongDestStation() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청_by_id(남부터미널역.getId(), 존재하지않는_역_ID);

		// then
		최단_경로_검색_실패됨(response);
	}

	@DisplayName("경로 검색 화면: 교대역과 남부터미널역 사이의 요금은 3km이므로 1250원이 나온다.")
	@Test
	void shortestPathDistanceFare1() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(교대역, 남부터미널역);

		// then
		최단_거리가_10이하다(response);
		최단_경로_요금_조회됨(response, 1250);
	}

	@DisplayName("경로 검색 화면: 남부터미널역과 고속버스터미널역 사이의 요금은 15km이므로 1350원이 나온다.")
	@Test
	void shortestPathDistanceFare2() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(남부터미널역, 고속버스터미널역);

		// then
		최단_거리가_10과_50사이다(response);
		최단_경로_요금_조회됨(response, 1350);
	}

	@DisplayName("경로 검색 화면: 남부터미널역과 대화역 사이의 요금은 74km이므로 2350원이 나온다.")
	@Test
	void shortestPathDistanceFare3() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(남부터미널역, 대화역);

		// then
		최단_거리가_50초과다(response);
		최단_경로_요금_조회됨(response, 2350);
	}

	@DisplayName("경로 검색 화면: 양재역에서 역삼역을 거치면 신분당선 추가요금 900과 2호선 추가요금 500중에 신분당선 추가요금이 합해져 2250원이 나온다.")
	@Test
	void shortestPathExtra() {
		// when
		ExtractableResponse<Response> response = 최단_경로_검색_요청(양재역, 역삼역);

		// then
		최단_경로_검색_정상_조회됨(response);
		최단_경로_요금_조회됨(response, 2250);
	}

	@DisplayName("경로 검색 화면:어린이로 로그인해 출발역, 도착역을 지정해서 조회하면 어린이 요금으로 할인된 금액을 확인할 수 있다.")
	@Test
	void shortestPathChild() {
		// when
		ExtractableResponse<Response> response = 토큰포함해_최단_경로_검색_요청(어린이_토큰, 강남역, 남부터미널역);

		// then
		최단_경로_요금_조회됨(response, 1300);
	}

	@DisplayName("경로 검색 화면:청소년으로 로그인해 출발역, 도착역을 지정해서 조회하면 청소년 요금으로 할인된 금액을 확인할 수 있다.")
	@Test
	void shortestPathYouth() {
		// when
		ExtractableResponse<Response> response = 토큰포함해_최단_경로_검색_요청(청소년_토큰, 강남역, 남부터미널역);

		// then
		최단_경로_요금_조회됨(response, 1870);
	}

	@DisplayName("경로 검색 화면:어른으로 로그인해 출발역, 도착역을 지정해서 조회하면 할인받을 수 없다.")
	@Test
	void shortestPathAdult() {
		// when
		ExtractableResponse<Response> response = 토큰포함해_최단_경로_검색_요청(어른_토큰, 강남역, 남부터미널역);

		// then
		최단_경로_요금_조회됨(response, 2250);
	}

	private void 최단_경로_검색_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 최단_경로_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
		PathResponse path = response.as(PathResponse.class);
		List<Long> stationIds = path.getStations().stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		List<Long> expectedStationIds = expectedStations.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);

	}

	private void 최단_거리_조회됨(ExtractableResponse<Response> response, int distance) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getDistance()).isEqualTo(distance);
	}

	private void 최단_거리가_10이하다(ExtractableResponse<Response> response) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getDistance()).isLessThanOrEqualTo(10);
	}

	private void 최단_거리가_10과_50사이다(ExtractableResponse<Response> response) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getDistance()).isGreaterThan(10);
		assertThat(path.getDistance()).isLessThanOrEqualTo(50);
	}

	private void 최단_거리가_50초과다(ExtractableResponse<Response> response) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getDistance()).isGreaterThan(50);
	}

	private void 최단_경로_요금_조회됨(ExtractableResponse<Response> response, int fare) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getFare()).isEqualTo(fare);
	}

	private void 최단_경로_검색_정상_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 최단_경로_검색_요청(StationResponse sourceStation, StationResponse targetStation) {
		return 최단_경로_검색_요청_by_id(sourceStation.getId(), targetStation.getId());
	}

	private ExtractableResponse<Response> 최단_경로_검색_요청_by_id(Long sourceStationId, Long targetStationId) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(String.format("/paths?source=%d&target=%d", sourceStationId, targetStationId))
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 토큰포함해_최단_경로_검색_요청(String accessToken, StationResponse sourceStation, StationResponse targetStation) {
		return 토큰포함해_최단_경로_검색_요청_by_id(accessToken, sourceStation.getId(), targetStation.getId());
	}

	private ExtractableResponse<Response> 토큰포함해_최단_경로_검색_요청_by_id(String accessToken,
		Long sourceStationId, Long targetStationId) {
		return RestAssured
			.given().log().all().auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(String.format("/paths?source=%d&target=%d", sourceStationId, targetStationId))
			.then().log().all()
			.extract();
	}

	private LineResponse 지하철_노선_등록되어_있음(
		String lineName,
		String color,
		StationResponse upStation,
		StationResponse downStation,
		int distance,
		int extraFare) {

		LineRequest lineRequest = LineRequest.of(lineName, color, upStation.getId(), downStation.getId(), distance, extraFare);
		return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

	}
}
