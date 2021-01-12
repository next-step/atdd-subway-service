package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PathFindService {
    private final LineService lineService;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFindService(LineService lineService) {
        this.lineService = lineService;
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public PathResponse findShortestPath(Station start, Station end) {
        validate(start, end);

        Set<Station> stations = lineService.getAllStations();
        stations.forEach(graph::addVertex);

        List<Section> sections = lineService.getAllSections();
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getDownStation(), section.getUpStation()), section.getDistance());
        }
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(start, end);

        if(path == null) throw new NotConnectedExeption(start, end);

        return new PathResponse(path.getVertexList(), (int) path.getWeight());
    }

    private void validate(Station start, Station end) {
        if(start == null || end == null) {
            throw new NoSuchStationException("존재하지 않는 역입니다.");
        }

        if(start == end) {
            throw new SameStartAndEndException(start);
        }

    }
}
