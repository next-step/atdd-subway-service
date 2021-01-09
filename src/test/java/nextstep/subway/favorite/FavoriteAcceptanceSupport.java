package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceSupport {
	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source, StationResponse target) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
		return RestAssured
				.given().log().all().auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(favoriteRequest)
				.when().post("/favorites")
				.then().log().all()
				.extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> 즐겨찾기_생성_결과) {
		assertThat(즐겨찾기_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
		return RestAssured
				.given().log().all().auth().oauth2(accessToken)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/favorites")
				.then().log().all()
				.extract();
	}

	public static void 즐겨찾기_목록_검사(ExtractableResponse<Response> 즐겨찾기_목록_조회_결과, int size, List<String> sourceStationNames) {
		List<FavoriteResponse> favoriteResponses =
				즐겨찾기_목록_조회_결과.body().jsonPath().getList("", FavoriteResponse.class);
		assertThat(favoriteResponses)
				.hasSize(size)
				.map(favoriteResponse -> favoriteResponse.getSource().getName())
				.asList()
				.containsExactly(sourceStationNames.toArray());
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> 즐겨찾기_생성_결과) {
		String uri = 즐겨찾기_생성_결과.header("Location");
		return RestAssured
				.given().log().all().auth().oauth2(accessToken)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().delete(uri)
				.then().log().all()
				.extract();
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> 즐겨찾기_삭제_결과) {
		assertThat(즐겨찾기_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static void 즐겨찾기_삭제_실패함(ExtractableResponse<Response> 즐겨찾기_삭제_결과) {
		assertThat(즐겨찾기_삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
