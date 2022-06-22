package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class JgraphShortestPathFinder implements ShortestPathFinder {
    WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    JgraphShortestPathFinder() {
    }

    private void setGraph(List<Section> sections) {
        sections.forEach((section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            Distance distance = section.getDistance();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance.value());
        }));
    }

    @Override
    public Path findShortestPath(List<Section> sections, Station startStation, Station targetStation) {
        findShortestPathValid(startStation, targetStation);
        setGraph(sections);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(startStation, targetStation);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void findShortestPathValid(Station startStation, Station targetStation) {
        if (startStation.equals(targetStation)) {
            throw new IllegalArgumentException("시작역과 찾을역이 같으면 안 됩니다.");
        }
    }
}
