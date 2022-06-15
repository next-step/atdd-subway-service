package nextstep.subway.path.util;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathNavigator {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath dijkstraShortestPath;

    private PathNavigator(final List<Line> lines) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (final Line line : lines) {
            setLineToGraph(line);
        }
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void setLineToGraph(final Line line) {
        line.getSections().forEach(section -> {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        });
    }

    public List<Station> getStationVertexList(final Station sourceStation, final Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int getDistance(final Station sourceStation, final Station targetStation) {
        return (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
    }

    public static PathNavigator of(final List<Line> lines) {
        return new PathNavigator(lines);
    }
}
