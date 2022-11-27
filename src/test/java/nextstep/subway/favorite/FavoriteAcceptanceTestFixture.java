package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceTestFixture {
    private static final String FAVORITE_MAIN_PATH = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, StationResponse source, StationResponse target) {
        FavoriteRequest request = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().post(FAVORITE_MAIN_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get(FAVORITE_MAIN_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(source.getId());
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(target.getId());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
