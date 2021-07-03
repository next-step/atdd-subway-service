package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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

	private LineAcceptanceTest lineAcceptanceTest = new LineAcceptanceTest();
	private StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();
	private LineResponse 신분당선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 정자역;
	private StationResponse 광교역;
	private StationResponse 성수역;
	private StationResponse 뚝섬역;

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = stationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = stationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		정자역 = stationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
		광교역 = stationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
		성수역 = stationAcceptanceTest.지하철역_등록되어_있음("성수역").as(StationResponse.class);
		뚝섬역 = stationAcceptanceTest.지하철역_등록되어_있음("뚝섬역").as(StationResponse.class);

		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 500);
		신분당선 = lineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
	}

	@DisplayName("지하철_구간_관리")
	@Test
	void 지하철_구간_관리() {
		// when 앞을 기준으로 중간에 지하철역 등록 요청
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
		// then
		ExtractableResponse<Response> response = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_등록됨(response);
		지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));

		//when 가장 앞에 지하철 역 등록 요청
		지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);
		// then
		ExtractableResponse<Response> response2 = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_등록됨(response2);
		지하철_노선에_지하철역_순서_정렬됨(response2, Arrays.asList(정자역, 강남역, 양재역, 광교역));

		//when 뒤를 기준으로 중간에 지하철 역 등록 요청
		지하철_노선에_지하철역_등록_요청(신분당선, 성수역, 양재역, 1);
		// then
		ExtractableResponse<Response> response3 = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_등록됨(response3);
		지하철_노선에_지하철역_순서_정렬됨(response3, Arrays.asList(정자역, 강남역, 성수역, 양재역, 광교역));

		//when 가장 뒤에 지하철 역 등록 요청
		지하철_노선에_지하철역_등록_요청(신분당선, 광교역, 뚝섬역, 1);
		// then
		ExtractableResponse<Response> response4 = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_등록됨(response4);
		지하철_노선에_지하철역_순서_정렬됨(response4, Arrays.asList(정자역, 강남역, 성수역, 양재역, 광교역, 뚝섬역));

		// when
		ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

		// then
		지하철_노선에_지하철역_제외됨(removeResponse);
		ExtractableResponse<Response> response5 = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_순서_정렬됨(response5, Arrays.asList(정자역, 강남역, 성수역, 광교역, 뚝섬역));
	}

	@DisplayName("지하철 구간을 등록한다.")
	@Test
	void addLineSection() {
		// when
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

		// then
		ExtractableResponse<Response> response = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_등록됨(response);
		지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));
	}

	@DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
	@Test
	void addLineSection2() {
		// when
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
		지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

		// then
		ExtractableResponse<Response> response = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_등록됨(response);
		지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));
	}

	@DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
	@Test
	void addLineSectionWithSameStation() {
		// when
		ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

		// then
		지하철_노선에_지하철역_등록_실패됨(response);
	}

	@DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
	@Test
	void addLineSectionWithNoStation() {
		// when
		ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

		// then
		지하철_노선에_지하철역_등록_실패됨(response);
	}

	@DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
	@Test
	void removeLineSection1() {
		// given
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
		지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 2);

		// when
		ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

		// then
		지하철_노선에_지하철역_제외됨(removeResponse);
		ExtractableResponse<Response> response = lineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 정자역, 광교역));
	}

	@DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
	@Test
	void removeLineSection2() {
		// when
		ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
		// then
		지하철_노선에_지하철역_제외_실패됨(removeResponse);
	}

	public ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation,
		StationResponse downStation, int distance) {
		SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
		return post(sectionRequest, "/lines/" + line.getId() + "/sections");
	}

	public void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
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

	public ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
		return delete("/lines/"+ line.getId() +"/sections?stationId=" + station.getId());
	}

	public void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

}
