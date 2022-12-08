package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceStep {

    public static ExtractableResponse<Response> 최단_경로_조회_요청(String token, long sourceId, long targetId) {
        if(token == null) {
            return 비회원_최단경로조회요청(sourceId, targetId);
        }
        return 로그인회원_최단경로조회요청(token, sourceId, targetId);
    }

    private static ExtractableResponse<Response> 비회원_최단경로조회요청(final long sourceId, final long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("sourceId", sourceId)
                .param("targetId", targetId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 로그인회원_최단경로조회요청(final String token, final long sourceId, final long targetId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("sourceId", sourceId)
                .param("targetId", targetId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단경로_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단경로_목록_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
