package nextstep.subway.path.infrastructure;

import java.util.List;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class WeightedMultiStationGraph implements StationGraph {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public WeightedMultiStationGraph() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    @Override
    public StationGraph createGraph(List<Line> lines) {
        for (Line line : lines) {
            addVertex(line.getStations());
            addEdgeWeight(line.getSections());
        }
        return this;
    }

    @Override
    public boolean containsVertex(Station station) {
        return graph.containsVertex(station);
    }

    @Override
    public Path getShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new NotFoundException("접점이 없습니다.");
        }
        return Path.of(path.getVertexList(), Double.valueOf(path.getWeight()).intValue());
    }

    private void addVertex(List<Station> stations) {
        for (Station s : stations) {
            graph.addVertex(s);
        }
    }

    private void addEdgeWeight(List<Section> sections) {
        for (Section s : sections) {
            graph.setEdgeWeight(graph.addEdge(s.getUpStation(), s.getDownStation()),
                s.getDistance().get());
        }
    }
}
