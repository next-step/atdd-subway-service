package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;
    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm;

    public PathFinder(List<Line> lines) {
        stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setStationGraph(lines);

        shortestPathAlgorithm = new DijkstraShortestPath<>(stationGraph);
    }

    private void setStationGraph(List<Line> lines) {
        loadStations(lines);
        loadSections(lines);
    }

    private void loadStations(List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(List::stream)
                .distinct()
                .forEach(stationGraph::addVertex);
    }

    private void loadSections(List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .flatMap(List::stream)
                .distinct()
                .forEach(this::setSection);
    }

    private void setSection(Section section) {
        stationGraph.setEdgeWeight(stationGraph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
    }

    public Path findShortestPath(Station start, Station end) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = shortestPathAlgorithm.getPath(start, end);

        return new Path(shortestPath.getVertexList(),
                Distance.from(Double.valueOf(shortestPath.getWeight()).intValue()));
    }
}
