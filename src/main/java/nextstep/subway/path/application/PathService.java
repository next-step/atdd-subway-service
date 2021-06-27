package nextstep.subway.path.application;

import nextstep.subway.exception.CustomException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.Paths;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.subway.exception.CustomExceptionMessage.SAME_SOURCE_AND_TARGET_STATION;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationService stationService;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse getPaths(final long sourceStationId, final long targetStationId) {
        return new PathResponse(getShortestPaths(sourceStationId, targetStationId));
    }

    private Paths getShortestPaths(final long sourceStationId, final long targetStationId) {
        checkSameSourceAndTarget(sourceStationId, targetStationId);
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        return createPathFinder().getShortestPaths(sourceStation, targetStation);
    }

    private void checkSameSourceAndTarget(final long sourceStationId, final long targetStationId) {
        if (sourceStationId == targetStationId) {
            throw new CustomException(SAME_SOURCE_AND_TARGET_STATION);
        }
    }

    private PathFinder createPathFinder() {
        List<Line> lines = lineRepository.findAll();
        return PathFinder.of(lines);
    }
}
