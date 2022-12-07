package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PathAcceptanceUtils {

	private static final String PATHS_URL = "/paths";

	public static ExtractableResponse<Response> 최단_경로_조회(Long sourceId, Long targetId) {
		return RestAssured.given().log().all()
			.param("source", sourceId)
			.param("target", targetId)
			.when()
			.get(PATHS_URL)
			.then().log().all()
			.extract();
	}

	public static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode())
			.isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

}
