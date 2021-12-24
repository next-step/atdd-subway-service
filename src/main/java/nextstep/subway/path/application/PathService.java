package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        Station sourceStation = stationRepository.findById(source)
                .orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다. Station ID : " + source));
        Station targetStation = stationRepository.findById(target)
                .orElseThrow(() -> new IllegalArgumentException("도착역이 존재하지 않습니다. Station ID : " + target));
        return PathResponse.of(pathFinder.findPath(sourceStation, targetStation));
    }
}