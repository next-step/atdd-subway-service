package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathInfo;
import nextstep.subway.path.domain.PathInfoCalculator;
import nextstep.subway.path.domain.StationEdge;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.constants.StationErrorMessages;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationRepository.findById(source)
                .orElseThrow(() -> new RuntimeException(StationErrorMessages.STATION_DOES_NOT_EXIST));
        Station targetStation = stationRepository.findById(target)
                .orElseThrow(() -> new RuntimeException(StationErrorMessages.STATION_DOES_NOT_EXIST));

        GraphPath<Station, StationEdge> path = PathFinder.findPath(lines, sourceStation, targetStation);
        PathInfo pathInfo = PathInfoCalculator.calculatePathInfo(loginMember, path, lines);
        return PathResponse.of(path, pathInfo);
    }
}
