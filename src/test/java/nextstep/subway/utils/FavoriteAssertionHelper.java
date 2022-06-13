package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;

public class FavoriteAssertionHelper {
    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> 즐겨찾기_생성요청_response) {
        Assertions.assertThat(즐겨찾기_생성요청_response.statusCode())
            .isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록조회_결과확인(ExtractableResponse<Response> 즐겨찾기_목록조회요청_response) {
        Assertions.assertThat(즐겨찾기_목록조회요청_response.jsonPath().getList(""))
            .extracting("source.name")
            .containsExactly("교대역", "남부터미널역");
        Assertions.assertThat(즐겨찾기_목록조회요청_response.jsonPath().getList(""))
            .extracting("target.name")
            .containsExactly("양재역","양재역");
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> 즐겨찾기_삭제요청_response) {
        Assertions.assertThat(즐겨찾기_삭제요청_response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
