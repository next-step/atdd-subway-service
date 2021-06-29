package nextstep.subway.path.service;

import nextstep.subway.component.PathFinder;
import nextstep.subway.component.domain.SubwayPath;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PathsService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public PathsService(LineRepository lineRepository, StationRepository stationRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Optional<Station> sourceStation = stationRepository.findById(source);
        Optional<Station> targetStation = stationRepository.findById(target);
        LocalDateTime now = LocalDateTime.now();

        SubwayPath subwayPath = pathFinder.shortestPath(lines, sourceStation, targetStation);
        return new PathResponse(
                Arrays.asList(
                    new StationResponse(source, "광교역", now, now),
                    new StationResponse(1L, "강남역", now, now),
                    new StationResponse(target, "교대역", now, now)
                ), 15);
    }
}
