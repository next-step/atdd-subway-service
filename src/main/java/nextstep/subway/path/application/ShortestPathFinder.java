package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortestPathFinder {
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private static final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private static final Map<DefaultWeightedEdge, Section> sectionByEdge;

    static {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        sectionByEdge = new HashMap<>();
    }

    public static Path findShortestPath(Sections sections, Station sourceStation, Station targetStation) {
        Stations stations = sections.getStations();

        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
            sectionByEdge.put(edge, section);
        });

        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        checkPathIsNull(shortestPath);

        int maxExtraCharge = getMaxExtraCharge(shortestPath.getEdgeList());

        return new Path(shortestPath.getVertexList(), new Distance((int) shortestPath.getWeight()), maxExtraCharge);
    }

    private static void checkPathIsNull(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (shortestPath == null) {
            throw new PathFindException("source station is not connected to target station");
        }
    }

    private static int getMaxExtraCharge(List<DefaultWeightedEdge> edgeList) {
        return edgeList
                .stream()
                .map(sectionByEdge::get)
                .max(Comparator.comparingInt(Section::getExtraCharge))
                .map(Section::getExtraCharge)
                .orElse(0);
    }
}
