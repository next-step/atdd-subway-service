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
import nextstep.subway.BaseTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends BaseTest {
	public static final int 신분당선_노선_길이 = 1000;
	public static final int 강남역_양재역_사이_거리 = 10;
	private LineResponse 신분당선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 판교역;
	private StationResponse 정자역;
	private StationResponse 미금역;
	private StationResponse 광교역;

	@BeforeEach
	public void setUp() {
		super.setUp();

		//Background
		//지하철역 등록되어 있음
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
		정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
		미금역 = StationAcceptanceTest.지하철역_등록되어_있음("미금역").as(StationResponse.class);
		광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

		//Background
		//지하철노선 상행종점(강남역), 하행종점역(광교역)과 함께 등록되어 있음
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(),
			신분당선_노선_길이);
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

		//Background
		//신분당 노선에 강남역 - 양재역 구간이 추가되어 있음
		지하철_구간을_등록_요청(신분당선, 강남역, 양재역, 강남역_양재역_사이_거리);

	}

	@DisplayName("구간관리화면: 신분당선을 선택하면 신분당선에 등록된 역인 강남역, 양재역, 광교역이 순서대로 나타난다.")
	@Test
	void findLineById() {
		// when
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

		// then
		지하철_노선에_등록된_역_정상_조회됨(response);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));
	}

	@DisplayName("구간관리 팝업화면: 노선을 선택하면 역 전체 목록 6개를 가져온다.")
	@Test
	void selectLineAfter() {
		// when
		ExtractableResponse<Response> response = StationAcceptanceTest.지하철역_목록_조회_요청();

		// then
		StationAcceptanceTest.지하철역_목록_응답됨(response);
		StationAcceptanceTest.지하철역_목록_포함됨(response, Arrays.asList(강남역, 양재역, 판교역, 정자역, 미금역, 광교역));
	}

	@DisplayName("구간관리 팝업화면: 신분당선에 상행역에 판교역, 하행역에 강남역을 선택하면, 상행종점역이 판교역으로 교체된다.(상행종점교체케이스)")
	@Test
	void addLineSection1() {
		// when
		지하철_구간을_등록_요청(신분당선, 판교역, 강남역, 10);

		// then
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_등록된_역_정상_조회됨(response);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(판교역, 강남역, 양재역, 광교역));
	}

	@DisplayName("구간관리 팝업화면: 신분당선에 상행역에 광교역, 하행역에 미금역을 선택하면, 하행종점역이 미금역으로 교체된다.(하행종점교체케이스)")
	@Test
	void addLineSection2() {
		// when
		지하철_구간을_등록_요청(신분당선, 광교역, 미금역, 10);

		// then
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_등록된_역_정상_조회됨(response);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역, 미금역));
	}

	@DisplayName("구간관리 팝업화면: 신분당선에 상행역에 양재역, 하행역에 판교역을 선택하고 거리를 10으로 추가하면 구간이 추가된다.(상행역매치 중간역케이스)")
	@Test
	void addLineSection3() {
		// when
		지하철_구간을_등록_요청(신분당선, 양재역, 판교역, 10);

		// then
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_등록된_역_정상_조회됨(response);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 판교역, 광교역));
	}

	@DisplayName("구간관리 팝업화면: 신분당선에 상행역에 미금역, 하행역에 광교역을 선택하고 거리를 10으로 추가하면 구간이 추가된다.(하행역매치 중간역케이스)")
	@Test
	void addLineSection4() {
		// when
		지하철_구간을_등록_요청(신분당선, 미금역, 광교역, 10);

		// then
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_등록된_역_정상_조회됨(response);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 미금역, 광교역));
	}

	@DisplayName("구간관리화면: 신분당선을 선택한 후 상행 종점인 강남역을 제거한다.")
	@Test
	void removeLineSection1() {

		// when
		ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

		// then
		지하철_노선에_지하철역_제외됨(removeResponse);
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(양재역, 광교역));
	}

	@DisplayName("구간관리화면: 신분당선을 선택한 후 하행 종점인 광교역을 제거한다.")
	@Test
	void removeLineSection2() {

		// when
		ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);

		// then
		지하철_노선에_지하철역_제외됨(removeResponse);
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역));
	}

	@DisplayName("구간관리화면: 신분당선을 선택한 후 중간에 존재하는 양재역을 제거한다.")
	@Test
	void removeLineSection3() {

		// when
		ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

		// then
		지하철_노선에_지하철역_제외됨(removeResponse);
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_등록된_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 광교역));
	}

	@DisplayName("구간관리화면: 두개역이 남아 있을때 한역을 제거하면 실패된다.")
	@Test
	void removeLineSection4() {
		//given
		지하철_노선에_지하철역_제외_요청(신분당선, 광교역);

		// when
		ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

		// then
		지하철_노선에_지하철역_제외_실패됨(removeResponse);
	}

	@DisplayName("구간관리 팝업화면: 상행역, 하행역 모두 이미 등록되어 있는 역으로 선택하고 등록하면 실패한다.")
	@Test
	void addLineSectionWithSameStation() {
		// when
		ExtractableResponse<Response> response = 지하철_구간을_등록_요청(신분당선, 강남역, 광교역, 3);

		// then
		지하철_노선에_지하철역_등록_실패됨(response);
	}

	@DisplayName("구간관리 팝업화면: 상행역, 하행역 등록되어 있지 않은 역으로 선택하고 등록하면 실패한다.")
	@Test
	void addLineSectionWithNoStation() {
		// when
		ExtractableResponse<Response> response = 지하철_구간을_등록_요청(신분당선, 판교역, 정자역, 3);

		// then
		지하철_노선에_발견되지_않은역으로_실패됨(response);
	}

	@DisplayName("구간관리 팝업화면: 기존에 등록되어 있는 역과 역사이의 거리보다 큰 거리를 입력하면 구간 등록이 실패한다.")
	@Test
	void addLineSectionWrongDistance() {
		int 양재역_광교역_사이_거리 = 신분당선_노선_길이 - 강남역_양재역_사이_거리;

		// when
		ExtractableResponse<Response> response = 지하철_구간을_등록_요청(신분당선, 양재역, 미금역, 양재역_광교역_사이_거리 + 1);

		// then
		지하철_노선에_지하철역_등록_실패됨(response);
	}

	public static ExtractableResponse<Response> 지하철_구간을_등록_요청(LineResponse line, StationResponse upStation,
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

	public static void 지하철_노선에_등록된_역_정상_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static void 지하철_노선에_발견되지_않은역으로_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	public static void 지하철_노선에_등록된_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
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
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
