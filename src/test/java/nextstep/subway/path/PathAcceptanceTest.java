package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;

	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 17;

	/**
	 *   Background
	 *     Given 지하철역 등록되어 있음
	 *     And 지하철 노선 등록되어 있음
	 *     And 지하철 노선에 지하철역 등록되어 있음
	 *     And 회원이 생성되어 있음
	 *
	 *
	 *     교대역     --- *2호선* ---   강남역
	 *       |                          |
	 *    *3호선*                    *신분당선*
	 *       |                          |
	 *     남부터미널역  --- *3호선* --- 양재역
	 *
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();
		// Given 지하철역 등록되어 있음
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		// And 지하철 노선 등록되어 있음
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 23).as(LineResponse.class);
		이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 62).as(LineResponse.class);
		삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 84).as(LineResponse.class);

		// And 지하철 노선에 지하철역 등록되어 있음
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 43);

		// And 회원이 생성되어 있음
		 MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
	}

	/**
	 *   Scenario: 지하철 최단 경로를 조회
	 *     When 지하철 최단 경로 조회 요청
	 *     Then 최단_경로_조회_성공됨
	 *     AND 최단 경로에 포함된 역 목록이 순서대로 조회됨
	 *     AND 최단 경로의 거리가 예상과 같음
	 *     AND 지하철 이용 요금이 예상과 같음
	 *
	 *     When 로그인 요청
	 *     Then 로그인 성공됨
	 *
	 *     When 지하철 최단 경로 조회 요청
	 *     AND 지하철 이용 요금이 예상과 같음
	 */
	@DisplayName("지하철 경로 조회 통합 인수 테스트")
	@Test
	void testIntegrationAcceptance () {
		// When 지하철 최단 경로 조회 요청
		ExtractableResponse<Response> 지하철_최단_경로_조회_요청_응답 = 지하철_최단_경로_조회_요청(교대역, 양재역);

		// Then 최단_경로_조회_성공됨
		최단_경로_조회_성공됨(지하철_최단_경로_조회_요청_응답);

		// And 등록한 지하철 구간이 반영된 역 목록이 조회됨
		최단_경로에_지하철역_순서_정렬됨(지하철_최단_경로_조회_요청_응답, Arrays.asList(교대역, 남부터미널역, 양재역));
		
		// And 최단 경로의 거리가 예상과 같음
		최단_경로의_거리가_예상과_같음(지하철_최단_경로_조회_요청_응답, 84);

		// And 지하철 이용 요금이 예상과 같음
		지하철_이용_요금이_예상과_같음(지하철_최단_경로_조회_요청_응답, 2450);

		// When 로그인 요청
		ExtractableResponse<Response> 로그인_요청_응답 = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD);
		
		// Then 로그인 성공됨
		String 로그인_토큰 = AuthAcceptanceTest.로그인_토큰_추출함(로그인_요청_응답);

		// When 지하철 최단 경로 조회 요청
		ExtractableResponse<Response> 로그인_후_최단_경로_조회_요청_응답 = 로그인_후_최단_경로_조회_요청(로그인_토큰, 교대역, 양재역);

		// And 지하철 이용 요금이 예상과 같음
		지하철_이용_요금이_예상과_같음(로그인_후_최단_경로_조회_요청_응답, (2450 - 350) * 80 / 100);
	}

	private ExtractableResponse<Response> 지하철_최단_경로_조회_요청(StationResponse source, StationResponse target) {
		return RestAssured
			.given().log().all()
			.when().get("/paths?source={sourceStationId}&target={targetStationId}", source.getId(), target.getId())
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 로그인_후_최단_경로_조회_요청(String token, StationResponse source, StationResponse target) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.when().get("/paths?source={sourceStationId}&target={targetStationId}", source.getId(), target.getId())
			.then().log().all()
			.extract();
	}

	public static void 최단_경로_조회_성공됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 최단_경로에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
		PathResponse pathResponse = response.as(PathResponse.class);
		List<Long> stationIds = pathResponse.getStations().stream()
			.map(PathStationResponse::getId)
			.collect(Collectors.toList());

		List<Long> expectedStationIds = expectedStations.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
	}

	private void 최단_경로의_거리가_예상과_같음(ExtractableResponse<Response> response, int expectedDistance) {
		PathResponse pathResponse = response.as(PathResponse.class);
		assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
	}

	private void 지하철_이용_요금이_예상과_같음(ExtractableResponse<Response> response, int expectedFare) {
		PathResponse pathResponse = response.as(PathResponse.class);
		assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
	}
}
