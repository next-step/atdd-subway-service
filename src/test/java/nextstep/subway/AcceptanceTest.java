package nextstep.subway;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest<T>{
	@LocalServerPort
	int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
	}

	protected ExtractableResponse<Response> post(T request, String uri) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post(uri)
			.then().log().all()
			.extract();
	}

	protected ExtractableResponse<Response> delete(String uri) {
		return RestAssured
			.given().log().all()
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	protected ExtractableResponse<Response> put(T request, String uri) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().put(uri)
			.then().log().all()
			.extract();
	}

	protected ExtractableResponse<Response> get(String uri) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(uri)
			.then().log().all()
			.extract();
	}

}
