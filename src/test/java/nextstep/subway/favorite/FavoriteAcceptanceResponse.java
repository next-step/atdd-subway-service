package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceResponse {
    public static void 즐겨찾기_생성_요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 즐겨찾기_조회_요청_성공(ExtractableResponse<Response> response) {
        List<FavoriteResponse> list = response.jsonPath().getList(".");
        assertThat(list.size()).isEqualTo(2);
        assertThat(list).isNotEmpty();
    }
}
