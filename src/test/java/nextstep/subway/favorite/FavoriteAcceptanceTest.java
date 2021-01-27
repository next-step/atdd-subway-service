package nextstep.subway.favorite;

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
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 정자역;
	private LineResponse 이호선;
	private String 사용자_토큰;

	/**
	 *   Background
	 *     Given 지하철역 등록되어 있음
	 *     And 지하철 노선 등록되어 있음
	 *     And 지하철 노선에 지하철역 등록되어 있음
	 *     And 회원 등록되어 있음
	 *     And 로그인 되어있음
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		// Given 지하철역 등록되어 있음
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

		// And 지하철 노선 등록되어 있음
		이호선 = LineAcceptanceTest
			.지하철_노선_등록되어_있음("이호선", "green", 0, 강남역, 양재역, 5).as(LineResponse.class);

		// And 지하철 노선에 지하철역 등록되어 있음
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 양재역, 정자역, 6);

		// And 회원 등록되어 있음
		MemberAcceptanceTest
			.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);

		// And 로그인 되어있음
		사용자_토큰 = MemberAcceptanceTest.로그인_되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
	}

	/**
	 *   Scenario: 즐겨찾기를 관리
	 *     When 즐겨찾기 생성을 요청
	 *     Then 즐겨찾기 생성됨
	 *     When 즐겨찾기 목록 조회 요청
	 *     Then 즐겨찾기 목록 조회됨
	 *     When 즐겨찾기 삭제 요청
	 *     Then 즐겨찾기 삭제됨
	 */
	@DisplayName("즐겨찾기 관련 기능 통합 인수 테스트")
	@Test
	void testIntegrationAcceptance() {
		// When 즐겨찾기 생성을 요청
		ExtractableResponse<Response> 즐겨찾기_생성_요청_응답1 = 즐겨찾기_생성_요청(사용자_토큰, 강남역, 정자역);
		ExtractableResponse<Response> 즐겨찾기_생성_요청_응답2 = 즐겨찾기_생성_요청(사용자_토큰, 정자역, 양재역);

		// Then 즐겨찾기 생성됨
		즐겨찾기_생성됨(즐겨찾기_생성_요청_응답1);
		즐겨찾기_생성됨(즐겨찾기_생성_요청_응답2);

		// When 즐겨찾기 목록 조회 요청
		ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_응답 = 즐겨찾기_목록_조회_요청(사용자_토큰);
		// Then 즐겨찾기 목록 조회됨
		즐겨찾기_목록_응답됨(즐겨찾기_목록_조회_요청_응답);
		즐겨찾기_목록_포함됨(즐겨찾기_목록_조회_요청_응답, Arrays.asList(즐겨찾기_생성_요청_응답1, 즐겨찾기_생성_요청_응답2));

		// When 즐겨찾기 삭제 요청
		ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = 즐겨찾기_삭제_요청(사용자_토큰, 즐겨찾기_생성_요청_응답2);
		// Then 즐겨찾기 삭제됨
		즐겨찾기_목록_삭제됨(즐겨찾기_삭제_요청_응답);
	}

	private ExtractableResponse<Response> 즐겨찾기_생성_요청(String 사용자_토큰, StationResponse 정자역, StationResponse 양재역) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(정자역.getId(), 양재역.getId());

		return RestAssured
			.given().log().all()
			.auth().oauth2(사용자_토큰)
			.body(favoriteRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String 사용자_토큰) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(사용자_토큰)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response,
		List<ExtractableResponse<Response>> createdResponses) {
		List<Long> 생성된_즐겨찾기_ID_목록 = createdResponses.stream()
			.map(it -> Long.parseLong(it.header("Location").split("/")[2]))
			.collect(Collectors.toList());

		assertThat(response.jsonPath().getList("id", Long.class)).containsAll(생성된_즐겨찾기_ID_목록);
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String 사용자_토큰, ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return RestAssured
			.given().log().all()
			.auth().oauth2(사용자_토큰)
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_목록_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
