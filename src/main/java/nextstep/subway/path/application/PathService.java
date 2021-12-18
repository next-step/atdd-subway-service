package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(final LoginMember loginMember, final PathRequest pathRequest) {
        final List<Line> lines = lineService.findAll();
        final Path path = new PathFinder(lines).findPath(
            stationService.getStationById(pathRequest.getSource()),
            stationService.getStationById(pathRequest.getTarget())
        );
        final Fare fare = path.calculateFare(loginMember);
        return PathResponse.of(path, fare.getFare());
    }
}
