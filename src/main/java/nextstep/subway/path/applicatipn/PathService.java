package nextstep.subway.path.applicatipn;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.SourceAndTargetStationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse getPath(Long sourceId, Long targetId, LoginMember loginMember) {
        SourceAndTargetStationDto station = stationService.findStationById(sourceId, targetId);
        List<Line> lines = lineService.findAll();
        PathStrategy strategy = new DijkstraShortestPathStrategy(lines);
        PathFinder pathFinder = strategy.getShortPath(station.getSourceStation(), station.getTargetStation());

        int fee = Lines.from(lines).getPaidFee(
                pathFinder.getStations(), pathFinder.getDistance(), new KmPerFeePolicy());

        AgeStrategy agePolicy = new DefaultAgePolicy(
                loginMember.getAge(), loginMember.getMemberType(), fee);

        return PathResponse.from(pathFinder, agePolicy.discount());
    }
}
