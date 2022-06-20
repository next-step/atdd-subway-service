package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public PathFinder(final List<Line> lines) {
        for (Line line : lines) {
            addStationToGraphVertex(line.getStations());
            addSectionToEdgeAndSetWeight(line.getSections());
        }

        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public ShortestPath getShortestPath(final Station sourceStation, final Station targetStation) {
        validInputCheck(sourceStation, targetStation);
        return new ShortestPath(dijkstraShortestPath.getPath(sourceStation, targetStation));
    }

    private void addStationToGraphVertex(final List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addSectionToEdgeAndSetWeight(final List<Section> sections) {
        for (Section section : sections) {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
    }

    private void validInputCheck(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 다른 역으로 지정되어야 합니다.");
        }
    }
}
