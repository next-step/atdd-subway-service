package nextstep.subway.path.domain;

import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private WeightedMultigraph<Station, Section> getSectionDistanceGraph(List<Line> lines) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph(Section.class);
        lines.stream().forEach(line -> makeGraph(line, graph));
        return graph;
    }

    private void makeGraph(Line line, WeightedMultigraph<Station, Section> graph) {
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

    private void drawGraph(WeightedMultigraph<Station, Section> graph, Optional<Section> nextSection) {
        if (!nextSection.isPresent()) {
            return;
        }

        Section section = nextSection.get();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.addEdge(upStation, downStation, section);
        graph.setEdgeWeight(graph.getEdge(upStation, downStation), section.getDistance().getValue());
    }

    public List<Station> getShortestPathStationList() {
        return sourceTargetGraphPath.getVertexList();
    }

    public int getShortestPathDistance() {
        return (int) sourceTargetGraphPath.getWeight();
    }

    public ExtraFare getExtraFare() {
        ExtraFare maxExtraFare = new ExtraFare(0);
        for(Section section : objectEdgeToSectionEdge()) {
            maxExtraFare = maxExtraFare.max(section.getLine().getExtraFare());
        }
        return maxExtraFare;
    }

    private List<Section> objectEdgeToSectionEdge() {
        return (List<Section>) sourceTargetGraphPath.getEdgeList()
                .stream()
                .map(obj -> ((Section) obj))
                .collect(Collectors.toList());
    }
}
