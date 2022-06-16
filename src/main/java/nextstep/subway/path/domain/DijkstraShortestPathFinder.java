package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements StationGraphStrategy {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public Path findShortestPath(List<Line> lines, Station source, Station target) {
        setGraph(lines);

        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        List<Station> stations = path.getPath(source, target).getVertexList();
        int distance = (int)path.getPathWeight(source, target);

        return Path.of(stations, distance);
    }

    private void setGraph(List<Line> lines) {
        lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .forEach(it -> {
                Station upStation = it.getUpStation();
                Station downStation = it.getDownStation();

                graph.addVertex(upStation);
                graph.addVertex(downStation);

                graph.setEdgeWeight(graph.addEdge(upStation, downStation), it.getDistance());
            });
    }
}
