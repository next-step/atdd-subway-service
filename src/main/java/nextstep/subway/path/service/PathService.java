package nextstep.subway.path.service;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long departureStationId, Long arrivalStationId) {
        checkEqualsDepartureArrival(departureStationId, arrivalStationId);
        Lines lines = findAllLine();
        Station departureStation = lines.searchStationById(departureStationId);
        Station arrivalStation = lines.searchStationById(arrivalStationId);
        PathFinder pathFinder = new PathFinder(lines.allSection());
        return pathFinder.ofPathResponse(departureStation, arrivalStation);
    }

    @Transactional(readOnly = true)
    public Lines findAllLine() {
        return new Lines(lineRepository.findAll());
    }

    private void checkEqualsDepartureArrival(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }
}
