package nextstep.subway.path;

import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;

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
		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

}
