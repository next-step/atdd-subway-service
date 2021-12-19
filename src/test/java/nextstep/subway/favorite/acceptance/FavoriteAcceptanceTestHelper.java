package nextstep.subway.favorite.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteAcceptanceTestHelper {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token,
        StationResponse source, StationResponse target) {

        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .body(favoriteRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
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

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long favoriteId) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/favorites/{id}", favoriteId)
            .then().log().all().extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
