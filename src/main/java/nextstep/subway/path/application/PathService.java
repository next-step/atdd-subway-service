package nextstep.subway.path.application;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.DijkstraAlgorithm;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private static final String STATION = "역";

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Path shortestPath(long source, long target) {
        Station startStation = findStation(source);
        Station endStation = findStation(target);

        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.from(lines, new DijkstraAlgorithm());
        return pathFinder.shortestPath(startStation, endStation);
    }

    private Station findStation(long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(STATION));
    }
}
