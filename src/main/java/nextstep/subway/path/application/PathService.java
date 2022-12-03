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

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathResponse findPath(Long sourceId, Long targetId) {

        List<Line> lines = lineRepository.findAll();

        Station source = stationRepository.findById(sourceId).orElseThrow(IllegalArgumentException::new);
        Station target = stationRepository.findById(targetId).orElseThrow(IllegalArgumentException::new);

        PathFinder pathFinder = new PathFinder(lines);
        return new PathResponse(pathFinder.findPath(source, target));
    }
}
