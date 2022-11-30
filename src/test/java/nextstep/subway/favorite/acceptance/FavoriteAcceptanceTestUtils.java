package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestUtils.인증_실패;
import static nextstep.subway.utils.CommonTestFixture.FAVORITE_BASE_PATH;
import static nextstep.subway.utils.CommonTestFixture.FAVORITE_PATH_VARIABLE;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTestUtils {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long departureId, Long arrivalId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(new FavoriteRequest(departureId, arrivalId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITE_BASE_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get(FAVORITE_BASE_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(FAVORITE_BASE_PATH + FAVORITE_PATH_VARIABLE, favoriteId)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        인증_실패(response);
    }

    public static void 즐겨찾기_목록_조회_실패됨(ExtractableResponse<Response> response) {
        인증_실패(response);
    }

    public static void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        인증_실패(response);
    }
}
