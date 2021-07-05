package nextstep.subway.path.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceStep {
    public static void 지하철_시작_종료지점이_경로에_포함됨(PathResponse response, StationResponse upStation, StationResponse downStation) {
        assertThat(response.getStations()).extracting("id").contains(upStation.getId(), downStation.getId());
    }

    public static PathResponse 지하철_경로_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(".", PathResponse.class);
    }

    public static void 지하철_경로_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(StationResponse upStation, StationResponse downStation) {
        return RestAssured
                .given().log().all()
                .when().get("/paths/?source={source}&target={target}", upStation.getId(), downStation.getId())
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(TokenResponse tokenResponse, StationResponse upStation, StationResponse downStation) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/paths/?source={source}&target={target}", upStation.getId(), downStation.getId())
                .then().log().all().extract();
    }

    public static void 총_거리도_함께_응답함(PathResponse response) {
        assertThat(response.getDistance()).isNotZero();
    }

    public static void 지하철_이용_요금도_함께_응답함(PathResponse response, int estimateFare, int age) {
        int expected = discountFare(estimateFare, age);
        assertThat(response.getFare()).isEqualTo(expected);
    }

    private static int discountFare(int fare, int age) {
        if (age == 0 || age > 20) {
            return fare;
        }
        if (age > 13) {
            return (fare - 350) * 80 / 100;
        }
        if (age > 6) {
            return (fare - 350) * 50 / 100;
        }
        return 0;
    }

}
