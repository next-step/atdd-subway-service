package nextstep.subway.favorite.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

public final class FavoriteAcceptanceStep {

    private FavoriteAcceptanceStep() {
        throw new AssertionError();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(
        String accessToken, Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .body(new FavoriteRequest(sourceId, targetId))
            .contentType(ContentType.JSON)
            .when()
            .post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when()
            .get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response,
        StationResponse expectedSource, StationResponse expectedTarget) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> 즐겨찾기_목록_포함됨(response, expectedSource, expectedTarget)
        );
    }

    public static void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response,
        StationResponse expectedSource, StationResponse expectedTarget) {
        List<FavoriteResponse> favorites = response.as(new TypeRef<List<FavoriteResponse>>() {
        });
        assertThat(favorites)
            .singleElement()
            .satisfies(favorite ->
                assertAll(
                    () -> assertThat(favorite.getId()).isNotNull(),
                    () -> assertThat(favorite.getSource())
                        .extracting(StationResponse::getId, StationResponse::getName)
                        .containsExactly(expectedSource.getId(), expectedSource.getName()),
                    () -> assertThat(favorite.getTarget())
                        .extracting(StationResponse::getId, StationResponse::getName)
                        .containsExactly(expectedTarget.getId(), expectedTarget.getName())
                )
            );
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(
        String accessToken, ExtractableResponse<Response> createResponse) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when()
            .delete(createResponse.header("Location"))
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
