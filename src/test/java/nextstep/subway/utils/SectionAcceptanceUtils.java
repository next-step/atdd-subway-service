package nextstep.subway.utils;

import static nextstep.subway.utils.LineAcceptanceUtils.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;

public class SectionAcceptanceUtils {

	private static final String SECTION_URL = "/sections";

	private SectionAcceptanceUtils() {
		throw new AssertionError("Utility class cannot be instantiated");
	}

	public static void 지하철_노선에_구간이_추가되어_있음(LineResponse lineResponse, SectionRequest request) {
		지하철_구간_등록_요청(lineResponse.getId(), request);
	}

	public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long lineId, SectionRequest request) {
		return RestAssuredUtils.post(LINE_URL + "/" + lineId + SECTION_URL, request).extract();
	}

	public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
		return RestAssuredUtils.delete(LINE_URL + "/{id}" + SECTION_URL + "?stationId={stationId}", lineId, stationId)
			.extract();
	}
}
