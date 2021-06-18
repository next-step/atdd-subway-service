package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.ui.NoStationInListException;
import nextstep.subway.path.ui.SameSourceTargetException;
import nextstep.subway.path.ui.SourceTargetNotConnectException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    List<Station> allStations;
    List<Line> lines;

    WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Station> allStations, List<Line> lines) {
        this.allStations = allStations;
        this.lines = lines;

        graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        addStationToVertex(allStations);
        addSectionToEdge(lines);
    }

    private void addSectionToEdge(List<Line> lines) {
        for (Line line : lines) {
            for (Section section : line.getSections()) {
                graph.setEdgeWeight(graph.addEdge(section.upStation(), section.downStation()), section.getDistance());
            }
        }
    }

    private void addStationToVertex(List<Station> allStations) {
        for (Station station : allStations) {
            graph.addVertex(station);
        }
    }


    public List<Station> shortestPath(Station source, Station target) {
        try {
            return graphPath(source, target).getVertexList();
        } catch (NullPointerException e) {
            throw new SourceTargetNotConnectException();
        }
    }

    public Integer shortestWeight(Station source, Station target) {
        Double distance;

        try {
            distance = graphPath(source, target).getWeight();
        } catch (NullPointerException e) {
            throw new SourceTargetNotConnectException();
        }

        return distance.intValue();
    }

    private GraphPath graphPath(Station source, Station target) {
        validateSourceTarget(source, target);
        validateValidStation(source, target);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        return dijkstraShortestPath.getPath(source, target);
    }

    private void validateSourceTarget(Station source, Station target) {
        if (source.isSameStation(target)) {
            throw new SameSourceTargetException();
        }
    }

    private void validateValidStation(Station source, Station target) {
        if (!allStations.contains(source) || !allStations.contains(target)) {
            throw new NoStationInListException();
        }
    }
}
