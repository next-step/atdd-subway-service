package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final SectionService sectionService;
    private final StationService stationService;

    public PathService(final SectionService sectionService, final StationService stationService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(final Long upStationId, final Long downStationId) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> subwayGraph = getSubwayGraph();
        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayGraph);
        final GraphPath graphPath = dijkstraShortestPath.getPath(upStationId, downStationId);
        return getPathResponseByGraphPath(graphPath);
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> getSubwayGraph() {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setVertexes(graph);
        setEdges(graph);
        return graph;
    }

    private void setVertexes(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        final List<StationResponse> stations = stationService.findAllStations();
        stations.stream().forEach(stationResponse -> {
            graph.addVertex(stationResponse.getId());
        });
    }

    private void setEdges(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        final List<Section> sections = sectionService.findAllSections();
        sections.stream().forEach(section -> {
            graph.setEdgeWeight(
                    graph.addEdge(
                            section.getUpStation().getId(),
                            section.getDownStation().getId()),
                    section.getDistance());
        });
    }

    private PathResponse getPathResponseByGraphPath(final GraphPath graphPath) {
        return new PathResponse(getStationsByGraphPath(graphPath), getDistanceByGraphPath(graphPath));
    }

    private List<PathStationResponse> getStationsByGraphPath(final GraphPath graphPath) {
        final List<Long> stationIds = ((List<Long>) graphPath.getVertexList());
        return stationIds
                .stream()
                .map(stationService::findStationById)
                .map(PathStationResponse::of)
                .collect(Collectors.toList());
    }

    private int getDistanceByGraphPath(final GraphPath graphPath) {
        return (int) graphPath.getWeight();
    }
}
