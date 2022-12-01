package nextstep.subway.favorite.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

public class FavoriteAcceptanceTestActions {

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> getFavoriteResponses) {
        JsonPath jsonPath = getFavoriteResponses.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getString("id")).isNotBlank(),
                () -> assertThat(jsonPath.getString("source[0].name")).isEqualTo("강남역"),
                () -> assertThat(jsonPath.getString("target[0].name")).isEqualTo("광교역")
        );
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(ContentType.JSON)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> createFavoriteResponse) {
        assertThat(createFavoriteResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source,
                                                           StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
