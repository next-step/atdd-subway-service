package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.FareResolver;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Fare;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;
    private final FareResolver fareResolver;

    public PathService(
            StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder,
            FareResolver fareResolver) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
        this.fareResolver = fareResolver;
    }

    public PathResponse get(long sourceStationId, long targetStationId) {
        Station source = findStationById(sourceStationId);
        Station target = findStationById(targetStationId);
        ShortestPath shortestPath = pathFinder.getShortestPath(new PathRequest(findLines(), source, target));
        Fare fare = fareResolver.resolve(shortestPath.getDistance());
        fare.add(shortestPath.mostExpensive());
        return PathResponse.of(shortestPath, fare);
    }

    private Station findStationById(long stationId) {
        return stationRepository.findById(stationId).orElseThrow(NoSuchElementException::new);
    }

    private Lines findLines() {
        return new Lines(lineRepository.findAll());
    }

}
