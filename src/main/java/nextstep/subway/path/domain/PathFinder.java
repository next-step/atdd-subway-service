package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionEdge> graph;
    private final DijkstraShortestPath dijkstraShortestPath;

    private PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(SectionEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines).createGraph(lines);
    }

    public Path findShortestPath(Station fromStation, Station toStation) {
        GraphPath path = dijkstraShortestPath.getPath(fromStation, toStation);
        return Path.from(path);
    }


    private PathFinder createGraph(List<Line> lines) {
        for (Line line: lines) {
            addVertex(line.getStations());
            addEdgeWeight(line.getSectionEdges());
        }
        return this;
    }

    private  void addVertex(List<Station> stations) {
        for (Station s: stations) {
            graph.addVertex(s);
        }
    }

    private void addEdgeWeight(List<SectionEdge> sectionEdges) {
        for (SectionEdge se: sectionEdges) {
            graph.addEdge(se.getSource(), se.getTarget(), se);
            graph.setEdgeWeight(se, se.getDistance());
        }
    }

}
