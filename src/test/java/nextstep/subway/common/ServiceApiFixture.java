package nextstep.subway.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.utils.RestApiFixture;

public class ServiceApiFixture {

	public static SectionRequest sectionRequest(Long upStationId, Long downStationId, int distance) {
		return new SectionRequest(upStationId, downStationId, distance);
	}

	public static ExtractableResponse<Response> postSections(Long lineId, SectionRequest sectionRequest) {
		return RestApiFixture.post(sectionRequest, "/lines/{id}/sections", lineId);
	}

	public static ExtractableResponse<Response> deleteSections(Long lineId, Long stationId) {
		return RestApiFixture.delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId);
	}

	public static LineRequest lineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
		return new LineRequest(name, color, upStationId, downStationId, distance);
	}

	public static ExtractableResponse<Response> postLines(LineRequest lineRequest) {
		return RestApiFixture.post(lineRequest, "/lines");
	}

	public static ExtractableResponse<Response> postStations(String name) {
		return RestApiFixture.post(new StationRequest(name), "/stations");
	}
}
