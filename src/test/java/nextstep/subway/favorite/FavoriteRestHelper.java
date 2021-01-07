package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
@SuppressWarnings("NonAsciiCharacters")
public class FavoriteRestHelper {

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(long source, long target, String accessToken) {
        FavoriteRequest request = new FavoriteRequest(source, target);
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(String url, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(url)
                .then().log().all()
                .extract();
    }
}
