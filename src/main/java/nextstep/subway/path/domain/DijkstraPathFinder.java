package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraPathFinder implements PathFinder{

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private DijkstraPathFinder(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static DijkstraPathFinder from(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createWeightedMultiGraphByLines(lines);
        return new DijkstraPathFinder(new DijkstraShortestPath<>(graph));
    }

    @Override
    public Path findShortPath(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> path = this.dijkstraShortestPath.getPath(sourceStation, targetStation);
        return Path.of(path.getVertexList(), (int) path.getWeight());
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createWeightedMultiGraphByLines(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(lines, graph);
        addWeight(lines, graph);
        return graph;
    }

    private static void addWeight(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Sections> sections = StreamUtils.mapToList(lines, Line::getSections);
        List<Section> allSections = sections.stream()
                .map(Sections::getSections)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        allSections.forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().getValue())
        );
    }

    private static void addVertexes(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        stations.forEach(graph::addVertex);
    }
}
