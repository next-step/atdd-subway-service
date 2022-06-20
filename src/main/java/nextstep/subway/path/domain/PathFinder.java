package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.SameSourceAndTargetException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    public Path getShortestDistance(Lines lines, Station source, Station target) {
        validateSameSourceAndTarget(source, target);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        DijkstraShortestPath path = new DijkstraShortestPath(graph);

        List<Section> sections = lines.getAllSections();
        generateGraph(sections, graph);

        GraphPath graphPath = path.getPath(source, target);

        List<Station> stations = graphPath.getVertexList();
        int distance = (int) graphPath.getWeight();

        return new Path(stations, distance);
    }

    private void generateGraph(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetException();
        }
    }
}
