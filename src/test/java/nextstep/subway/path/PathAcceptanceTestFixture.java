package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathAcceptanceTestFixture {
    public static ExtractableResponse<Response> 최단_경로_조회_요청(String 사용자, StationResponse source, StationResponse target) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(사용자)
                .when().get("/paths?source={source}&target={target}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStations) {
        List<Long> expectStationsIds = expectStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualStationIds).containsAll(expectStationsIds)
        );
    }

    public static void 총_거리_조회됨(ExtractableResponse<Response> response, int expectTotalDistance) {
        int responseTotalDistance = response.jsonPath().getInt("distance");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseTotalDistance).isEqualTo(expectTotalDistance)
        );
    }

    public static void 이용_요금_조회됨(ExtractableResponse<Response> response, int expectedTotalFare) {
        int responseTotalFare = response.jsonPath().getInt("Fare");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseTotalFare).isEqualTo(expectedTotalFare)
        );
    }

    public static void 최단_경로_조회_실패됨_400(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 최단_경로_조회_실패됨_404(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
