package nextstep.subway.path.application;

import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public PathService(StationService stationService, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(PathRequest pathRequest) {
        Station source = stationService.findStationById(pathRequest.getSource());
        Station target = stationService.findStationById(pathRequest.getTarget());

        return PathResponse.of(new StationGraph(sectionRepository.findAll()).findShortestPath(source, target));
    }
}
