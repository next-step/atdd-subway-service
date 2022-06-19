package nextstep.subway.favorite.factory;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceFactory {

    public static ExtractableResponse<Response> 즐겨찾기_추가(
            String accessToken,
            StationResponse sourceStation,
            StationResponse targetStation
    ) {
        FavoriteRequest favoriteRequest = FavoriteRequest.of(sourceStation.getId(), targetStation.getId());

        return RestAssured
                .given().log().all()
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
