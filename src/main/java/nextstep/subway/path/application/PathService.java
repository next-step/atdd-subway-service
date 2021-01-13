package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.fare.FareCalculator;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;

    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse getPath(PathRequest pathRequest, LoginMember loginMember) {
        List<Line> lines = lineRepository.findAll();

        Station sourceStation = stationService.findStationById(pathRequest.getSource());
        Station targetStation = stationService.findStationById(pathRequest.getTarget());

        PathFinder pathFinder = new PathFinder(lines);
        GraphPath<Station, Section> path = pathFinder.getPath(sourceStation, targetStation);

        FareCalculator fareCalculator = new FareCalculator((int)path.getWeight(), path.getEdgeList(), loginMember);
        return PathResponse.of(path, fareCalculator.calculateFare());
    }
}
