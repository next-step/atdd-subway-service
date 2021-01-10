package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceSupport;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceSupport {
	private PathAcceptanceSupport() {
	}

	public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int additionalFee) {
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance));
		return response.body().as(LineResponse.class);
	}

	public static void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
		LineSectionAcceptanceSupport.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
	}

	public static ExtractableResponse<Response> 지하철_경로_조회_요청(StationResponse source, StationResponse target) {
		return RestAssured
				.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("source", source.getId())
				.queryParam("target", target.getId())
				.when().get("/paths")
				.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_경로_조회_요청_회원용(String accessToken, StationResponse source, StationResponse target) {
		return RestAssured
				.given().log().all().auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("source", source.getId())
				.queryParam("target", target.getId())
				.when().get("/paths")
				.then().log().all().extract();
	}

	public static void 지하철_경로_조회됨(ExtractableResponse<Response> response, List<String> stations, int distance, int fare) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		PathResponse pathResponse = response.body().as(PathResponse.class);
		assertThat(pathResponse.getStations())
				.map(StationResponse::getName)
				.asList()
				.containsAll(stations);
		assertThat(pathResponse.getDistance()).isEqualTo(distance);
		assertThat(pathResponse.getFare()).isEqualTo(fare);
	}

	public static void 지하철_경로_조회_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
