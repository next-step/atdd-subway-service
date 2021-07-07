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
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
	private LineResponse 신분당선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 정자역;
	private StationResponse 광교역;

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
		광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

		LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선_요청).as(LineResponse.class);
	}

	@Test
	void 시나리오_지하철_구간을_등록한다() {
		// When: 신분당선에 간격이 3이고, 강남역을 상행으로 한 양재역을 등록한다.
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

		// Then: 신분당선에 지하철역이 등록된다.
		// And: 강남역, 양재역, 광교역 순으로 순서가 정렬되어 있다.
		ExtractableResponse<Response> 신분당선 = LineAcceptanceTest.지하철_노선_조회_요청(this.신분당선);
		지하철_노선에_지하철역_등록됨(신분당선);
		지하철_노선에_지하철역_순서_정렬됨(신분당선, Arrays.asList(강남역, 양재역, 광교역));
	}

	@Test
	void 시나리오_지하철_노선에_여러_개의_역을_순서_상관_없이_등록한다() {
		// When: 신분당선에 간격이 2이고, 강남역을 상행으로 한 양재역을 등록한다.
		// And: 신분당선에 간격이 5이고, 강남역을 하행으로 한 정자역을 등록한다.
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
		지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

		// Then: 신분당선에 지하철역이 등록된다.
		// And: 정자역, 강남역, 양재역, 광교역 순으로 순서가 정렬되어 있다.
		ExtractableResponse<Response> 신분당선 = LineAcceptanceTest.지하철_노선_조회_요청(this.신분당선);
		지하철_노선에_지하철역_등록됨(신분당선);
		지하철_노선에_지하철역_순서_정렬됨(신분당선, Arrays.asList(정자역, 강남역, 양재역, 광교역));
	}

	@Test
	void 시나리오_지하철_노선에_이미_등록되어있는_역을_등록한다() {
		// When: 신분당선에 간격이 3이고, 이미 등록된 강남역과 광교역을 등록한다.
		ExtractableResponse<Response> 등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

		// Then: 역 등록에 실패한다.
		지하철_노선에_지하철역_등록_실패됨(등록_응답);
	}

	@Test
	void 시나리오_지하철_노선에_등록되지_않은_역을_기준으로_등록한다() {
		// When: 신분당선에 간격이 3이고, 노선에 등록되지 않은 정자역과 양재역을 등록한다.
		ExtractableResponse<Response> 등록_응답 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

		// Then: 역 등록에 실패한다.
		지하철_노선에_지하철역_등록_실패됨(등록_응답);
	}

	@Test
	void 시나리오_지하철_노선에_등록된_지하철역을_제외한다() {
		// Given: 신분당선에 정자역, 강남역, 양재역, 광교역 순으로 등록되어 있다.
		지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 2);
		지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 정자역, 2);

		// When: 신분당선에 양재역을 제외한다.
		ExtractableResponse<Response> 제외_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

		// Then: 신분당선에 양재역이 제외된다.
		// And: 강남역, 정자역, 광교역 순으로 순서가 정렬되어 있다.
		지하철_노선에_지하철역_제외됨(제외_응답);
		ExtractableResponse<Response> 신분당선 = LineAcceptanceTest.지하철_노선_조회_요청(this.신분당선);
		지하철_노선에_지하철역_순서_정렬됨(신분당선, Arrays.asList(강남역, 정자역, 광교역));
	}

	@Test
	void 시나리오_지하철_노선에_등록된_지하철역이_두개일_때_한_역을_제외한다() {
		// When: 신분당선에 강남역을 제외한다.
		ExtractableResponse<Response> 제외_응답 = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

		// Then: 신분당선에 강남역 제외하지 못한다.
		지하철_노선에_지하철역_제외_실패됨(제외_응답);
	}
	public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation,
			StationResponse downStation, int distance) {
		SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(sectionRequest)
			.when().post("/lines/{lineId}/sections", line.getId())
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation,
			StationResponse downStation, int distance) {
		return 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
	}

	public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
		List<StationResponse> expectedStations) {
		LineResponse line = response.as(LineResponse.class);
		List<Long> stationIds = line.getStations().stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		List<Long> expectedStationIds = expectedStations.stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
	}

	public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
		return RestAssured
			.given().log().all()
			.when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
			.then().log().all()
			.extract();
	}

	public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
