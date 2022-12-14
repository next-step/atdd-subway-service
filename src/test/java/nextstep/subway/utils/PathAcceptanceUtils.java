package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.dto.StationResponse;

public class PathAcceptanceUtils {

	private static final String PATHS_URL = "/paths";

	public static ExtractableResponse<Response> 최단_경로_조회(Long sourceId, Long targetId) {
		return RestAssured.given().log().all()
			.param("source", sourceId)
			.param("target", targetId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get(PATHS_URL)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 최단_경로_조회(String accessToken, Long sourceId, Long targetId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.param("source", sourceId)
			.param("target", targetId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get(PATHS_URL)
			.then().log().all()
			.extract();
	}

	public static void 지하철역_최단_경로_포함됨(ExtractableResponse<Response> response,
		List<StationResponse> expectedStations, int expectedDistance, int expectedFare) {
		PathResponse path = response.as(PathResponse.class);
		assertAll(
			() -> assertThat(path.getDistance()).isEqualTo(expectedDistance),
			() -> assertThat(path.getFare()).isEqualTo(expectedFare),
			() -> assertThat(path.getStations()).hasSize(expectedStations.size()),
			() -> assertThat(path.getStations())
				.doesNotHaveDuplicates()
				.extracting(PathStationResponse::getId)
				.containsExactly(expectedStations.stream()
					.map(StationResponse::getId)
					.toArray(Long[]::new))
		);
	}

	public static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode())
			.isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static void 최단_경로_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode())
			.isEqualTo(HttpStatus.OK.value());
	}

}
