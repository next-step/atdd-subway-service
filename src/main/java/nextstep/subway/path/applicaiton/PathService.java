package nextstep.subway.path.applicaiton;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findPaths(Long sourceId, Long targetId) {
        validFindPaths(sourceId, targetId);
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);

        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = makeGraph(stations, sections);
        GraphPath path = findShortestPath(source, target, graph);

        return PathResponse.of(path.getVertexList(), path.getWeight());
    }

    private static void validFindPaths(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalArgumentException("동일한 역으로 경로조회할 수 없습니다.");
        }
    }

    private GraphPath findShortestPath(Station source, Station target,
                                          WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        return path;
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance()));
        return graph;
    }

    private Station findStationById(Long sourceId) {
        return stationRepository.findById(sourceId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 역입니다."));
    }

}
