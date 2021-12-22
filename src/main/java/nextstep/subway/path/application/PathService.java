package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.Member;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathInfoService pathInfoService;

    public PathService(LineService lineService, StationService stationService, PathInfoService pathInfoService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathInfoService = pathInfoService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Member loginMember, Long startStationId, Long destinationStationId) {
        List<Line> lines = lineService.findAllLines();

        Station source = stationService.findStationById(startStationId);
        Station target = stationService.findStationById(destinationStationId);

        PathInfo pathInfo = pathInfoService.calculatePathInfo(lines, source, target, loginMember);
        return PathResponse.of(pathInfo);
    }
}
