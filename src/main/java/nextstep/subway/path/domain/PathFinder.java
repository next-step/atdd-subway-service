package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private StationGraphStrategy stationGraphStrategy;

    public PathFinder(StationGraphStrategy stationGraphStrategy) {
        this.stationGraphStrategy = stationGraphStrategy;
    }

    public Path findShortestPath(List<Line> lines, Station target, Station source) {
        return stationGraphStrategy.findShortestPath(lines, target, source);
    }

//    private void setGraph(List<Line> lines) {
//        lines.stream()
//            .map(Line::getSections)
//            .flatMap(Collection::stream)
//            .forEach(it -> {
//                Station upStation = it.getUpStation();
//                Station downStation = it.getDownStation();
//
//                graph.addVertex(upStation);
//                graph.addVertex(downStation);
//
//                graph.setEdgeWeight(graph.addEdge(upStation, downStation), it.getDistance());
//            });
//    }
}
