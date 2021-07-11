package nextstep.subway.favorite.domain;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.AuthToken;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.IdTransferObject;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

public class FavoriteTestSnippet {
    public static ExtractableResponse<Response> 즐겨찾기_생성(AuthToken token, Station source, Station target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }


    public static void 즐겨찾기_생성_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
    }

    private static void 즐겨찾기_생성된_ID_할당(ExtractableResponse<Response> createFavoriteResponse, IdTransferObject ido) {
        String createdId = createFavoriteResponse.header("Location").split("/")[2];
        ido.changeId(Long.parseLong(createdId));
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(AuthToken token) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_조회_성공_확인(ExtractableResponse<Response> response, Station source, Station target) {
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);


        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(source.getId());
        assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(target.getId());
    }

    public static Executable 즐겨찾기_생성_및_성공_확인(AuthToken token, Station source, Station target, IdTransferObject ido) {
        return () -> {
            // when
            ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성(token, source, target);

            // then
            즐겨찾기_생성_성공_확인(createFavoriteResponse);
            즐겨찾기_생성된_ID_할당(createFavoriteResponse, ido);
        };
    }

    public static Executable 유효하지_않은_즐겨찾기_생성_및_실패_확인(AuthToken token, Station source, Station target, IdTransferObject ido) {
        return () -> {
            // when
            ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_생성(token, source, target);

            // then
            즐겨찾기_생성_실패_확인(createFavoriteResponse);
        };
    }

    public static Executable 즐겨찾기_조회_및_성공_확인(AuthToken token, Station source, Station target) {
        return () -> {
            // when
            ExtractableResponse<Response> findFavoriteResponse = 즐겨찾기_조회(token);

            // then
            즐겨찾기_조회_성공_확인(findFavoriteResponse, source, target);
        };
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(AuthToken token, Long id) {
        System.out.println("즐겨찾기 삭제 ### " + id);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    private static void 즐겨찾기_삭제_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }
    public static Executable 즐겨찾기_삭제_및_성공_확인(AuthToken authToken, IdTransferObject ido) {
        return () -> {
            // when
            ExtractableResponse<Response> deleteFavoriteResponse = 즐겨찾기_삭제(authToken, ido.getId());

            // then
            즐겨찾기_삭제_성공_확인(deleteFavoriteResponse);
        };
    }

    public static Executable 타_회원_즐겨찾기_삭제_및_실패_확인(AuthToken authToken, IdTransferObject ido) {
        return () -> {
            // when
            ExtractableResponse<Response> createFavoriteResponse = 즐겨찾기_삭제(authToken, ido.getId());

            // then
            즐겨찾기_삭제_실패_확인(createFavoriteResponse);
        };
    }


}
