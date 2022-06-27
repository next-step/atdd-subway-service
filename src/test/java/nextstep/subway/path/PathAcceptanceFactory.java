package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;


public class PathAcceptanceFactory {
    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회_조회됨(ExtractableResponse<Response> response, int distance) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    public static void 최단_경로_조회_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 회원의_최단경로_조회_요청(Long sourceId, Long targetId, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static void 이용요금_조회(ExtractableResponse<Response> response, int fare) {
        PathResponse 거리응답결과 = response.as(PathResponse.class);
        assertThat(거리응답결과.getFare()).isEqualTo(fare);
    }
}
