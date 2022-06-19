package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.PathResponse;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse get(long sourceStationId, long targetStationId) {
        Station source = findStationById(sourceStationId);
        Station target = findStationById(targetStationId);
        return pathFinder.getShortestPath(new PathRequest(findLines(), source, target));
    }

    private Station findStationById(long stationId) {
        return stationRepository.findById(stationId).orElseThrow(NoSuchElementException::new);
    }

    private Lines findLines() {
        return new Lines(lineRepository.findAll());
    }

}
