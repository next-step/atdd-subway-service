package nextstep.subway.utils;

import static nextstep.subway.utils.ResponseExtractUtils.*;
import static nextstep.subway.utils.StationAcceptanceUtils.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;

public class LineAcceptanceUtils {

	static final String LINE_URL = "/lines";

	private LineAcceptanceUtils() {
		throw new AssertionError("Utility class cannot be instantiated");
	}

	public static LineResponse 지하철_노선_등록되어_있음(final LineCreateRequest request) {
		return 지하철_노선_생성(request).as(LineResponse.class);
	}

	public static ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color,
		final String upStationName, final String downStationName) {
		return 지하철_노선_생성(name, color, upStationName, downStationName, 10);
	}

	public static ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color,
		final String upStationName, final String downStationName, final int distance) {
		return 지하철_노선_생성(name, color, upStationName, downStationName, distance);
	}

	private static ExtractableResponse<Response> 지하철_노선_생성(String name, String color,
		String upStationName, String downStationName, int distance) {
		Long upStationId = id(지하철역_생성_요청(upStationName));
		Long downStationId = id(지하철역_생성_요청(downStationName));

		LineCreateRequest lineCreateRequest = new LineCreateRequest(name, color, upStationId, downStationId, distance);
		return RestAssuredUtils.post(LINE_URL, lineCreateRequest).extract();
	}

	private static ExtractableResponse<Response> 지하철_노선_생성(LineCreateRequest request) {
		return RestAssuredUtils.post(LINE_URL, request).extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
		return RestAssuredUtils.get(LINE_URL).extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
		return RestAssuredUtils.get(LINE_URL + "/" + id).extract();

	}

	public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, LineUpdateRequest updateRequest) {
		return RestAssuredUtils.put(LINE_URL + "/" + id, updateRequest).extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
		return RestAssuredUtils.delete(LINE_URL + "/" + id).extract();
	}
}
