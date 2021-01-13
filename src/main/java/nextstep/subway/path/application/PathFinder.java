package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.NoLinkPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private WeightedMultigraph<Station, Section> graph;

    public PathFinder(List<Line> lines) {
        buildGraph(lines);
    }

    private void buildGraph(List<Line> lines) {
        graph = new WeightedMultigraph<>(Section.class);
        for (Line line : lines) {
            buildGraphVertex(line);
            buildGraphEdges(line);
        }
    }

    private void buildGraphVertex(Line line) {
        List<Station> stations = line.getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void buildGraphEdges(Line line) {
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), section);
        }
    }

    public GraphPath<Station, Section> getPath(Station sourceStation, Station targetStation) {
        verifySameSourceTargetStation(sourceStation, targetStation);

        GraphPath<Station, Section> shortestPath = getShortestPath(sourceStation, targetStation);
        verifyNoLinkPath(shortestPath);

        return shortestPath;
    }

    private void verifySameSourceTargetStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(
                    String.format("출발지와 목적지가 같습니다. (id: %s - %s)", sourceStation.getId(), sourceStation.getName())
            );
        }
    }

    private void verifyNoLinkPath(GraphPath<Station, Section> shortestPath) {
        if (shortestPath == null) {
            throw new NoLinkPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private GraphPath<Station, Section> getShortestPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, Section> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

}
