package nextstep.subway.favorite;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoritePathRequest;

public class FavoriteAcceptanceMethods {
    public static final String LOCATION_HEADER_NAME = "Location";
    private static final String FAVORITE_URL_PATH = "/favorites";

    private FavoriteAcceptanceMethods() {}

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse token, FavoritePathRequest request) {
        return postByAuth(FAVORITE_URL_PATH, request, token);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
