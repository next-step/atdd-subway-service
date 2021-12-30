package nextstep.subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestPathFactory {
    public static ExtractableResponse<Response> 비로그인_지하철_최적_경로_요청(final Long source, final Long target) {
        return RestAssured.given().log().all()
                .queryParams("source", source, "target", target)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_지하철_최적_경로_요청(final String 토큰, final Long source, final Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰)
                .queryParams("source", source, "target", target)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 지하철_최적_경로_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_최적_경로_목록_포함됨(ExtractableResponse<Response> response, int 개수, double 거리, BigDecimal 요금, StationResponse... 역) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(개수),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(거리),
                () -> assertThat(pathResponse.getStations()).containsExactly(역),
                () -> assertThat(pathResponse.getFare()).isEqualTo(요금)
        );
    }

    public static void 지하철_최적_경로_응답_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
