package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathTestFixture {

    public static ExtractableResponse<Response> 출발역에서_도착역_경로_조회됨(StationResponse source, StationResponse target) {
        return 출발역에서_도착역_경로_조회_요청(source.getId(), target.getId());
    }

    public static ExtractableResponse<Response> 출발역에서_도착역_경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static void 경로_조회_요청_성공됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 경로_조회_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_이용_요금이_응답됩(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("fare")).isNotNull();
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("distance")).isNotNull();
    }

    public static void 최단_거리_경로가_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getList("stations")).isNotEmpty();
    }
}
