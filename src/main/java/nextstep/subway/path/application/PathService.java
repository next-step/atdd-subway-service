package nextstep.subway.path.application;

import nextstep.subway.fare.application.FareService;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final FareService fareService;

    public PathService(StationRepository stationRepository,
                       LineRepository lineRepository, FareService fareService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.fareService = fareService;
    }

    public PathResponse getShortestDistance(long sourceStationId, long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(EntityNotFoundException::new);
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(EntityNotFoundException::new);

        List<Line> lines = lineRepository.findAllWithSections();
        PathFinder shortestPathFinder = new ShortestPathFinder(lines);
        Path path = shortestPathFinder.findPath(sourceStation, targetStation);
        Fare fare = fareService.calculateFare(path);
        return PathResponse.of(path, fare);
    }
}
