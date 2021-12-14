package nextstep.subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class TestFavoriteFactory {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String 토큰, final Long source, final Long target) {
        final FavoriteRequest favoriteRequest = FavoriteRequest.of(source, target);
        return RestAssured.given().log().all()
                .auth().oauth2(토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String 토큰, final Long 즐겨찾기ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰)
                .when()
                .get("/favorites/{id}", 즐겨찾기ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String 토큰, final Long 즐겨찾기ID) {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰)
                .when()
                .delete("/favorites/{id}", 즐겨찾기ID)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회_요청됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
