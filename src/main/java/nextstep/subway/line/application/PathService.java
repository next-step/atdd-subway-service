package nextstep.subway.line.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.SectionEdge;
import nextstep.subway.utils.SubwayGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PathService {
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<StationResponse> getShortestPath(final String sourceStationId, final String targetStationid) {
        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();
        Station sourceStation = stationRepository.getById(Long.valueOf(sourceStationId));
        Station targetStation = stationRepository.getById(Long.valueOf(targetStationid));
        List<Station> shortestPath = getShortestPath(sections, stations)
                            .getPath(sourceStation, targetStation)
                            .getVertexList();
        return getShortestPathResponse(stations, shortestPath);
    }

    private DijkstraShortestPath<Station, SectionEdge> getShortestPath(List<Section> sections, List<Station> stations) {
        SubwayGraph<Station, SectionEdge> graph = new SubwayGraph<>(SectionEdge.class);

        graph.addGraphVertex(stations);
        graph.addEdgeWeight(sections);

        return new DijkstraShortestPath<>(graph);
    }

    private List<StationResponse> getShortestPathResponse(List<Station> stations, List<Station> shortestPath) {
        List<StationResponse> shortestStations = new ArrayList<>();

        for (Station innerStation : shortestPath) {
            Optional<Station> station = stations.stream()
                    .filter(it -> it.equals(innerStation))
                    .findFirst();

            station.ifPresent(it -> shortestStations.add(StationResponse.of(it)));
        }

        return shortestStations;
    }
}
