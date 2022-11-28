package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private final StationGraph stationGraph;

    public PathFinder(List<Section> sections) {
        stationGraph = new StationGraph(sections);
    }

    public Path getShortestPath(Station source, Station target) {
        validate(source, target);
        return stationGraph.findShortestPath(source, target);
    }

    private void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new RuntimeException("경로 조회가 불가능합니다.");
        }
        if (!stationGraph.containsStation(source) || !stationGraph.containsStation(target)) {
            throw new RuntimeException("역이 그래프에 포함되지 않았습니다.");
        }
    }
}
