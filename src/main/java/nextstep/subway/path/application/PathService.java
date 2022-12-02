package nextstep.subway.path.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.repository.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPaths(Long sourceId, Long targetId) {
        checkSameStation(sourceId, targetId);

        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.findFastPaths(lines, sourceStation, targetStation);

        return PathResponse.of(path);
    }

    private void checkSameStation(Long sourceId, Long targetId) {
        if (sourceId == targetId) {
            throw new InvalidRequestException("출발역과 도착역이 동일합니다.");
        }
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Station", id));
    }
}
