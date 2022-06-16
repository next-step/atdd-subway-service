package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.Objects;

public class PathRequest {
    private final Lines lines;
    private final Station source;
    private final Station target;

    public PathRequest(Lines lines, Station source, Station target) {
        check(source, target);
        this.lines = lines;
        this.source = source;
        this.target = target;
    }

    private void check(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException("출발역과 도착역은 달라야 합니다.");
        }
    }

    public Lines getLines() {
        return this.lines;
    }

    public Station getSource() {
        return this.source;
    }

    public Station getTarget() {
        return this.target;
    }
}
