package nextstep.subway.favorite.step;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteStep {

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse token, StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source.getId(), target.getId()))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(new TypeRef<List<FavoriteResponse>>() {}).size()).isEqualTo(1);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static FavoriteResponse 즐겨찾기_생성되어_있음(TokenResponse token, StationResponse source, StationResponse target) {
        return 즐겨찾기_생성_요청(token, source, target).as(FavoriteResponse.class);
    }
}
