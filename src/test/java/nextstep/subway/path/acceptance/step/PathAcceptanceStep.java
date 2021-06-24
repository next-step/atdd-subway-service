package nextstep.subway.path.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceStep {
    public static void 지하철_시작_종료지점이_경로에_포함됨(ExtractableResponse<Response> response, StationResponse upStation, StationResponse downStation) {
        PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        assertThat(actual.getStations()).extracting("id").contains(upStation.getId(), downStation.getId());
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
}
