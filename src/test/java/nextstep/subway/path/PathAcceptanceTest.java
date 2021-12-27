package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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
public class PathAcceptanceTest extends AcceptanceTest {

	LineResponse 신분당선;
	LineResponse 이호선;
	LineResponse 삼호선;
	StationResponse 강남역;
	StationResponse 양재역;
	StationResponse 교대역;
	StationResponse 남부터미널역;
	StationResponse 서울역;
	StationResponse 옥수역;
	TokenResponse 성인;
	TokenResponse 청소년;
	TokenResponse 어린이;
	TokenResponse 비로그인;

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
		서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
		옥수역 = StationAcceptanceTest.지하철역_등록되어_있음("옥수역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 2, 900);
		이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 3, 1000);
		삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 10, 1100);

		지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

		// given
		성인 = 회원_생성_및_로그인됨(19);
		청소년 = 회원_생성_및_로그인됨(18);
		어린이 = 회원_생성_및_로그인됨(12);
		비로그인 = new TokenResponse("");

	}

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재
	 */
	@DisplayName("최단 경로 관리")
	@Test
	void scenario1() {
		// When
		ExtractableResponse<Response> response = 최단경로_조회_요청함(양재역, 교대역);

		// Then
		최단경로_조회_응답함(response);
		최단경로_조회_경로_포함됨(response, Arrays.asList(양재역, 강남역, 교대역));
		최단경로_조회_최단거리_포함됨(response, 5);
		최단경로_조회_이용요금_포함됨(response, 2_250);

		// When
		ExtractableResponse<Response> response2 = 최단경로_조회_요청함(양재역, 양재역);

		// Then
		최단경로_조회_실패함(response2);

		// When
		ExtractableResponse<Response> response3 = 최단경로_조회_요청함(서울역, 옥수역);

		// Then
		최단경로_조회_실패함(response3);

		// When
		ExtractableResponse<Response> response4 = 최단경로_조회_요청함(서울역, 양재역);

		// then
		최단경로_조회_실패함(response4);
	}

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재
	 */
	@DisplayName("로그인에 따른 요금 관리")
	@Test
	void scenario2() {

		// when
		ExtractableResponse<Response> 비로그인_경로_조회응답 = 최단경로_조회_요청함(비로그인, 양재역, 교대역);

		// then
		최단경로_조회_응답함(비로그인_경로_조회응답);
		최단경로_조회_이용요금_포함됨(비로그인_경로_조회응답, 2250);

		// given
		TokenResponse 어린이 = 회원_생성_및_로그인됨(12);

		// when
		ExtractableResponse<Response> 어린이_경로_조회응답 = 최단경로_조회_요청함(어린이, 양재역, 교대역);

		// then
		최단경로_조회_응답함(어린이_경로_조회응답);
		최단경로_조회_이용요금_포함됨(어린이_경로_조회응답, 1125);

	}

	@DisplayName("성인 요금을 조회한다")
	@Test
	void getFareByAge1() {
		// when
		ExtractableResponse<Response> 성인_경로_조회응답 = 최단경로_조회_요청함(성인, 양재역, 교대역);

		// then
		최단경로_조회_응답함(성인_경로_조회응답);
		최단경로_조회_이용요금_포함됨(성인_경로_조회응답, 2_250);
	}

	@DisplayName("청소년 요금을 조회한다")
	@Test
	void getFareByAge2() {
		// when
		ExtractableResponse<Response> 청소년_경로_조회응답 = 최단경로_조회_요청함(청소년, 양재역, 교대역);

		// then
		최단경로_조회_응답함(청소년_경로_조회응답);
		최단경로_조회_이용요금_포함됨(청소년_경로_조회응답, 1_800);
	}

	@DisplayName("어린이 요금을 조회한다")
	@Test
	void getFareByAge3() {
		// when
		ExtractableResponse<Response> 어린이_경로_조회응답 = 최단경로_조회_요청함(어린이, 양재역, 교대역);

		// then
		최단경로_조회_응답함(어린이_경로_조회응답);
		최단경로_조회_이용요금_포함됨(어린이_경로_조회응답, 1125);
	}

	@DisplayName("비로그인 요금을 조회한다")
	@Test
	void getFare() {
		// when
		ExtractableResponse<Response> 비로그인_경로_조회응답 = 최단경로_조회_요청함(비로그인, 양재역, 교대역);

		// then
		최단경로_조회_응답함(비로그인_경로_조회응답);
		최단경로_조회_이용요금_포함됨(비로그인_경로_조회응답, 2_250);
	}

	@DisplayName("최단 경로를 조회한다")
	@Test
	void findPath() {
		// when
		ExtractableResponse<Response> response = 최단경로_조회_요청함(양재역, 교대역);

		// then
		최단경로_조회_응답함(response);
		최단경로_조회_경로_포함됨(response, Arrays.asList(양재역, 강남역, 교대역));
		최단경로_조회_최단거리_포함됨(response, 5);
	}

	@DisplayName("최단경로 조회 시, 출발역과 도착역이 같으면 안됨")
	@Test
	void findPath2() {
		// when
		ExtractableResponse<Response> response = 최단경로_조회_요청함(양재역, 양재역);

		// then
		최단경로_조회_실패함(response);
	}

	@DisplayName("존재하지 않은 출발역과 도착역을 조회할 경우 안됨")
	@Test
	void findPath3() {
		// when
		ExtractableResponse<Response> response = 최단경로_조회_요청함(서울역, 옥수역);

		// then
		최단경로_조회_실패함(response);
	}

	@DisplayName("출발역과 도착역이 연결되지 않으면 안됨")
	@Test
	void findPath4() {
		// when
		ExtractableResponse<Response> response = 최단경로_조회_요청함(서울역, 양재역);

		// then
		최단경로_조회_실패함(response);
	}

	private void 최단경로_조회_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 최단경로_조회_경로_포함됨(ExtractableResponse<Response> response,
		List<StationResponse> expectedStations) {

		PathResponse path = response.as(PathResponse.class);

		List<Long> expectedIds = expectedStations.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		List<Long> resultIds = path.getStations().stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultIds).containsAll(expectedIds);
	}

	private static void 최단경로_조회_이용요금_포함됨(ExtractableResponse<Response> response, int expectedFare) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getFare()).isEqualTo(expectedFare);
	}

	private static void 최단경로_조회_최단거리_포함됨(ExtractableResponse<Response> response, int expectedDistance) {
		PathResponse path = response.as(PathResponse.class);
		assertThat(path.getDistance()).isEqualTo(expectedDistance);
	}

	private void 최단경로_조회_응답함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 최단경로_조회_요청함(StationResponse source, StationResponse target) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
			.then().log().all()
			.extract();
	}

	public void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse station1,
		StationResponse station2, int distance) {
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, station1, station2, distance);
	}

	private LineResponse 지하철_노선_등록되어_있음(String lineName, String lineColor, StationResponse station1,
		StationResponse station2, int distance, int fare) {
		LineRequest lineRequest = new LineRequest(lineName, lineColor, station1.getId(), station2.getId(), distance,
			fare);
		return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
	}

	public static ExtractableResponse<Response> 최단경로_조회_요청함(TokenResponse token, StationResponse source,
		StationResponse target) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
			.then().log().all()
			.extract();
	}

	public static TokenResponse 회원_생성_및_로그인됨(int age) {
		String email = UUID.randomUUID() + "@email.com";
		String password = "PASSWORD";
		MemberAcceptanceTest.회원_생성을_요청(email, password, age);
		return AuthAcceptanceTest.로그인됨(email, password);
	}

}
