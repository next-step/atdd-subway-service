package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

	@DisplayName("지하철 최적경로를 조회힌다.")
	@Test
	void paths() {
		//given
		Map<String, Long> params = new HashMap<>();
		params.put("source", 1L);
		params.put("target", 2L);

		//when
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .params(params)
			  .when().get("/paths")
			  .then().log().all()
			  .extract();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//		PathResponseDto responseBody = response.body().as(PathResponseDto.class);
//		assertThat(responseBody).isNotNull();
	}
}

