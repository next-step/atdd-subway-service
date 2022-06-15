package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceSupport {
    public static ExtractableResponse<Response> 지하철역_최단경로_조회_요청(String accessToken, Long source, Long target) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", source)
            .param("target", target)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 비회원_지하철역_최단경로_조회_요청(String accessToken, Long source, Long target) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", source)
            .param("target", target)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    public static void 지하철역_최단거리_경로_검증_완료(ExtractableResponse<Response> response, List<String> stationNames) {
        List<String> 지하철역_최단거리_경로_응답 = toStationNames(response.as(PathResponse.class).getStations());
        assertThat(지하철역_최단거리_경로_응답).isEqualTo(stationNames);
    }

    public static void 지하철역_최단거리_길이_검증_완료(ExtractableResponse<Response> response, int distance) {
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance);
    }

    public static void 지하철역_최단거리_경로_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_요금_검증_완료(ExtractableResponse<Response> response, int fare) {
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare);
    }

    public static List<String> toStationNames(List<StationResponse> stationResponses) {
        return stationResponses.stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());
    }
}