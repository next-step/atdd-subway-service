package nextstep.subway.path.acceptance;

import static nextstep.subway.common.ServiceApiFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestApiFixture;

@DisplayName("지하철 경로 조회")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PathAcceptanceTest extends AcceptanceTest {
	private long 신분당선_ID;
	private long 이호선_ID;
	private long 삼호선_ID;
	private long 강남역_ID;
	private long 양재역_ID;
	private long 교대역_ID;
	private long 남부터미널역_ID;
	private String 사용자_성인;

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

		신분당선_ID = createLineId("신분당선", "bg-red-600", 강남역_ID, 양재역_ID, 10, 500);
		이호선_ID = createLineId("이호선", "bg-red-600", 교대역_ID, 강남역_ID, 10, 0);
		삼호선_ID = createLineId("삼호선", "bg-red-600", 교대역_ID, 양재역_ID, 5, 200);

		postSections(삼호선_ID, sectionRequest(교대역_ID, 남부터미널역_ID, 3));

		final String email = "member@email.com";
		final String password = "<secret>";
		final int age = 20;
		회원_등록됨(email, password, age);
		사용자_성인 = 로그인됨(email, password);
	}

	// [outside->in] happy case 만 고려
	@DisplayName("지하철 최단 경로를 조회한다.")
	@Test
	void findShortestPath() {
		final ExtractableResponse<Response> response = 최단_경로_조회_요청(사용자_성인, 강남역_ID, 남부터미널역_ID);
		최단_경로_응답_확인됨(response);

		final PathResponse pathResponse = response.as(PathResponse.class);
		최단_경로_역_목록_확인됨(pathResponse, Arrays.asList(강남역_ID, 양재역_ID, 남부터미널역_ID));
		최단_경로_거리_확인됨(pathResponse, 12);
		최단_경로_운임료_확인됨(pathResponse, 1650);
	}

	private ExtractableResponse<Response> 최단_경로_조회_요청(String accessToken, long source, long target) {
		return RestApiFixture.response(RestApiFixture.requestWithOAuth2(accessToken)
			.queryParam("source", source)
			.queryParam("target", target)
			.get("/paths")
		);
	}

	private void 최단_경로_응답_확인됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 최단_경로_역_목록_확인됨(PathResponse pathResponse, List<Long> expect) {
		final List<Long> actualStationIds = pathResponse.getStations()
			.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());
		assertThat(actualStationIds).containsExactlyElementsOf(expect);
	}

	private void 최단_경로_거리_확인됨(PathResponse pathResponse, double distance) {
		assertThat(pathResponse.getDistance()).isEqualTo(distance);
	}

	private void 최단_경로_운임료_확인됨(PathResponse pathResponse, int fare) {
		assertThat(pathResponse.getFare()).isEqualTo(fare);
	}

	private void 회원_등록됨(String email, String password, int age) {
		MemberAcceptanceTest.회원_생성을_요청(email, password, age);
	}

	private String 로그인됨(String email, String password) {
		return AuthAcceptanceTest.로그인_요청(email, password)
			.as(TokenResponse.class).getAccessToken();
	}
}
