package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

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
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends BaseTest {
	public static final String EMAIL = "test@test.com";
	public static final String PASSWORD = "test1234";
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private String 토큰;

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
		지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
		지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

		MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, 20);

		토큰 = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
	}

	@DisplayName("즐겨찾기를 관리")
	@Test
	void manageFavorite() {
		//when
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(토큰, 강남역, 남부터미널역);

		//then
		long 즐겨찾기_ID = 즐겨찾기_생성됨(createResponse);

		//when
		ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(토큰);

		//then
		즐겨찾기_조회됨(getResponse);

		//when
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(토큰, 즐겨찾기_ID);

		//then
		즐겨찾기_삭제됨(deleteResponse);
	}

	private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, long favoriteId) {
		return RestAssured
			.given().log().all().auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/favorites/{favoriteId}", favoriteId)
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
		return RestAssured
			.given().log().all().auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	private long 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return Long.parseLong(response.header("Location").replace("/favorites/", ""));
	}

	private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken,
		StationResponse sourceStation, StationResponse targetStation) {
		return RestAssured
			.given().log().all().auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(FavoriteRequest.of(sourceStation.getId(), targetStation.getId()))
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	private LineResponse 지하철_노선_등록되어_있음(
		String lineName,
		String color,
		StationResponse upStation,
		StationResponse downStation,
		int distance) {

		LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance);
		return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

	}
}