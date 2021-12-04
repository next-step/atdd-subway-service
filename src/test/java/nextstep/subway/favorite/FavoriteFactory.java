package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.MediaType;

public class FavoriteFactory {


    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse, String token) {
        String uri = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target, String token) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }
}
