package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

public class FavoriteStaticAcceptance {

	private static final String FAVORITE_PATH = "/favorites";
	private static final String SLASH = "/";

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long id) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.when().delete(FAVORITE_PATH + SLASH + id)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.when().get(FAVORITE_PATH)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, int size) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.body().jsonPath().getList(".", FavoriteResponse.class)).hasSize(size);
	}

	public static FavoriteRequest 즐겨찾기_요청값_생성(Long source, Long target) {
		return new FavoriteRequest(source, target);
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, FavoriteRequest params) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post(FAVORITE_PATH)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
