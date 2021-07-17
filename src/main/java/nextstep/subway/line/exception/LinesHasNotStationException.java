package nextstep.subway.line.exception;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import static java.lang.String.format;

public class LinesHasNotStationException extends IllegalArgumentException {
    public LinesHasNotStationException(Station source, Station target, Lines lines) {
        super(format("노선들은 %s와(과) %s을(를) 연결하고 있지 않습니다. / 노선 목록: %s", source.getName(), target.getName(), lines));
    }
}
