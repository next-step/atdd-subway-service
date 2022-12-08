package nextstep.subway.path.domain;

import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {

    private Station sourceStation;
    private Station targetStation;
    private GraphPath sourceTargetGraphPath;

    public PathFinder(Station sourceStation, Station targetStation, List<Line> lines) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.SOURCE_TARGET_EQUAL.getMessage());
        }
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.sourceTargetGraphPath = getSourceTargetGraphPath(lines);
    }

    private GraphPath getSourceTargetGraphPath(List<Line> lines) {
        return Optional.ofNullable(
                        new DijkstraShortestPath(getSectionDistanceGraph(lines)).getPath(sourceStation, targetStation))
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.DISCONNECT_SOURCE_TARGET.getMessage()));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> getSectionDistanceGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.stream().forEach(line -> makeGraph(line, graph));
        return graph;
    }

    private void makeGraph(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        Station nextStation = line.findUpStation();
        boolean isLastSection = false;
        while (!isLastSection) {
            Station beforeStation = nextStation.copyOf();
            Optional<Section> nextSection = line.findNextLowerSection(beforeStation);
            isLastSection = !nextSection.isPresent();
            drawGraph(graph, nextSection);
            nextStation = nextSection.map(Section::getDownStation).orElse(null);
        }
    }

    private void drawGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                           Optional<Section> nextSection) {
        if (!nextSection.isPresent()) {
            return;
        }
        Section section = nextSection.get();
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().getDistance());
    }

    public List<Station> getShortestPathStationList() {
        return sourceTargetGraphPath.getVertexList();
    }

    public int getShortestPathDistance() {
        return (int) sourceTargetGraphPath.getWeight();
    }

    public int getFee() {
        return 1250;
    }
}
