package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

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
}
