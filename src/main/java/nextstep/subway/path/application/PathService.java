package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.path.domain.PathAssembler;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.domain.fare.TotalFarePolicy;
import nextstep.subway.path.dto.PathDtos;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.repository.SectionRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {
    private static final FarePolicy FARE_POLICY = new TotalFarePolicy();
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public PathService(SectionRepository sectionRepository,
        StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        PathDtos pathDtos = PathDtos.from(sectionRepository.findAll());

        StationGraph graph = new StationGraph(pathDtos);
        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());

        PathFinder pathFinder = new PathFinder(graph, source, target);
        return PathAssembler.writeResponse(pathFinder, FARE_POLICY);
    }
}
