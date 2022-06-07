package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;

public class FavoriteAcceptanceTestMethod extends AcceptanceTest {

    private static final String FAVORITE_PATH = "/favorites";
    private static final String DOT = ".";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse,
                                                           FavoriteRequest favoriteRequest) {
        return postWithAuth(FAVORITE_PATH, tokenResponse, favoriteRequest);
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(TokenResponse tokenResponse) {
        return getWithAuth(FAVORITE_PATH, tokenResponse);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response,
                                List<ExtractableResponse<Response>> createdResponses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedFavoriteIds = createdResponses.stream()
                .map(AcceptanceTest::parseIdFromLocationHeader)
                .collect(Collectors.toList());

        List<Long> resultFavoriteIds = response.jsonPath().getList(DOT, FavoriteResponse.class).stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultFavoriteIds).containsAll(expectedFavoriteIds);
    }
}
