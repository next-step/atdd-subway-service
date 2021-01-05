package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 생성 요청")
    @Test
    void addFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청() {
        Map<String, String> params = new HashMap<>();
        params.put("source", 1L + "");
        params.put("target", 2L + "");
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/favorites").
                then().
                log().all().
                extract();
    }

    @DisplayName("즐겨찾기 목록 조회 요청")
    @Test
    void findAllFavorite() {
        // given
        // 즐겨찾기 생성됨

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured.given().log().all().
                when().
                accept(MediaType.APPLICATION_JSON_VALUE).
                get("/favorites").
                then().
                log().all().
                extract();
    }

    @DisplayName("즐겨찾기 삭제 요청")
    @Test
    void removeFavorite() {
        // when
        long favoriteId = 1L;
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(favoriteId);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(long favoriteId) {
        return RestAssured.given().log().all().
                when().
                delete("/favorites/{id}", favoriteId + "").
                then().
                log().all().
                extract();
    }
}