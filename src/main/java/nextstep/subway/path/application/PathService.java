package nextstep.subway.path.application;

import java.util.*;

import org.springframework.stereotype.*;

import nextstep.subway.common.exception.*;
import nextstep.subway.line.domain.*;
import nextstep.subway.path.domain.*;
import nextstep.subway.station.domain.*;

@Service
public class PathService {
    private static final String STATION = "역";

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public Path shortPath(long source, long target) {
        Station startStation = stationRepository.findById(source)
            .orElseThrow(() -> new NotFoundException(STATION));
        Station endStation = stationRepository.findById(target)
            .orElseThrow(() -> new NotFoundException(STATION));

        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.from(lines);
        return pathFinder.shortestPath(startStation, endStation);
    }
}
