package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.acceptance.LineAcceptanceSupport;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceSupport {
    public static ExtractableResponse<Response> 로그인_최단_경로를_조회한다(String accessToken, Long source, Long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .param("source", source)
                .param("target", target)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비로그인_최단_경로를_조회한다(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .param("source", source)
                .param("target", target)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static LineResponse 지하철_노선_등록되어_있음(
            String name, String color,
            StationResponse upStation, StationResponse downStation,
            int distance, int additionalFare
    ) {
        return LineAcceptanceSupport.지하철_노선_등록되어_있음(
            new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, additionalFare)
        ).as(LineResponse.class);
    }
}
