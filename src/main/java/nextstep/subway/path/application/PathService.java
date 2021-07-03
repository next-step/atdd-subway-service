package nextstep.subway.path.application;

import nextstep.subway.component.PathFinder;
import nextstep.subway.component.domain.SubwayPath;
import nextstep.subway.exception.SubwayPatchException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PathService {

    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "%s 아이디를 가진 지하철역이 존재하지않습니다.";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        SubwayPath subwayPath = pathFinder.shortestPath(lines, sourceStation, targetStation);
        return PathResponse.of(subwayPath);
    }

    private Station findStationById(Long stationId) {
        return stationRepository
                .findById(stationId)
                .orElseThrow(() -> new SubwayPatchException(NOT_FOUND_STATION_ERROR_MESSAGE, stationId));
    }
}
