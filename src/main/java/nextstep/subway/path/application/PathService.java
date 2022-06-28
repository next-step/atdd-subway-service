package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.JgraphPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(final StationService stationService, final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
        public PathResponse getShortestPath(final LoginMember loginMember, final Long startStationId, final Long destinationStationId) {
            final Station start = stationService.findById(startStationId);
            final Station destination = stationService.findById(destinationStationId);
            final List<Line> lines = lineService.findAll();
            final PathFinder pathFinder = new JgraphPathFinder();

            return PathResponse.of(pathFinder.getShortestPath(lines, start, destination), loginMember);
    }


}
