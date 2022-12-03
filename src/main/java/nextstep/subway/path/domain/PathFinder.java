package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {

    public static final String SOURCE_TARGET_NOT_SAME_EXCEPTION_MESSAGE = "출발역과 도착역이 같을 수 없습니다.";

    private List<Line> lines = new ArrayList<>();

    public PathFinder(List<Line> lines) {
        this.lines = lines;
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines);
    }

    public void findPath(Station source, Station target) {
        validSearchPath(source, target);
    }

    private void validSearchPath(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(SOURCE_TARGET_NOT_SAME_EXCEPTION_MESSAGE);
        }
    }
}
