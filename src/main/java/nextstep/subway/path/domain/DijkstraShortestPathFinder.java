package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);

    private DijkstraShortestPathFinder(List<Line> lines) {
        initGraph(lines);
    }

    public static DijkstraShortestPathFinder from(List<Line> lines) {
        return new DijkstraShortestPathFinder(lines);
    }

    private void initGraph(List<Line> lines) {
        lines.stream().forEach(line -> {
            addVertex(line);
            setEdgeWeight(line.getSections());
        });
    }

    private void addVertex(Line line) {
        line.getStations()
                .forEach(graph::addVertex);
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.stream()
                .forEach(section -> {
                    SectionEdge sectionEdge = new SectionEdge(section);
                    addEdge(section, sectionEdge);
                    graph.setEdgeWeight(sectionEdge, section.getDistance());
                });
    }

    private boolean addEdge(Section section, SectionEdge sectionEdge) {
        return graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
    }

    @Override
    public Path findPath(Station source, Station target) {
        validateSameStations(source, target);

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        validateNotConnectStations(graphPath);

        List<Station> stations = graphPath.getVertexList();
        double weight = graphPath.getWeight();
        return Path.of(stations, (int) weight, getMaxExtraFare(graphPath));
    }

    private void validateSameStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같습니다.");
        }
    }

    private void validateNotConnectStations(GraphPath<Station, SectionEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new SubwayException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private Lines getLines(GraphPath<Station, SectionEdge> graphPath) {
        return Lines.from(graphPath.getEdgeList().stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toList()));
    }

    private int getMaxExtraFare(GraphPath<Station, SectionEdge> graphPath){
        return getLines(graphPath).maxExtraFare();
    }
}

