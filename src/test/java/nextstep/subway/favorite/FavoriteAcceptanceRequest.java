package nextstep.subway.favorite;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.request.AcceptanceTestRequest;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceRequest {

    public static Executable 즐겨찾기_등록_요청_및_등록됨(AuthToken authToken, FavoriteRequest request, Long exceptId) {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_등록_요청(authToken, request);

            즐겨찾기_등록됨(response, exceptId);
        };
    }

    public static Executable 즐겨찾기_목록_요청_및_조회됨(AuthToken authToken, FavoriteRequest request) {
        return () -> {

        };
    }

    public static Executable 즐겨찾기_등록_요청_및_실패됨(AuthToken authToken, FavoriteRequest request) {
        return () -> {

        };
    }

    public static Executable 즐겨찾기_목록_요청_및_비어있음(AuthToken authToken) {
        return () -> {

        };
    }

    public static Executable 즐겨찾기_삭제_요청_및_삭제됨(AuthToken authToken, Long favoriteId) {
        return () -> {

        };
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
}
