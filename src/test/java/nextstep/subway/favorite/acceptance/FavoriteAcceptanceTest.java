package nextstep.subway.favorite.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;

	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private StationResponse 인천역;

	private String 로그인_토큰;

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
		인천역 = StationAcceptanceTest.지하철역_등록되어_있음("인천역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10);
		삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

		MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD,
			MemberAcceptanceTest.AGE);

		로그인_토큰 = AuthAcceptanceTest.로그인_토큰_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD)
			.as(TokenResponse.class)
			.getAccessToken();
	}

	@DisplayName("지하철 즐겨찾기 등록 요청을 함")
	@Test
	void crateFavoriteTest() {
		// given // when
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(로그인_토큰, 교대역.getId(), 양재역.getId());

		// then
		즐겨찾기_생성_확인됨(response);
	}

	@DisplayName("지하철 즐겨찾기 목록 조회 요청함")
	@Test
	void showFavoriteTest() {
		// given // when
		ExtractableResponse<Response> response = 즐겨찾기_조회_요청(로그인_토큰);

		// then
		즐겨찾기_목록_조회됨(response);
	}

	@DisplayName("지하철 즐겨찾기 목록 삭제 요청함")
	@Test
	void deleteFavoriteTest() {
		// given // when

	}

	private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long sourceId, Long targetId) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.body(new PathRequest(sourceId, targetId))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/favorites")
			.then().log().all().extract();
	}

	private ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all().extract();
	}

	private void 즐겨찾기_생성_확인됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
		);
	}
}
