package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.MediaType;

public class FavoriteRestAssured {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, FavoriteRequest favoriteRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .pathParam("id", favoriteId)
                .when().delete("/favorites/{id}")
                .then().log().all()
                .extract();
    }

}
