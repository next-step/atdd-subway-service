package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(
        DefaultWeightedEdge.class);
    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        for (Line line : lines) {
            setVertexPerLine(line);
            setWeightPerLine(line);
        }

        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void setVertexPerLine(Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private void setWeightPerLine(Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getDownStation(), section.getUpStation()),
                section.getDistance().getDistance());
        }
    }

    public GraphPath findPath(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }
}
