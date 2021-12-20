package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathStrategy pathStrategy;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathStrategy = new DijkstraShortest();
    }

    public PathResponse findShortestPath(final LoginMember loginMember, final Long source, final Long target) {
        final Station sourceStation = stationService.findStationById(source);
        final Station targetStation = stationService.findStationById(target);
        final List<Line> lines = lineRepository.findAll();
        final Path path = pathStrategy.getShortestPath(lines, sourceStation, targetStation);
        final Fare distanceFare = PathFareDistance.of(path);
        final Fare ageFare = PathFareAge.of(loginMember);
        return PathResponse.of(path, distanceFare.plus(ageFare));
    }
}
