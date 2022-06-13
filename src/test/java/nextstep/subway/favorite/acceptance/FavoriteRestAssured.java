package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class FavoriteRestAssured {
    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, StationResponse 강남역, StationResponse 정자역) {
        FavoriteRequest favoriteRequest = FavoriteRequest.of(강남역.getId(), 정자역.getId());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
