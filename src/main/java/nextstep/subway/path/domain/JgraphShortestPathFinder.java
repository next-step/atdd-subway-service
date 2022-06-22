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
    WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    private List<Section> sections;

    JgraphShortestPathFinder(List<Section> sections) {
        this.sections = sections;
        setGraph(sections);

    }

    private void setGraph(List<Section> sections) {
        sections.forEach((section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            Distance distance = section.getDistance();

            graph.addVertex(upStation.getId());
            graph.addVertex(downStation.getId());
            graph.setEdgeWeight(graph.addEdge(upStation.getId(), downStation.getId()), distance.value());
        }));
    }



    @Override
    public List<Station> getShortestStations(Long startStationId, Long endStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStationId, endStationId);
        //List<Long> vertexList = path.getEdgeList().get(0);
        return null;
    }
}
