package nextstep.subway.path.domain;

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

        return createStationPath(start, destination);
    }

    private StationPath createStationPath(final Station start, final Station destination) {
        final GraphPath<Station, SectionWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(start, destination);
        final Integer charge = getCharge(path.getEdgeList());
        return StationPath.of(path.getVertexList(), path.getWeight(), charge);
    }

    private Integer getCharge(final List<SectionWeightedEdge> sectionEdge) {
        return sectionEdge.stream()
                .map(SectionWeightedEdge::getSection)
                .map(Section::getLine)
                .map(Line::getExtraCharge)
                .max(Integer::compareTo)
                .get();
    }


    private void validation(final Station start, final Station destination) {
        if (start.equals(destination)) {
            throw new PathException(PathExceptionType.EQUALS_STATION);
        }
    }

    private void addStation(final List<Line> lines) {
        for (Line line : lines) {
            line.getSortedStations()
                    .forEach(it -> graph.addVertex(it));
        }
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
