package nextstep.subway.path.application;

import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findShortestPath(final PathRequest pathRequest) {
        PathFinder pathFinder = PathFinder.init(stationRepository.findAll(), sectionRepository.findAll());
        Path shortestPath = pathFinder.findShortestPath(pathRequest.getSource(), pathRequest.getTarget());
        return PathResponse.from(shortestPath);
    }
}
