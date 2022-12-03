package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = PathFinder.from(lines);
        Path path = pathFinder.findShortestPath(sourceStation, targetStation);

        return PathResponse.from(path);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException(ErrorEnum.STATION_NOT_EXISTS.message()));
    }
}
