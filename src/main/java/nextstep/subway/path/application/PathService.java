package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target) {
        Station sourceStation = stationRepository.findById(source).orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(target).orElseThrow(RuntimeException::new);
        Path path = new Path(sourceStation, targetStation);
        List<Line> lines = lineRepository.findAll();
        return PathResponse.of(path.findShortPath(lines), path.calculateDistance(lines.size()));
    }
}
