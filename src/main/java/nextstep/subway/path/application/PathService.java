package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.StationGraph;
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
    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(LoginMember loginMember, PathRequest pathRequest) {
        Station source = stationService.stationById(pathRequest.getSource());
        Station target = stationService.stationById(pathRequest.getTarget());
        List<Line> lines = lineRepository.findAll();

        Path path = new StationGraph(lines, loginMember).findShortestPath(source, target);

        return PathResponse.of(path);
    }
}
