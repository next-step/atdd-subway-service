package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteResponse;

public class FavoriteAcceptanceTestHelper {
    private FavoriteAcceptanceTestHelper() {
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Map<String, String> params) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_목록_예상된_결과_조회됨(ExtractableResponse<Response> response,
        List<Long> expected) {
        List<Long> resultIds = response.jsonPath()
            .getList(".", FavoriteResponse.class)
            .stream()
            .map(FavoriteResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultIds.size()).isEqualTo(expected.size());
        assertThat(resultIds).containsAll(expected);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, long id) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when()
            .delete("/favorites/" + id)
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
