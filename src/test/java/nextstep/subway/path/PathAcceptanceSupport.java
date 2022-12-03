package nextstep.subway.path;

import nextstep.subway.line.acceptance.LineAcceptanceSupport;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

public class PathAcceptanceSupport {
    public static LineResponse 지하철_노선_등록되어_있음(
            String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return LineAcceptanceSupport.지하철_노선_등록되어_있음(
                new LineRequest(name, color, upStation.getId(), downStation.getId(), distance)).as(LineResponse.class);
    }
}
