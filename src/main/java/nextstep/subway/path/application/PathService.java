package nextstep.subway.path.application;

import nextstep.subway.auth.domain.AccessMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.collections.Lines;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(AccessMember accessMember, Long stationId, Long targetId) {
        Station source = stationService.findStationById(stationId);
        Station target = stationService.findStationById(targetId);
        Lines lines = new Lines(lineRepository.findAll());

        ShortestPath shortestPath = lines.findShortestPath(source, target);
        int fare = shortestPath.calculateFare(accessMember.getMemberFarePolicy());
        return PathResponse.of(shortestPath, fare);
    }
}
