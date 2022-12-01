package nextstep.subway.favorite;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.station.dto.StationResponse;
import org.apache.groovy.util.Maps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceStep {

    private static final String FAVORITES = "/favorites";

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

    static void 즐겨찾기_목록_조회됨() {
    }

    static void 즐겨찾기_목록_조회_요청() {
    }

    static void 즐겨찾기_삭제_요청() {
    }

    static void 즐겨찾기_삭제됨() {

    }
}
