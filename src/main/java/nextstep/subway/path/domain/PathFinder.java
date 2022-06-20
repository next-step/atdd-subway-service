package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph;

    private PathFinder(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph) {
        this.pathGraph = pathGraph;
    }

    public static PathFinder init(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(pathGraph, stations);
        setEdgeWeight(pathGraph, sections);
        return new PathFinder(pathGraph);
    }

    private static void addVertex(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, List<Station> stations) {
        stations.forEach(it -> pathGraph.addVertex(it.getId()));
    }

    private static void setEdgeWeight(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, List<Section> sections) {
        sections.forEach(it -> {
            Station source = it.getUpStation();
            Station target = it.getDownStation();
            int distance = it.getDistance();

            pathGraph.setEdgeWeight(pathGraph.addEdge(source.getId(), target.getId()), distance);
        });
    }
}
