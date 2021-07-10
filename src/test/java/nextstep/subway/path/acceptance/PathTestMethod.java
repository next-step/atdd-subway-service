package nextstep.subway.path.acceptance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;

public class PathTestMethod {
	public static ExtractableResponse<Response> findPath(Long startStationId, Long destinationStationId) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/path?source=" + startStationId + "&target=" + destinationStationId)
			.then().log().all()
			.extract();
	}

	public static List<Long> getIds(List<StationResponse> stations) {
		return stations.stream().map(it -> it.getId()).collect(Collectors.toList());
	}
}
