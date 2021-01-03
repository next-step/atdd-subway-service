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

    public PathResponse findPath(Long source, Long target) {
        Lines lines = new Lines(lineRepository.findAll());
        Station sourceStation = lines.searchStationById(source);
        Station targetStation = lines.searchStationById(target);
        PathFinder pathFinder = new PathFinder(lines.allSection());
        return pathFinder.ofPathResponse(sourceStation, targetStation);
    }
}
