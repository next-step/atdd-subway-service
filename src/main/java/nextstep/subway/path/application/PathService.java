package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.FareCalculator;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinderStrategy;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathFinderStrategy pathFinderStrategy;
    private final FareCalculator fareCalculator;

    public PathService(LineRepository lineRepository, StationService stationService, PathFinderStrategy pathFinderStrategy, FareCalculator fareCalculator) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathFinderStrategy = pathFinderStrategy;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse getShortestDistance(LoginMember loginMember, PathRequest request) {
        List<Line> lines = lineRepository.findAll();
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Path path = pathFinderStrategy.getShortestDistance(new Lines(lines), source, target);
        fareCalculator.calculate(path, loginMember);

        return PathResponse.of(path);
    }
}
