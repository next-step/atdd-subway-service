package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
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

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(LoginMember loginMember, PathRequest pathRequest) {
        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());
        List<Line> lines = lineRepository.findAll();
        Path path = new Path(lines,source, target);
        Fare fare = new Fare(loginMember.getAge(), path.findWeight());
        return PathResponse.of(path, fare);
    }
}
