package nextstep.subway.path.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

	@DisplayName("최단 경로 조회")
	@Test
	void 최단_경로_조회() {
		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/paths?source={source}&target={target}", 1L, 6L)
			.then().log().all().extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		List<StationResponse> stationResponses = response.jsonPath().getList("stations", StationResponse.class);
		assertThat(stationResponses).extracting("name").containsExactly("서울역", "광명역", "대전역", "동대구역", "부산역");

		int distance = response.jsonPath().getInt("distance");
		assertThat(distance).isEqualTo(40);
	}

}
