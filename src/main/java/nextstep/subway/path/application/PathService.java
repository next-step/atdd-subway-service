package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.FareCalculateResolver;
import nextstep.subway.path.domain.FareDiscountResolver;
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
    private final FareCalculateResolver fareCalculateResolver;
    private final FareDiscountResolver fareDiscountResolver;

    public PathService(
            StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder,
            FareCalculateResolver fareCalculateResolver, FareDiscountResolver fareDiscountResolver) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
        this.fareCalculateResolver = fareCalculateResolver;
        this.fareDiscountResolver = fareDiscountResolver;
    }

    public PathResponse get(LoginMember loginMember, long sourceStationId, long targetStationId) {
        Station source = findStationById(sourceStationId);
        Station target = findStationById(targetStationId);
        ShortestPath shortestPath = pathFinder.getShortestPath(new PathRequest(findLines(), source, target));
        Fare fare = fareCalculateResolver.resolve(shortestPath.getDistance()).add(shortestPath.mostExpensive());
        return PathResponse.of(shortestPath, fareDiscountResolver.resolve(loginMember, fare));
    }

    private Station findStationById(long stationId) {
        return stationRepository.findById(stationId).orElseThrow(NoSuchElementException::new);
    }

    private Lines findLines() {
        return new Lines(lineRepository.findAll());
    }

}
