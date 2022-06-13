package nextstep.subway.path.application;

import nextstep.subway.error.ErrorCode;
import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.subway.error.ErrorCode.NO_EXISTS_STATION;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        List<Station> stations = stationRepository.findAll();
        Station source = stationRepository.findById(sourceId).orElseThrow(() -> new ErrorCodeException(NO_EXISTS_STATION));
        Station target = stationRepository.findById(targetId).orElseThrow(() -> new ErrorCodeException(NO_EXISTS_STATION));
        if (source == target) {
            throw new ErrorCodeException(ErrorCode.SOURCE_EQUALS_TARGET);
        }
        List<Line> lines = lineRepository.findAllWithSections();
        return pathFinder.findPath(stations, lines, source, target);
    }
}
