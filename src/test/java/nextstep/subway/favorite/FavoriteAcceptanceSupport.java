package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceSupport {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, Long sourceId, Long targetId) {
        FavoriteRequest request = new FavoriteRequest(sourceId, targetId);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 중복으로_인해_즐겨찾기_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long id) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/favorites/{id}" , id)
            .then().log().all()
            .extract();
    }
}
