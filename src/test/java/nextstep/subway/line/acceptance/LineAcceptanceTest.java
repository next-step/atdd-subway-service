package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
	private StationResponse 종로3가역;
	private StationResponse 신길역;
	private StationResponse 충무로역;
	private LineRequest 일호선_요청;
	private LineRequest 삼호선_요청;
	private LineRequest 오호선_요청;

	@BeforeEach
	public void setUp() {
		super.setUp();

		// given
		종로3가역 = StationAcceptanceTest.지하철역_등록되어_있음("종로3가역").as(StationResponse.class);
		신길역 = StationAcceptanceTest.지하철역_등록되어_있음("신길역").as(StationResponse.class);
		충무로역 = StationAcceptanceTest.지하철역_등록되어_있음("충무로역").as(StationResponse.class);

		일호선_요청 = new LineRequest("1호선", "bg-blue-600", 종로3가역.getId(), 신길역.getId(), 100);
		삼호선_요청 = new LineRequest("3호선", "bg-orange-600", 종로3가역.getId(), 충무로역.getId(), 300);
		오호선_요청 = new LineRequest("5호선", "bg-purple-600", 종로3가역.getId(), 신길역.getId(), 100);
	}

	@Test
	void 시나리오_지하철_노선을_관리한다() {
		// When: 종로3가역과 신길역의 간격이 100이고 파란색인 1호선을 생성 요청한다.
		ExtractableResponse<Response> 일호선 = 지하철_노선_생성_요청(일호선_요청);

		// Then: 지하철 1호선이 생성된다.
		지하철_노선_생성됨(일호선);

		// When: 종로3가역과 신길역의 간격이 100이고 파란색인 이미 등록된 1호선을 생성 요청한다.
		ExtractableResponse<Response> 일호선_실패_응답 = 지하철_노선_생성_요청(일호선_요청);

		// Then: 이미 등록되었으므로 생성에 실패한다.
		지하철_노선_생성_실패됨(일호선_실패_응답);

		// When: 종로3가역과 충무로역의 간격이 300이고 주황색인 3호선을 생성 요청한다.
		ExtractableResponse<Response> 삼호선 = 지하철_노선_생성_요청(삼호선_요청);

		// Then: 지하철 3호선이 생성된다.
		지하철_노선_생성됨(삼호선);

		// When: 지하철 노선 목록을 조회한다.
		ExtractableResponse<Response> 노선_목록 = 지하철_노선_목록_조회_요청();

		// Then: 지하철 1호선, 3호선이 조회된다.
		지하철_노선_목록_응답됨(노선_목록);
		지하철_노선_목록_포함됨(노선_목록, Arrays.asList(일호선, 삼호선));

		// When: 지하철 1호선을 조회한다.
		ExtractableResponse<Response> 노선_조회_응답 = 지하철_노선_목록_조회_요청(일호선);

		// Then: 지하철 1호선이 조회된다.
		지하철_노선_응답됨(노선_조회_응답, 일호선);

		// When: 지하철 1호선을 보라색인 5호선으로 수정한다.
		ExtractableResponse<Response> 수정_응답 = 지하철_노선_수정_요청(일호선, 오호선_요청);

		// Then: 지하철 보라색 5호선으로 수정된다.
		지하철_노선_수정됨(수정_응답);

		// When: 지하철 3호선을 제거한다.
		ExtractableResponse<Response> 제거_응답 = 지하철_노선_제거_요청(삼호선);

		// Then: 지하철 3호선이 제거된다.
		지하철_노선_삭제됨(제거_응답);
	}

	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() {
		// when
		ExtractableResponse<Response> 일호선_응답 = 지하철_노선_생성_요청(일호선_요청);

		// then
		지하철_노선_생성됨(일호선_응답);
	}

	@DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
	@Test
	void createLineWithDuplicateName() {
		// given
		지하철_노선_등록되어_있음(일호선_요청);

		// when
		ExtractableResponse<Response> 일호선_응답 = 지하철_노선_생성_요청(일호선_요청);

		// then
		지하철_노선_생성_실패됨(일호선_응답);
	}

	@DisplayName("지하철 노선 목록을 조회한다.")
	@Test
	void getLines() {
		ExtractableResponse<Response> 일호선_응답 = 지하철_노선_등록되어_있음(일호선_요청);
		ExtractableResponse<Response> 삼호선_응답 = 지하철_노선_등록되어_있음(삼호선_요청);

		// when
		ExtractableResponse<Response> 노선_목록 = 지하철_노선_목록_조회_요청();

		// then
		지하철_노선_목록_응답됨(노선_목록);
		지하철_노선_목록_포함됨(노선_목록, Arrays.asList(일호선_응답, 삼호선_응답));
	}

	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void getLine() {
		// given
		ExtractableResponse<Response> 일호선_응답 = 지하철_노선_등록되어_있음(일호선_요청);

		// when
		ExtractableResponse<Response> 노선_조회_응답 = 지하철_노선_목록_조회_요청(일호선_응답);

		// then
		지하철_노선_응답됨(노선_조회_응답, 일호선_응답);
	}

	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLine() {
		// given
		ExtractableResponse<Response> 일호선_응답 = 지하철_노선_등록되어_있음(일호선_요청);

		// when
		ExtractableResponse<Response> 수정_응답 = 지하철_노선_수정_요청(일호선_응답, 오호선_요청);

		// then
		지하철_노선_수정됨(수정_응답);
	}

	@DisplayName("지하철 노선을 제거한다.")
	@Test
	void deleteLine() {
		// given
		ExtractableResponse<Response> 일호선_응답 = 지하철_노선_등록되어_있음(일호선_요청);

		// when
		ExtractableResponse<Response> 제거_응답 = 지하철_노선_제거_요청(일호선_응답);

		// then
		지하철_노선_삭제됨(제거_응답);
	}

	public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest params) {
		return 지하철_노선_생성_요청(params);
	}

	public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/lines")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
		return 지하철_노선_목록_조회_요청("/lines");
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");

		return 지하철_노선_목록_조회_요청(uri);
	}

	private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String uri) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/lines/{lineId}", response.getId())
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response,
		LineRequest params) {
		String uri = response.header("Location");

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().put(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");

		return RestAssured
			.given().log().all()
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	public static void 지하철_노선_생성됨(ExtractableResponse response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
	}

	public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 지하철_노선_응답됨(ExtractableResponse<Response> response,
		ExtractableResponse<Response> createdResponse) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.as(LineResponse.class)).isNotNull();
	}

	public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
		List<ExtractableResponse<Response>> createdResponses) {
		List<Long> expectedLineIds = createdResponses.stream()
			.map(it -> Long.parseLong(it.header("Location").split("/")[2]))
			.collect(Collectors.toList());

		List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
			.map(LineResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultLineIds).containsAll(expectedLineIds);
	}

	public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
