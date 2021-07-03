package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.makeBearerToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathAcceptanceStep {
    public static final String PATHS = "/paths";
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String AUTHORIZATION = "Authorization";

    public static String makeBearerToken(String token) {
        return "Bearer " + token;
    }

    public static ExtractableResponse<Response> 로그인_유저의_지하철_최단_경로_조회_요청(Long startStationId, Long endStationId, TokenResponse 사용자) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, makeBearerToken(사용자.getAccessToken()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param(SOURCE, startStationId)
                .param(TARGET, endStationId)
                .when().get(PATHS)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비로그인_지하철_최단_경로_조회_요청(Long startStationId, Long endStationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param(SOURCE, startStationId)
                .param(TARGET, endStationId)
                .when().get(PATHS)
                .then().log().all()
                .extract();
    }

    public static void 지하철_최단_경로_확인(PathResponse actual, PathResponse expected) {
        assertAll(() -> {
            assertThat(actual.getDistance()).isEqualTo(expected.getDistance());
            assertThat(actual.getStations().size()).isEqualTo(expected.getStations().size());
            for (int i = 0; i < actual.getStations().size(); i++) {
                assertThat(actual.getStations().get(i)).isEqualTo(expected.getStations().get(i));
            }
        });
    }

    public static void 지하철_최단_경로_응답됨(ExtractableResponse<Response> 지하철_최단_경로_조회_요청_결과) {
        assertThat(지하철_최단_경로_조회_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최단_경로_예외_응답됨(ExtractableResponse<Response> 지하철_최단_경로_조회_요청_예외) {
        assertThat(지하철_최단_경로_조회_요청_예외.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_요금_확인(PathResponse 지하철_최단_경로_조회_요청_결과, int fare) {
        assertThat(지하철_최단_경로_조회_요청_결과.getFare()).isEqualTo(fare);
    }
}
