package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
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

	private TokenResponse tokenResponse;

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
		회원이_생성됨("email@email.com", "123", 30);
		tokenResponse = 로그인을_요청한다(new TokenRequest("email@email.com", "123"))
			.as(TokenResponse.class);

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(
			LineResponse.class);
		이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
		삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

		지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
	}

	@DisplayName("지하철 최단경로를 조회한다")
	@Test
	void getShortestPath() {
		ExtractableResponse<Response> response = 최단_경로를_조회한다(tokenResponse.getAccessToken(), 강남역.getId(),
			남부터미널역.getId());
		this.최단_경로_조회_확인(response, 12, 1250, Arrays.asList(강남역.getId(), 양재역.getId(), 남부터미널역.getId()));
	}

	@DisplayName("지하철 경로조회 실패 - 출발역과 도착역이 동일함")
	@Test
	void testGetShortestPathError1() {
		ExtractableResponse<Response> response = 최단_경로를_조회한다(tokenResponse.getAccessToken(), 강남역.getId(), 강남역.getId());
		this.최단_경로_조회_실패(response);
	}

	@DisplayName("지하철 경로조회 실패 - 출발역과 도착역이 연결되지 않은경우")
	@Test
	void testGetShortestPathError2() {
		StationResponse 수원역 = StationAcceptanceTest.지하철역_등록되어_있음("수원역").as(StationResponse.class);
		StationResponse 안양역 = StationAcceptanceTest.지하철역_등록되어_있음("안양역").as(StationResponse.class);
		LineResponse 일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "bg-red-600", 안양역.getId(), 수원역.getId(), 10)).as(
			LineResponse.class);

		ExtractableResponse<Response> response = 최단_경로를_조회한다(tokenResponse.getAccessToken(), 강남역.getId(), 안양역.getId());
		this.최단_경로_조회_실패(response);
	}

	@DisplayName("지하철 경로조회 실패 - 출발역과 도착역이 경로에 존재하지 않는경우")
	@Test
	void testGetShortestPathError3() {
		StationResponse 수원역 = StationAcceptanceTest.지하철역_등록되어_있음("수원역").as(StationResponse.class);
		StationResponse 안양역 = StationAcceptanceTest.지하철역_등록되어_있음("안양역").as(StationResponse.class);

		ExtractableResponse<Response> response = 최단_경로를_조회한다(tokenResponse.getAccessToken(), 수원역.getId(), 안양역.getId());
		this.최단_경로_조회_실패(response);
	}

	private void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 최단_경로_조회_확인(ExtractableResponse<Response> response, int distance, int fare, List<Long> expectedIds) {
		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		PathResponse pathResponse = response.as(PathResponse.class);
		Assertions.assertThat(pathResponse.getDistance()).isEqualTo(distance);
		Assertions.assertThat(
			pathResponse.getStations()
				.stream()
				.map(StationResponse::getId).collect(Collectors.toList()))
			.containsExactlyElementsOf(expectedIds);
		Assertions.assertThat(pathResponse.getFare()).isEqualTo(fare);
	}

	private static ExtractableResponse<Response> 최단_경로를_조회한다(String token, long source, long target) {
		return RestAssured.given().log().all()
			.auth().oauth2(token)
			.queryParam("source", source)
			.queryParam("target", target)
			.when()
			.get("/paths")
			.then().log().all()
			.extract();
	}
}
