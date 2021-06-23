package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.CannotFoundStationException;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target) {
        Station sourceStation = stationRepository.findById(source).orElseThrow(CannotFoundStationException::new);
        Station targetStation = stationRepository.findById(target).orElseThrow(CannotFoundStationException::new);
        PathFinder pathFinder = new PathFinder(new Lines(lineRepository.findAll()));
        return pathFinder.findPath(sourceStation, targetStation);
    }

}
