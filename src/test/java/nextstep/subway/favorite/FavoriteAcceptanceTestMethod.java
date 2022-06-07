package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;

public class FavoriteAcceptanceTestMethod extends AcceptanceTest {

    private static final String FAVORITE_PATH = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse,
                                                           FavoriteRequest favoriteRequest) {
        return postWithAuth(FAVORITE_PATH, tokenResponse, favoriteRequest);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
