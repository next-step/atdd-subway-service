package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(PathRequest request, LoginMember loginMember) {
        PathFinder pathFinder = PathFinder.of(lineRepository.findAll());
        Path path = pathFinder.findShortestPath(request.getSource(), request.getTarget());
        List<Station> stations = stationRepository.findAllById(path.getStationIds());

        FareCalculator fareCalculator = new FareCalculator(path);

        return PathResponse.from(
                stations,
                path.getDistance(),
                fareCalculator.calculate(loginMember.getAge()).value());
    }

}
