package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceFactory {
    public static ExtractableResponse<Response> 즐겨찾기_생성_시도(TokenResponse accessToken, StationResponse sourceStation, StationResponse targetStation) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStation.getId(), targetStation.getId());
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when()
                .post("/favorites")
                .then()
                .log().all().extract();
    }
    public static ExtractableResponse<Response> 즐겨찾기_목록조회_시도(TokenResponse accessToken, StationResponse sourceStation, StationResponse targetStation) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/favorites")
                .then()
                .log().all().extract();
    }

    public static void 즐겨찾기_생성완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_조회완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
