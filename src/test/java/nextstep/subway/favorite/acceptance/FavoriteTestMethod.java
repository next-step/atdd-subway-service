package nextstep.subway.favorite.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;

public class FavoriteTestMethod {
	public static ExtractableResponse<Response> deleteFavorite(String token, Long id) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/favorites/" + id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findFavorite(String token) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> createFavorite(String token, Long sourceId, Long targetId) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}
}
