package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import com.sun.tools.javac.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

public class PathStaticAcceptance {

	private static final String PATHS_PATH = "/paths";

	public static ExtractableResponse<Response> 지하철역_사이의_최단경로를_조회한다(PathRequest params) {
		return RestAssured.given().log().all()
			.param("source", params.getSource())
			.param("target", params.getTarget())
			.when().get(PATHS_PATH)
			.then().log().all()
			.extract();
	}

	public static void 지하철역_사이의_최단경로가_조회됨(ExtractableResponse<Response> response, int distance) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(distance)
		);
	}

	public static void 지하철역_사이의_경로가_조회됨(ExtractableResponse<Response> response, StationResponse... stationResponses) {
		List<Long> responseList = response.as(PathResponse.class).getStations()
			.stream()
			.map(StationResponse::getId)
			.collect(List.collector());

		List<Long> stationsList = Arrays.stream(stationResponses)
			.map(StationResponse::getId)
			.collect(List.collector());

		assertThat(responseList).containsAll(stationsList);
	}

	public static PathRequest 최단경로_조회_요청(Long source, Long target) {
		return new PathRequest(source, target);
	}

	public static void 지하철역_사이의_경로_조회가_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

}
