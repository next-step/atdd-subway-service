package nextstep.subway.favorite;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.request.AcceptanceTestRequest;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceRequest {

    public static Executable 즐겨찾기_등록_요청_및_등록됨(AuthToken authToken, FavoriteRequest request, Long exceptId) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_등록_요청(authToken, request);

            즐겨찾기_등록됨(response, exceptId);
        };
    }

    public static Executable 즐겨찾기_목록_요청_및_조회됨(AuthToken authToken, FavoriteResponse ...responses) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_목록_요청(authToken);

            즐겨찾기_목록_조회됨(response, responses);
        };
    }

    public static Executable 즐겨찾기_등록_요청_및_실패됨(AuthToken authToken, FavoriteRequest request) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_등록_요청(authToken, request);

            즐겨찾기_등록_실패됨(response);
        };
    }

    public static Executable 비로그인시_즐겨찾기_등록_요청_및_실패됨(AuthToken authToken, FavoriteRequest request) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_등록_요청(authToken, request);

            비로그인_즐겨찾기_실패됨(response);
        };
    }

    public static Executable 비로그인시_즐겨찾기_목록_요청_및_실패됨(AuthToken authToken) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_목록_요청(authToken);

            비로그인_즐겨찾기_실패됨(response);
        };
    }

    public static Executable 비로그인시_즐겨찾기_삭제_요청_및_실패됨(AuthToken authToken, Long id) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(authToken, id);

            비로그인_즐겨찾기_실패됨(response);
        };
    }

    public static Executable 즐겨찾기_목록_요청_및_비어있음(AuthToken authToken) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_목록_요청(authToken);

            즐겨찾기_목록_빔(response);
        };
    }

    public static Executable 즐겨찾기_삭제_요청_및_삭제됨(AuthToken authToken, Long favoriteId) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(authToken, favoriteId);

            즐겨찾기_삭제됨(response);
        };
    }

    public static Executable 즐겨찾기_삭제_요청_및_거절됨(AuthToken authToken, Long favoriteId) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(authToken, favoriteId);

            즐겨찾기_삭제_거절됨(response);
        };
    }

    private static ExtractableResponse<Response> 즐겨찾기_목록_요청(AuthToken authToken) {
        return AcceptanceTestRequest.get(
                Given.builder().bearer(authToken.getToken()).build() ,
                When.builder().uri("/favorites").build()
        );
    }

    private static ExtractableResponse<Response> 즐겨찾기_삭제_요청(AuthToken authToken, Long favoriteId) {
        return AcceptanceTestRequest.delete(
                Given.builder().bearer(authToken.getToken()).build(),
                When.builder()
                        .uri(format("/favorites/%d", favoriteId))
                        .build()
        );
    }

    private static ExtractableResponse<Response> 즐겨찾기_등록_요청(AuthToken authToken, FavoriteRequest request) {
        return AcceptanceTestRequest.post(
                Given.builder()
                        .bearer(authToken.getToken())
                        .body(request)
                        .contentType(ContentType.JSON)
                        .build(),
                When.builder()
                        .uri("/favorites")
                        .build()
        );
    }

    private static void 즐겨찾기_등록됨(ExtractableResponse<Response> response, Long exceptId) {
        String uri = response.header("Location");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(uri).isEqualTo("/favorites/" + exceptId);
    }

    private static void 즐겨찾기_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 즐겨찾기_삭제_거절됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private static void 비로그인_즐겨찾기_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, FavoriteResponse[] responses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(FavoriteResponse[].class))
                .containsExactlyInAnyOrder(responses);
    }

    private static void 즐겨찾기_목록_빔(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
