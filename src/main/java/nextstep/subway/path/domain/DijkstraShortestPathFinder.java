package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements StationGraphStrategy {
    public Path findShortestPath(List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        setGraph(lines, graph);

        GraphPath graphPath = Optional.ofNullable(path.getPath(source, target))
            .orElseThrow(PathException::new);

        List<Station> stations = graphPath.getVertexList();
        int distance = (int)graphPath.getWeight();

        return Path.of(stations, distance);
    }

    private void setGraph(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
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
