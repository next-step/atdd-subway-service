package nextstep.subway.favorite;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import nextstep.subway.utils.StreamUtils;

public class FavoriteAcceptanceMethods {
    private static final String LOCATION_HEADER_NAME = "Location";
    private static final String FAVORITE_URL_PATH = "/favorites";
    private static final String SLASH_SIGN = "/";

    private FavoriteAcceptanceMethods() {}

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse token, FavoritePathRequest request) {
        return postByAuth(FAVORITE_URL_PATH, request, token);
    }

    public static ExtractableResponse<Response> 즐겨찾기_등록됨(TokenResponse token, FavoritePathRequest request) {
        return 즐겨찾기_생성_요청(token, request);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
        return getByAuth(FAVORITE_URL_PATH, token);
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(ExtractableResponse<Response> response,
                                                                TokenResponse token) {
        String uri = response.header(LOCATION_HEADER_NAME);
        return getByAuth(uri, token);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id, TokenResponse token) {
        return deleteByAuth(FAVORITE_URL_PATH + SLASH_SIGN + id, token);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response, List<FavoritePathResponse> expecteds) {
        List<Long> expectedIds = StreamUtils.mapToList(expecteds, FavoritePathResponse::getId);
        List<Long> resultIds = StreamUtils.mapToList(response.jsonPath()
                                                                 .getList(".", FavoritePathResponse.class),
                                                         FavoritePathResponse::getId);
        assertThat(resultIds).containsAll(expectedIds);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
