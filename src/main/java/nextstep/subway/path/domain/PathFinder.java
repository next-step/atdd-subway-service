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

    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Station> allStations, List<Line> lines) {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        addStationToVertex(allStations, graph);
        addSectionToEdge(lines, graph);

        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void addStationToVertex(List<Station> allStations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : allStations) {
            graph.addVertex(station);
        }
    }

    private void addSectionToEdge(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Line line : lines) {
            line.getSections().forEach(
                    section -> graph.setEdgeWeight(graph.addEdge(section.upStation(), section.downStation()),
                            section.getDistance()
                    )
            );
        }
    }

    public List<Station> shortestPath(Station source, Station target) {
        try {
            return graphPath(source, target).getVertexList();
        } catch (NullPointerException e) {
            throw new SourceTargetNotConnectException();
        }
    }

    public int shortestWeight(Station source, Station target) {
        double distance;

        try {
            distance = graphPath(source, target).getWeight();
        } catch (NullPointerException e) {
            throw new SourceTargetNotConnectException();
        }

        return (int) distance;
    }

    private GraphPath graphPath(Station source, Station target) {
        validateSourceTarget(source, target);

        return dijkstraShortestPath.getPath(source, target);
    }

    private void validateSourceTarget(Station source, Station target) {
        if (source.isSameStation(target)) {
            throw new SameSourceTargetException();
        }
    }
}
