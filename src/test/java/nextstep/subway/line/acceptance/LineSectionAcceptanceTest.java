package nextstep.subway.line.acceptance;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import nextstep.subway.line.dto.SectionResponse;
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

		// Background

		// Given 지하철역 등록되어 있음
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
		광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

		// Given 지하철 노선 등록되어 있음 && 지하철 노선에 지하철역 등록되어 있음
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

	}

	@DisplayName("지하철 구간을 관리한다.")
	@Test
	void lineSectionManage() {
		// When 지하철 구간 등록 요청
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 정자역, 5);
		// Then 지하철 구간 등록됨
		지하철_노선에_지하철역_등록됨(LineAcceptanceTest.지하철_노선_조회_요청(신분당선), 정자역.getId());

		// Given 새로운 구간 등록 전 기존 노선의 구간 목록
		final List<SectionResponse> previousSectionResponse = 지하철_노선_구간_목록_조회(신분당선.getId())
			.jsonPath().getList(".", SectionResponse.class);
		// When 지하철 구간 사이에 구간 등록 요청
		ExtractableResponse<Response> stationCreatedResponse =
			지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
		// Then 지하철 구간 등록됨
		ExtractableResponse<Response> lineResponse = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
		지하철_노선에_지하철역_등록됨(lineResponse, 양재역.getId());
		// Then 기존 구간의 길이와 새로 생긴 구간으로 나누어진 구간들의 길이의 합이 같음
		지하철_노선의_구간_생성_확인(stationCreatedResponse, previousSectionResponse,
			lineResponse.as(LineResponse.class));

		// When 지하철 노선에 등록된 역 목록 조회 요청
		// Then 등록한 지하철 구간이 반영된 역 목록이 순서대로 정렬되어 조회됨
		지하철_노선에_지하철역_순서_정렬됨(LineAcceptanceTest.지하철_노선_조회_요청(신분당선),
			Arrays.asList(강남역, 양재역, 정자역, 광교역));

		// When 종점역이 포함되지 않은 지하철 구간 삭제 요청
		ExtractableResponse<Response> deleteStationResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);
		// Then 지하철 구간 삭제됨
		지하철_노선에_지하철역_제외됨(deleteStationResponse, 신분당선, 양재역);

		// When 종점역이 포함된 지하철 구간 삭제 요청
		deleteStationResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
		// Then 지하철 구간 삭제됨
		지하철_노선에_지하철역_제외됨(deleteStationResponse, 신분당선, 강남역);

		// When 지하철 노선에 등록된 역 목록 조회 요청
		// Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
		지하철_노선에_지하철역_순서_정렬됨(LineAcceptanceTest.지하철_노선_조회_요청(신분당선),
			Arrays.asList(정자역, 광교역));
	}

	@DisplayName("지하철 구간을 등록한다.")
	@Test
	void addLineSection() {
		// when
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

		// then
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
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
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
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
		지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, 양재역);
		ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
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

	public static void 지하철_노선에_지하철역_등록됨(final ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 지하철_노선에_지하철역_등록됨(final ExtractableResponse<Response> response, final Long stationId) {
		List<Long> stationIds = response.as(LineResponse.class).getStations().stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(stationIds).contains(stationId)
		);
	}

	public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
		List<StationResponse> expectedStations) {
		LineResponse line = response.as(LineResponse.class);
		List<Long> stationIds = line.getStations().stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		List<Long> expectedStationIds = expectedStations.stream()
			.map(StationResponse::getId)
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

	public static void 지하철_노선에_지하철역_제외됨(final ExtractableResponse<Response> response,
		final LineResponse lineResponse, final StationResponse stationResponse) {

		LineResponse line = LineAcceptanceTest.지하철_노선_조회_요청(lineResponse).as(LineResponse.class);

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(line.getStations().stream().map(StationResponse::getId)).isNotIn(stationResponse.getId());
	}

	public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static ExtractableResponse<Response> 지하철_노선_구간_목록_조회(final Long lineId) {
		return given().log().all()
			.accept(MediaType.ALL_VALUE)
			.when()
			.get(String.format("/lines/%d/sections", lineId))
			.then().log().all()
			.extract();
	}

	public static void 지하철_노선의_구간_생성_확인(final ExtractableResponse<Response> response,
		final List<SectionResponse> previousSectionResponse, final LineResponse lineResponse) {
		SectionResponse sectionResponse = response.as(SectionResponse.class);
		List<SectionResponse> sectionResponses = 지하철_노선_구간_목록_조회(lineResponse.getId())
			.jsonPath().getList(".", SectionResponse.class);

		SectionResponse previousSameSection = previousSectionResponse.stream()
			.filter(previousSection -> previousSection.getDownStationId().equals(sectionResponse.getDownStationId()))
			.findFirst()
			.orElse(previousSectionResponse.stream()
				.filter(previousSection -> previousSection.getUpStationId().equals(sectionResponse.getUpStationId()))
				.findFirst()
				.orElseGet(null));

		SectionResponse modifiedSection = sectionResponses.stream()
			.filter(section -> section.getDownStationId().equals(sectionResponse.getUpStationId()))
			.findFirst()
			.orElse(
				sectionResponses.stream()
				.filter(section -> section.getUpStationId().equals(sectionResponse.getDownStationId()))
				.findFirst()
				.orElseGet(null));

		assertAll(
			() -> assertThat(modifiedSection).isNotNull(),
			() -> assertThat(previousSameSection).isNotNull(),
			() -> assertThat(previousSameSection.getDistance()).isEqualTo(sectionResponse.getDistance() +
				modifiedSection.getDistance())
		);
	}

}
