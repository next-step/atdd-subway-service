package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;

import static nextstep.subway.AcceptanceTest.*;

public class LineSectionSteps {

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        return post("/lines/{lineId}/sections", sectionRequest, line.getId());
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId());
    }
}
