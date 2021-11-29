package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.calculator.FareCalculator;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.path.finder.DijkstraShortestPathAlgorithm;
import nextstep.subway.path.finder.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long sourceId, Long targetId) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);

        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(lines));
        ShortestPath path = pathFinder.findShortestPath(sourceStation, targetStation);

        return createPathResponse(lines, path);
    }

    private PathResponse createPathResponse(List<Line> lines, ShortestPath path) {
        int pathFare = FareCalculator.calculatePathFare(path.getDistance());
        int lineFare = FareCalculator.calculateLineFare(lines, path);

        return PathResponse.of(StreamUtils.mapToList(path.getStations(), StationResponse::of),
                                                     path.getDistance(),
                                                     pathFare + lineFare);
    }
}
