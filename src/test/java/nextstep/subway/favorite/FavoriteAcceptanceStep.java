package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceStep {
    public static ExtractableResponse<Response> 등록된_즐겨찾기(final TokenResponse tokenResponse, FavoriteRequest request) {
        return 즐겨찾기_등록_요청(tokenResponse, request);
    }

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(final TokenResponse tokenResponse, FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final TokenResponse tokenResponse) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final TokenResponse tokenResponse, ExtractableResponse<Response> response) {
        Long id = Long.parseLong(response.header("Location").split("/")[2]);
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
