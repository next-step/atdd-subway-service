package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceSupport {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, Long sourceId, Long targetId) {
        FavoriteRequest request = new FavoriteRequest(sourceId, targetId);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 중복으로_인해_즐겨찾기_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asString()).contains(ExceptionType.ALREADY_REGISTERED_FAVORITE.getMessage());
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_조회_검증됨(ExtractableResponse<Response> response, List<ExtractableResponse> favorites) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<FavoriteResponse> sources = response.jsonPath().getList(".", FavoriteResponse.class);
        List<String> targets = favorites.stream()
                .map(it -> it.header("Location"))
                .map(it -> it.replaceAll("/favorites/", ""))
                .collect(Collectors.toList());

        assertThat(sources.size()).isEqualTo(favorites.size());

        for (int i=0; i<sources.size(); i++) {
            FavoriteResponse source = sources.get(i);
            String targetId = targets.get(i);

            assertThat(String.valueOf(source.getId())).isEqualTo(targetId);
        }
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(uri)
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
