package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    public static List<Station> findShortest(Station source, Station target, List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        addAllLineSectionsToGraph(lines, graph);

        return createShortestPaths(source, target, graph);
    }

    private static void addAllLineSectionsToGraph(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(Line::getSections)
                .forEach(sections -> addSectionsToGraph(graph, sections));
    }

    private static List<Station> createShortestPaths(Station source, Station target, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        return new DijkstraShortestPath<>(graph).getPath(source, target).getVertexList();
    }

    private static void addSectionsToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            addSectionToGraph(graph, section);
        }
    }

    private static void addSectionToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
    }
}
