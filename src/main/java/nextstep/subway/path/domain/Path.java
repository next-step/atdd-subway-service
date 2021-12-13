package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final PathFinder pathFinder;

    public Path(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    private void validateCorrectSourceAndTarget(Station source, Station target) {
        if(source == target) {
            throw new IllegalStateException("출발역과 도착역이 같습니다.");
        }
    }

    public List<Station> findPaths(List<Line> lines, Station source, Station target) {
        validateCorrectSourceAndTarget(source, target);
        return this.pathFinder.findStations(lines, source, target);
    }

    public Distance findPathWeight(List<Line> lines, Station source, Station target) {
        validateCorrectSourceAndTarget(source, target);
        return this.pathFinder.findDistance(lines, source, target);
    }
}
