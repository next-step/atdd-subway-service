package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.MediaType;

public class FavoriteApiHelper {

    public static ExtractableResponse<Response> 즐겨찾기_생성요청(String 토큰, Long 경로_시작역_ID,
        Long 경로_종료역_ID) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(경로_시작역_ID, 경로_종료역_ID);
        return RestAssured.given().log().all()
            .header("Authorization", "Bearer " + 토큰)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록조회요청(String 토큰) {
        return RestAssured.given().log().all()
            .header("Authorization", "Bearer " + 토큰)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제요청(String 토큰, String 즐겨찾기_ID) {
        return RestAssured.given().log().all()
            .header("Authorization", "Bearer " + 토큰)
            .when().delete("/favorites/" + 즐겨찾기_ID)
            .then().log().all()
            .extract();
    }
}
