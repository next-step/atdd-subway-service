package nextstep.subway.path.domain;

import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.path.exception.PathExceptionType;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class JgraphPathFinder implements PathFinder {
    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;

    public JgraphPathFinder() {
        this.graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
    }

    @Override
    public StationPath getShortestPath(final List<Line> lines, final Station start, final Station destination) {
        validation(start, destination);
        addStation(lines);
        addEdgeWeight(lines);

        return toStationPath(start, destination);
    }

    private StationPath toStationPath(final Station start, final Station destination) {
        final GraphPath<Station, SectionWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(start, destination);
        final Integer extraCharge = getExtraCharge(path.getEdgeList());
        return StationPath.of(path.getVertexList(), path.getWeight(), extraCharge);
    }

    private Integer getExtraCharge(final List<SectionWeightedEdge> sectionEdge) {
        return sectionEdge.stream()
                .map(SectionWeightedEdge::getExtraCharge)
                .max(Integer::compareTo)
                .orElse(ExtraCharge.DEFAULT_EXTRA_CHARGE);
    }


    private void validation(final Station start, final Station destination) {
        if (start.equals(destination)) {
            throw new PathException(PathExceptionType.EQUALS_STATION);
        }
    }

    private void addStation(final List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getSortedStations().stream())
                .forEach(graph::addVertex);
    }

    private void addEdgeWeight(final List<Line> lines) {
        for (Line line : lines) {
            line.getSections()
                    .forEach(this::addSection);
        }
    }

    private void addSection(final Section section) {
        SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionWeightedEdge);
        graph.setEdgeWeight(sectionWeightedEdge, section.getDistance());
    }
}
