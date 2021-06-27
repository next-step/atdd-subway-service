package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.makeBearerToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteAcceptanceStep {
    private static final String FAVORITES = "/favorites";
    private static final String AUTHORIZATION = "Authorization";

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse 사용자, StationResponse upStation, StationResponse downStation) {
        FavoriteRequest request = new FavoriteRequest(String.valueOf(upStation.getId()), String.valueOf(downStation.getId()));

        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, makeBearerToken(사용자.getAccessToken()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(FAVORITES)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse 사용자) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, makeBearerToken(사용자.getAccessToken()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITES)
                .then().log().all().extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회_응답_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_목록_조회_포함_됨(ExtractableResponse<Response> response, List<FavoriteResponse> expected) {
        List<FavoriteResponse> actual = response.jsonPath().getList(".", FavoriteResponse.class);
        assertAll(()->{
            assertThat(actual.size()).isEqualTo(expected.size());
            for (int i = 0; i < actual.size(); i++) {
                assertThat(actual.get(i).getId()).isEqualTo(expected.get(i).getId());
                assertThat(actual.get(i).getSource().getId()).isEqualTo(expected.get(i).getSource().getId());
                assertThat(actual.get(i).getTarget().getId()).isEqualTo(expected.get(i).getTarget().getId());
            }
        });
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse 사용자, ExtractableResponse<Response> createResponse) {
        return null;
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {

    }
}
