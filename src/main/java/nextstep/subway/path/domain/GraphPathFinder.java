package nextstep.subway.path.domain;

import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.path.exception.PathExceptionType;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class GraphPathFinder {

    private WeightedMultigraph<String, SectionWeightedEdge> graph;

    public StationPath getShortestPath(List<Line> lines, Station start, Station destination) {
        validation(start, destination);
        graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        addVertex(lines);
        addEdgeWeight(lines);

        return toStationPath(start, destination);
    }

    private StationPath toStationPath(final Station start, final Station destination) {
        GraphPath<String, SectionWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(start.getName(), destination.getName());
        if(Objects.isNull(path)){
            throw new PathException(PathExceptionType.NO_PATH);
        }
        Integer extraCharge = getExtraCharge(path.getEdgeList());
        return StationPath.of(path, extraCharge);
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

    private void addVertex(final List<Line> lines) {
        for (Line line : lines) {
            line.getSortedStations()
                    .forEach(it -> graph.addVertex(it.getName()));
        }
    }

    private void addEdgeWeight(final List<Line> lines) {
        for (Line line : lines) {
            line.getSections()
                    .forEach(section -> {
                        SectionWeightedEdge sectionWeightedEdge = new SectionWeightedEdge(section.getUpStation().getName(), section.getDownStation().getName(), section);
                        graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName(), sectionWeightedEdge);
                        graph.setEdgeWeight(sectionWeightedEdge, section.getDistance());
                    });
        }
    }
}
