package nextstep.subway.path.domain;

import nextstep.subway.error.ErrorCode;
import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class PathFinder {

    public Path findPath(List<Station> stations, List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertex(graph, stations);
        setEdgeWeight(graph, lines);
        GraphPath<Station, SectionEdge> graphPath = find(graph, source, target);
        List<Section> sections = graphPath.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(toList());
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), sections);
    }

    private void addVertex(WeightedMultigraph<Station, SectionEdge> graph, List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, List<Line> lines) {
        lines.forEach(line -> setEdgeWeightSections(graph, line.getSections().getSections()));
    }

    private void setEdgeWeightSections(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections) {
        sections.forEach(section -> {
            SectionEdge edge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
            graph.setEdgeWeight(edge, section.getDistance());
        });
    }

    private GraphPath<Station, SectionEdge> find(WeightedMultigraph<Station, SectionEdge> graph, Station source, Station target) {
        GraphPath<Station, SectionEdge> graphPath = new DijkstraShortestPath<>(graph).getPath(source, target);
        if (graphPath == null) {
            throw new ErrorCodeException(ErrorCode.SOURCE_NOT_CONNECT_TARGET);
        }
        return graphPath;
    }
}
