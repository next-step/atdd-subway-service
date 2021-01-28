package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.FareCalculater;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {
    private LineRepository lineRepository;
    private StationService stationService;
    private FareCalculater fareCalculater;

    public PathService(LineRepository lineRepository, StationService stationService, FareCalculater fareCalculater) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.fareCalculater = fareCalculater;
    }

    public PathResponse findPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        List<Line> allLines = lineRepository.findAll();
        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);
        return PathFinder.findPath(Sections.merge(allLines), source, target)
                        .map(path -> PathResponse.of(path, fareCalculater.calculateFare(loginMember, path)))
                        .orElseThrow(() -> new IllegalArgumentException("최단 경로를 찾을 수 없습니다."));
    }
}
