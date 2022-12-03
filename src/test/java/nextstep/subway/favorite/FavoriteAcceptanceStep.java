package nextstep.subway.favorite;


import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.apache.groovy.util.Maps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceStep {

    private static final String FAVORITES = "/favorites";
    public static final Long NOT_EXIST_FAVORITE_ID = Long.MAX_VALUE;

    static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse token,
                                                    StationResponse sourceStation,
                                                    StationResponse targetStation) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequestBody(sourceStation, targetStation))
                .when().post(FAVORITES)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> createRequestBody(StationResponse sourceStation, StationResponse targetStation) {
        return Maps.of("source", sourceStation.getId() + "",
                "target", targetStation.getId() + "");
    }

    static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();
    }

    static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITES)
                .then().log().all()
                .extract();
    }

    static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, StationResponse ...expectedStations) {
        List<FavoriteResponse> favoriteResponses = toFavoritesResponseList(response);

        assertThat(Stream.concat(
                favoriteResponses.stream()
                        .map(FavoriteResponse::getSource)
                        .map(StationResponse::getName),
                favoriteResponses.stream().map(FavoriteResponse::getTarget)
                        .map(StationResponse::getName)))
                .containsExactlyInAnyOrderElementsOf(getStationsName(expectedStations));
    }

    private static List<FavoriteResponse> toFavoritesResponseList(ExtractableResponse<Response> response) {
        return response.body().as(new TypeRef<List<FavoriteResponse>>() {});
    }

    private static List<String> getStationsName(StationResponse ...stations) {
        return Arrays.stream(stations)
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }


    static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse token, String uri) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse token, ExtractableResponse<Response> response) {
        return 즐겨찾기_삭제_요청(token, response.header(HttpHeaders.LOCATION));
    }

    static ExtractableResponse<Response> 존재하지_않는_즐겨찾기_삭제_요청(TokenResponse token) {
        return 즐겨찾기_삭제_요청(token, FAVORITES + "/" + NOT_EXIST_FAVORITE_ID);
    }

    static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    static void 즐겨찾기_삭제_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}