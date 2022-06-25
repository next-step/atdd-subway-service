package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFare;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathMap;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PathService {
    private final PathFinder pathFinder;
    private final PathMap pathMap;
    private final PathFare pathFare;
    private final StationService stationService;
    private final LineService lineService;
    private final SectionRepository sectionRepository;

    @Autowired
    public PathService(PathFinder pathFinder, PathMap pathMap, PathFare pathFare, StationService stationService, LineService lineService, SectionRepository sectionRepository) {
        this.pathFinder = pathFinder;
        this.pathMap = pathMap;
        this.pathFare = pathFare;
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        WeightedMultigraph<Station, DefaultWeightedEdge> map = pathMap.createMap(lineService.findAll());

        return pathFinder.findShortestPath(map, source, target);
    }

    public PathResponse findShortestPathNew(Long sourceId, Long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        WeightedMultigraph<Station, DefaultWeightedEdge> map = pathMap.createMap(lineService.findAll());
        Map<String, Object> data = pathFinder.findShortestPathNew(map, source, target);
        int fare = pathFare.calculateFare((List<DefaultWeightedEdge>) data.get("edge"), sectionRepository.findAll());

        return new PathResponse((List<Station>) data.get("vertex"), (double) data.get("weight"), fare);
    }
}
