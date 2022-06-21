package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;

    public PathResponse findShortestPath(List<Section> allSection, Station sourceStation, Station targetStation) {
        stationGraph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(allSection);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(stationGraph);

        GraphPath shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return new PathResponse(shortestPath.getVertexList(), (long) shortestPath.getWeight());
    }

    private void initGraph(List<Section> allSection) {
        List<Station> allStations = findAllStations(allSection);
        allStations.forEach(station -> stationGraph.addVertex(station));

        allSection.forEach(section -> stationGraph.setEdgeWeight
            (stationGraph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));

    }

    private List<Station> findAllStations(List<Section> allSection) {
        Set<Station> stations = new HashSet<>();
        allSection.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });
        return new ArrayList<>(stations);
    }
}
