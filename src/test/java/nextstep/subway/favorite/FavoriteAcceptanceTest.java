package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.testfactory.LineAcceptanceTestFactory;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.testfactory.StationAcceptanceTestFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private static String EMAIL = "email@test.com";
	private static String PASSWORD = "test password123";

	private StationResponse 강남역;
	private StationResponse 정자역;
	private String 사용자_인증_정보;

	@BeforeEach
	public void setUp() {
		super.setUp();
		// given
		강남역 = StationAcceptanceTestFactory.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		정자역 = StationAcceptanceTestFactory.지하철역_등록되어_있음("정자역").as(StationResponse.class);
		LineAcceptanceTestFactory.지하철_노선_등록되어_있음("신분당선", "red", 강남역, 정자역, 10);

		MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, 30);
		사용자_인증_정보 = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	public void manageFavorite() {
		//when
		ExtractableResponse<Response> 즐겨찾기_생성_결과 = 즐겨찾기_생성_요청(사용자_인증_정보, 강남역, 정자역);
		//then
		즐겨찾기_생성됨(즐겨찾기_생성_결과);

		// when
		ExtractableResponse<Response> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회_요청(사용자_인증_정보);
		// then
		즐겨찾기_조회됨(즐겨찾기_목록_조회_결과, 강남역, 정자역);
		//
		//when
		ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_삭제_요청(사용자_인증_정보, 즐겨찾기_생성_결과);
		//then
		즐겨찾기_삭제됨(즐겨찾기_삭제_결과);
	}

	@DisplayName("없는 즐겨찾기 삭제를 요청하는 경우 예외")
	@Test
	public void deleteFavorite_exception1() {

		// when
		ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_삭제_요청(사용자_인증_정보, "/favorites/10");

		//then
		즐겨찾기_삭제오류(즐겨찾기_삭제_결과);
	}

	@DisplayName("다른 사용자의 즐겨찾기를 삭제 요청하는 경우 예외")
	@Test
	public void deleteFavorite_exception2() {

		//given
		String otherEmail = "other@email.com";
		String otherPassword = "test password";
		MemberAcceptanceTest.회원_생성을_요청(otherEmail, otherPassword, 25);
		String 다른사용자_인증정보 = AuthAcceptanceTest.로그인_요청(otherEmail, otherPassword)
			.as(TokenResponse.class)
			.getAccessToken();
		ExtractableResponse<Response> 즐겨찾기_생성_결과 = 즐겨찾기_생성_요청(다른사용자_인증정보, 강남역, 정자역);

		// when
		ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_삭제_요청(사용자_인증_정보, 즐겨찾기_생성_결과);

		//then
		즐겨찾기_삭제오류(즐겨찾기_삭제_결과);
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source,
		StationResponse target) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

		return RestAssured
			.given()
			.log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
		return RestAssured
			.given()
			.log().all()
			.auth().oauth2(accessToken)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
		String uri = response.header("Location");

		return 즐겨찾기_삭제_요청(accessToken, uri);
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String uri) {
		return RestAssured
			.given()
			.log().all()
			.auth().oauth2(accessToken)
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response, StationResponse source,
		StationResponse target) {
		List<FavoriteResponse> favoriteResponse = response.body().jsonPath().getList(".", FavoriteResponse.class);

		assertThat(favoriteResponse.size()).isGreaterThan(0);

		FavoriteResponse favorite = favoriteResponse.get(0);
		assertThat(favorite.getId()).isNotNull();
		assertThat(favorite.getSource()).isEqualTo(source);
		assertThat(favorite.getTarget()).isEqualTo(target);
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private void 즐겨찾기_삭제오류(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}