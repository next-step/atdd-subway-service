package nextstep.subway.line.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

	/**
	 * Feature: 지하철 구간 관련 기능
	 *
	 *   Background
	 *     Given 지하철역 등록되어 있음 (ex. [강남], [양재], [정자], [광교])
	 *     And 지하철 노선 등록되어 있음 (ex. [강남]----------20----------[광교], [양재], [정자])
	 *
	 *   Scenario: 지하철 구간을 관리
	 *     When 등록된 지하철역이 2개일 때 지하철 구간 삭제 요청
	 *     Then 지하철 구간 삭제 실패됨
	 *     When 동일한 지하철 구간 등록 요청
	 *     Then 지하철 구간 등록 실패됨
	 *     When 등록되지 않은 역들로 지하철 구간 등록 요청
	 *     Then 지하철 구간 등록 실패됨
	 *     When 지하철 구간 등록 요청
	 *     Then 지하철 구간 등록됨
	 *     When 지하철 노선에 등록된 역 목록 조회 요청
	 *     Then 등록한 지하철 구간이 반영된 역 목록이 조회됨 (ex. [강남]-2-[양재]---------18---------[광교])
	 *     When 지하철 구간 등록 요청
	 *     Then 지하철 구간 등록됨
	 *     When 지하철 노선에 등록된 역 목록 조회 요청
	 *     Then 등록한 지하철 구간이 반영된 역 목록이 조회됨 (ex. [강남]-2-[양재]----8----[정자]-----10-----[광교])
	 *     When 지하철 구간 삭제 요청
	 *     Then 지하철 구간 삭제됨
	 *     When 지하철 노선에 등록된 역 목록 조회 요청
	 *     Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨 (ex. [강남]-----10-----[정자]-----10-----[광교])
	 */
	@DisplayName("인수 조건")
	@Test
	void acceptanceCriteria() {
		// Background
		StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		StationResponse 정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
		StationResponse 광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
		LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 20, 900);

		// Scenario
		지하철_노선에_지하철역_제외_실패됨(지하철_노선에_지하철역_제외_요청(신분당선, 강남역));
		지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(강남역, 광교역));
		지하철_노선에_지하철역_등록_실패됨(지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 20));
		지하철_노선에_지하철역_등록_실패됨(지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 8));
		지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2));
		지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(강남역, 양재역, 광교역));
		지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 8));
		지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(강남역, 양재역, 정자역, 광교역));
		지하철_노선에_지하철역_제외됨(지하철_노선에_지하철역_제외_요청(신분당선, 양재역));
		지하철_노선에_지하철역_순서_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(강남역, 정자역, 광교역));
	}

	public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(
		LineResponse line,
		StationResponse upStation,
		StationResponse downStation,
		int distance
	) {
		SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

		Map<String, Object> pathParams = new HashMap<>();
		pathParams.put("lineId", line.getId());

		return post("/lines/{lineId}/sections", pathParams, sectionRequest);
	}

	public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(
		LineResponse line,
		StationResponse upStation,
		StationResponse downStation,
		int distance
	) {
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
		Map<String, Object> pathParams = new HashMap<>();
		pathParams.put("lineId", line.getId());

		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("stationId", station.getId());

		return delete("/lines/{lineId}/sections", pathParams, queryParams);
	}

	public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
