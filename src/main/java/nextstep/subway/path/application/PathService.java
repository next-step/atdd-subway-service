package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;

    public PathService(
            LineRepository lineRepository,
            StationRepository stationRepository,
            StationService stationService
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        stationService.validateStation(source);
        stationService.validateStation(target);
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPath(source, target);
        Fare fare = new Fare(path.getDistance(), path.getAdditionalFareByLine(), loginMember.getAge());
        List<StationResponse> stations = stationService.findAllByIdIsIn(path.getStationIds());
        return PathResponse.from(stations, path.getDistance(), fare.getFare());
    }
}
