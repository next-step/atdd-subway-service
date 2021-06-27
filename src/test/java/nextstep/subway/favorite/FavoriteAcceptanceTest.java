package nextstep.subway.favorite;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

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
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private StationResponse 강남역;
	private StationResponse 광교역;
	private LineResponse 신분당선;
	private TokenResponse 로그인토큰;

	@BeforeEach
	public void setUp() {
		super.setUp();
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
			new LineRequest("신분당선", "red", 강남역.getId(), 광교역.getId(), 10))
			.as(LineResponse.class);

		회원이_생성됨(EMAIL, PASSWORD, AGE);
		로그인토큰 = AuthAcceptanceTest.로그인을_요청한다(new TokenRequest(EMAIL, PASSWORD)).as(TokenResponse.class);
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manageFavorites() {
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 광교역.getId());
		ExtractableResponse<Response> postFavoriteResponse = this.즐겨찾기_생성을_요청(로그인토큰, favoriteRequest);
		this.즐겨찾기가_생성됨을_확인(postFavoriteResponse);

		String favoriteId = postFavoriteResponse.header("Location").split("/")[2];

		ExtractableResponse<Response> findFavoritesResponse = this.즐겨찾기_목록을_조회요청(로그인토큰);
		this.즐겨찾기_목록조회_확인(findFavoritesResponse);

		ExtractableResponse<Response> deleteResponse = this.즐겨찾기_삭제_요청(로그인토큰, favoriteId);
		this.즐겨찾기_삭제_확인(deleteResponse);
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse token, String deleteId) {
		return RestAssured.given().log().all()
			.auth().oauth2(token.getAccessToken())
			.when()
			.delete("/favorites/" + deleteId)
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_삭제_확인(ExtractableResponse<Response> deleteResponse) {
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private void 즐겨찾기_목록조회_확인(ExtractableResponse<Response> findFavoritesResponse) {
		assertThat(findFavoritesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_목록을_조회요청(TokenResponse 로그인토큰) {
		return RestAssured.given().log().all()
			.auth().oauth2(로그인토큰.getAccessToken())
			.when()
			.get("/favorites")
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기가_생성됨을_확인(ExtractableResponse<Response> postFavoriteResponse) {
		assertThat(postFavoriteResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse 로그인토큰, FavoriteRequest favoriteRequest) {
		return RestAssured.given().log().all()
			.auth().oauth2(로그인토큰.getAccessToken())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when()
			.post("/favorites")
			.then().log().all()
			.extract();
	}
}