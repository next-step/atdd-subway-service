package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.exception.IllegalFindingPathException;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    public Path findShortestPath(Sections sections, Station sourceStation, Station targetStation) {
        validateFindingShortestPath(sourceStation, targetStation);
        PathGraph pathGraph = createPathGraph(sections);

        return pathGraph.findShortestPath(sourceStation, targetStation);
    }

    private PathGraph createPathGraph(Sections sections) {
        return new PathGraph(sections);
    }

    private void validateFindingShortestPath(Station source, Station target) {
        if(source.equals(target)) {
            throw new IllegalFindingPathException("출발지와 도착지가 같을 수 없습니다.");
        }
    }
}
